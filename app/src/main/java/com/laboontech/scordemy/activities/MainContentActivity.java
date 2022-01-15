package com.laboontech.scordemy.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.adapters.ViewPagerAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;

import com.laboontech.scordemy.databinding.ActivityMainContentBinding;
import com.laboontech.scordemy.entity.HomeData;
import com.laboontech.scordemy.entity.Subscription;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.fragments.NotesFragment;
import com.laboontech.scordemy.fragments.PracticeQuizListFragment;
import com.laboontech.scordemy.fragments.VideoLectureFragment;
import com.laboontech.scordemy.fragments.scordemy.CategoryFragment;
import com.laboontech.scordemy.fragments.scordemy.SubCategoryFragment;
import com.laboontech.scordemy.fragments.scordemy.SubjectFragment;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;
import java.util.Objects;

import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainContentActivity extends AppCompatActivity {
    private static final String TAG = "ClassActivityTest";
    private ActivityMainContentBinding binding;
    private Activity activity;
    private List<HomeData> list;

    // Disposal
    DisposableSingleObserver<HomeData> disposableSingleObserver;

    //Retrofit api
    RetrofitApi mService;


    public static String home_id, subject_id, chapter_id, class_id;
    public static String user_subscription_status="0";
    public boolean isSubjectFragment = false;
    public String back_title;
    public String currentScreen = "medium";
    private SharedPreferenceUtil sharedPreferenceUtil;
    private String user_id;
    private User userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainContentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initViews();
        setFragment(new CategoryFragment());
        sharedPreferenceUtil = new SharedPreferenceUtil(activity);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        user_id=userData.user_id;

    }

    private void initViews() {
        mService = RetrofitClient.getAPI();
        Variables.sharedPreferences = getSharedPreferences(Variables.my_shared_pref, MODE_PRIVATE);
        Glide.with(activity).load(Variables.IMAGE_PATH + Variables.sharedPreferences.getString(Variables.image, "")).into(binding.imageView);
        home_id = Variables.sharedPreferences.getString("home_id", "");
        back_title = Variables.sharedPreferences.getString(Constants.BACK_TITLE, "");
        setTitle(back_title);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void seFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft = ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
    public void setFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft = ft.add(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (chapter_id != null) {
            chapter_id = null;
//            setFragment(new SubCategoryFragment());
            setTitle("Chapters");
        } else if (isSubjectFragment) {

            isSubjectFragment = false;
            subject_id = null;
//            setFragment(new SubjectFragment());
            setTitle("Subjects");


        } else if (subject_id != null) {

            subject_id = null;
//            setFragment(new CategoryFragment());
            setTitle(back_title);


        } else if (class_id != null) {
            class_id = null;
            setTitle(back_title);

        } else{
            super.onBackPressed();
        }       super.onBackPressed();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SUBSCRIPTION_SUCCESS && resultCode == RESULT_OK && data != null) {
            String status = data.getStringExtra(Razorpay.EXTRA_SUBSCRIPTION_STATUS);
            String home_title = data.getStringExtra(Razorpay.EXTRA_SUBSCRIPTION_HOME_TITLE);
            String content_name = data.getStringExtra(Razorpay.EXTRA_SUBSCRIPTION_CONTENT_NAME);
            assert status != null;
            if (status.equalsIgnoreCase("success")){
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_subscription_success);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.findViewById(R.id.btnContinue).setOnClickListener(v ->  recreate());
                TextView title = dialog.findViewById(R.id.tvTitle);
                title.setText(home_title + " - " + content_name);
                dialog.show();

            }
        }
    }


    public  void checkUserSubscriptionStatus(String content_id){
        user_subscription_status = "0";
        Log.d(TAG, "checkUserSubscriptionStatus: user_id "+ content_id);
        Log.d(TAG, "checkUserSubscriptionStatus: home id  "+home_id);
        mService.checkMySubscriptionStatus(user_id,content_id,home_id)
                .enqueue(new Callback<Subscription>() {
                    @Override
                    public void onResponse(@NonNull Call<Subscription> call, @NonNull Response<Subscription> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                user_subscription_status = response.body().status;
                                Log.d(TAG, "onResponse: "+user_subscription_status);
                            }else{
                                Log.d(TAG, "onResponse: "+response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Subscription> call, @NonNull Throwable t) {
                        Functions.ShowToast(activity, t.getMessage());
                    }
                });
    }
}