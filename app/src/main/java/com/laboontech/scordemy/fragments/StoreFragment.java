package com.laboontech.scordemy.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MyStoreActivity;
import com.laboontech.scordemy.adapters.HomeDataAdapter;
import com.laboontech.scordemy.adapters.StoreLiveCourseAdapter;
import com.laboontech.scordemy.adapters.StoreTestSeriesAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentPracticeQuizBinding;
import com.laboontech.scordemy.databinding.FragmentStoreBinding;
import com.laboontech.scordemy.entity.HomeData;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.interfaces.IStorePurchaseListener;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoreFragment extends Fragment {
    private static final String TAG = "StoreFragmentTest";
    private Context context;
    private FragmentStoreBinding binding;
    private RetrofitApi mService;
    private String user_id;
    private User userData;
    private DisposableSingleObserver<Test> disposableSingleObserver;
    private DisposableSingleObserver<HomeData> disposableSingleObserverHome;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private int test_id = 0;
    private int live_course_id = 0;

   private Test testData;
    private Video liveData;
private    Gson gson;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStoreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
         gson = new Gson();
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        Animations.fade_in(context, binding.cvTest);
        initTestSeries();
        initLiveCourses();
        initHomeContent();
        initActions();
        return view;
    }
    private void initActions() {
        binding.buyTestSeries.setOnClickListener(v -> {
            if (test_id != 0) {
                //open test detail page
                if (testData!=null){
                    if (testData.user_subscribed_test==1){
                        Functions.showSnackBar(binding.getRoot(),"Already Purchased");
                    }else{
                        String test_data =gson.toJson(testData);
                        Intent intent = new Intent(context, MyStoreActivity.class);
                        intent.putExtra(Constants.KEY_TYPE,Constants.TYPE_TEST);
                        intent.putExtra(Constants.KEY_CONTENT_DATA,test_data);
                        startActivity(intent);
                    }

                }

            } else {
                Functions.showSnackBar(binding.getRoot(), "Please select Test Series");
                Animations.shake(context, binding.buyTestSeries);
            }
        });
        binding.buyLiveCourse.setOnClickListener(v -> {
            if (live_course_id != 0) {
                //open live course detail page
                if (liveData!=null){
                    if (liveData.is_userSubscribed==1){
                        Functions.showSnackBar(binding.getRoot(),"Already Purchased");
                    }else{
                        String live_data =gson.toJson(liveData);
                        Intent intent = new Intent(context, MyStoreActivity.class);
                        intent.putExtra(Constants.KEY_TYPE,Constants.TYPE_LIVE);
                        intent.putExtra(Constants.KEY_CONTENT_DATA,live_data);
                        startActivity(intent);
                    }

                }
            } else {
                Functions.showSnackBar(binding.getRoot(), "Please select Live Course");
                Animations.shake(context, binding.buyLiveCourse);
            }
        });
    }





    private void initHomeContent() {
        disposableSingleObserverHome = mService.home_data()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HomeData>() {
                    @Override
                    public void onSuccess(HomeData data) {
                        if (data.code.equals(Constants.SUCCESS)){
                            List<HomeData> list = new ArrayList<>();
                            for (HomeData dt : data.res){
                                list.add(new HomeData(dt.home_data_id,dt.title));
                            }
                            ArrayAdapter<HomeData> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,list);
                            binding.homeContent.setAdapter(arrayAdapter);
                            binding.homeContent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    HomeData data = (HomeData)parent.getSelectedItem();
                                    Toast.makeText(context, ""+data.home_data_id, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }else {
                            Log.d(TAG, "onSuccess: "+data.error_msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: exception " + e.getMessage());
                    }
                });

    }

    private void initLiveCourses() {
        mService.live_videos(user_id)
                .enqueue(new Callback<Video>() {
                    @Override
                    public void onResponse(@androidx.annotation.NonNull Call<Video> call, @androidx.annotation.NonNull Response<Video> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS) && response.body().res.size()>0) {

                                binding.rvLiveCourses.setAdapter(new StoreLiveCourseAdapter(response.body().res, new IStorePurchaseListener() {
                                    @Override
                                    public void onTestSeriesPurchaseClick(Test data) {

                                    }

                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onLiveCoursePurchaseClick(Video data) {
                                        Log.d(TAG, "onTestSeriesPurchaseClick: Test id  :- " + data.lc_id);
                                        Log.d(TAG, "onTestSeriesPurchaseClick: Price :- " + data.ic_price);
                                        live_course_id =Integer.parseInt(data.lc_id);
                                        binding.livePrice.setText(context.getString(R.string.rupee)+" "+data.ic_price);
                                        liveData=data;
                                    }
                                }));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Call<Video> call, @androidx.annotation.NonNull Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }




    private void initTestSeries() {
        disposableSingleObserver = mService.test(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Test>() {
                    @Override
                    public void onSuccess(@NonNull Test test) {
                        Log.d(TAG, "onSuccess: " + test.code);
                        if (test.code.equals(Constants.SUCCESS)) {
                            binding.rvTestSeries.setAdapter(new StoreTestSeriesAdapter(test.res, new IStorePurchaseListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onTestSeriesPurchaseClick(Test data) {
                                    Log.d(TAG, "onTestSeriesPurchaseClick: Test id  :- " + data.test_topic_id);
                                    Log.d(TAG, "onTestSeriesPurchaseClick: Price :- " + data.tt_price);
                                    test_id=data.test_topic_id;
                                    binding.testPrice.setText(context.getString(R.string.rupee)+" "+data.tt_price);
                                    testData=data;
                                }

                                @Override
                                public void onLiveCoursePurchaseClick(Video data) {

                                }
                            }));
                            Animations.shake(context, binding.buyTestSeries);
                        } else {
                            Functions.ShowToast(context, "" + test.error_msg);
                        }
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
    }

}