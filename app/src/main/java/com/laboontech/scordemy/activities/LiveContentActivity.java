package com.laboontech.scordemy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.ebooks.adapters.EbookBookSolutionAdapter;
import com.laboontech.scordemy.activities.ebooks.adapters.LiveContentAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityEbookBinding;
import com.laboontech.scordemy.databinding.ActivityLiveContentBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.entity.LiveContentVideo;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.fragments.DetailOverViewFragment;
import com.laboontech.scordemy.fragments.VideoLectureFragment;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveContentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LiveContentActivTest";
    private Activity activity;
    private ActivityLiveContentBinding binding;
    private RetrofitApi mService;
    private String live_course_id,title , price, schedule, instruction;
    List<LiveContentVideo> list = new ArrayList<>();
    public int is_userSubscribed=0;
    Video liveData;
    Gson gson;
   public String  str_live_data, type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveContentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
gson = new Gson();
        mService = RetrofitClient.getAPI();

        if(getIntent()!=null){
            str_live_data = getIntent().getStringExtra(Constants.KEY_CONTENT_DATA);
            type = getIntent().getStringExtra(Constants.KEY_TYPE);
            liveData = gson.fromJson(str_live_data,Video.class);
            live_course_id = liveData.lc_id;
            title = liveData.lc_title;
            price = liveData.ic_price;
            schedule = liveData.lc_schedule_period;
            instruction =liveData.ic_instructions;
            is_userSubscribed= liveData.is_userSubscribed;

            if (is_userSubscribed==1){
                binding.tvPro.setVisibility(View.VISIBLE);
            }else{
                binding.tvPro.setVisibility(View.GONE);
            }
        }


        initViews();
    }

    private void initViews() {

        binding.live.setOnClickListener(this);
        binding.practice.setOnClickListener(this);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.tvDetails.setOnClickListener(v -> {
            showDetailsOverview();
        });

        getLiveContentData("live");
    }


    public void showDetailsOverview() {

        Bundle args = new Bundle();
        args.putString("Title", title);
        args.putString("Price", price);
        args.putString("Scheduled", schedule);
        args.putString("Instructions", instruction);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        DetailOverViewFragment newFragment = new DetailOverViewFragment();
        newFragment.setArguments(args);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }


    private void getLiveContentData(String type) {
        binding.progressBar.setVisibility(View.VISIBLE);
        mService.getLiveContent(live_course_id, type)
                .enqueue(new Callback<LiveContentVideo>() {
                    @Override
                    public void onResponse(@NonNull Call<LiveContentVideo> call, @NonNull Response<LiveContentVideo> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                binding.progressBar.setVisibility(View.GONE);
                                if (response.body().res.size() > 0) {
                                    list = response.body().res;
                                    binding.recyclerview.setAdapter(new LiveContentAdapter(response.body().res));

                                } else {
                                    Log.d(TAG, "onResponse: "+response.body().error_msg);
//                                    Functions.ShowToast(activity, "No Data Found");
                                }
                            } else {
                                Functions.ShowToast(activity, response.body().error_msg);
                                binding.progressBar.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LiveContentVideo> call, @NonNull Throwable t) {
                        Functions.ShowToast(activity, t.getMessage());
                        binding.progressBar.setVisibility(View.GONE);

                    }
                });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live:
                binding.liveBorder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.border));
                binding.practiceBorder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.border_transparent));
                getLiveContentData("live");
                break;
            case R.id.practice:
                binding.liveBorder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.border_transparent));
                binding.practiceBorder.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.border));
               getLiveContentData("practice");
                break;


        }
    }
}
