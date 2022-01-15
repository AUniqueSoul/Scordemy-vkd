package com.laboontech.scordemy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.TestsExploreActivity;
import com.laboontech.scordemy.activities.TestsSubListActivity;
import com.laboontech.scordemy.adapters.LiveVideoAdapter;
import com.laboontech.scordemy.adapters.TestsListAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentMyContentBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyContentFragment extends Fragment {
    private static final String TAG = "MyPurchasesFragmTest";
    private Context context;
    private FragmentMyContentBinding binding;
    private RetrofitApi mService;
    private String user_id;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private DisposableSingleObserver<Test> disposableSingleObserver;

    private List<Video> liveList = new ArrayList<>();
    private List<Test> testList = new ArrayList<>();

    public MyContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyContentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;

        initMyTestSeries();
        initMyLiveCourse();

        return view;
    }

    private void initMyTestSeries() {
        disposableSingleObserver = mService.test(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Test>() {
                    @Override
                    public void onSuccess(@NonNull Test test) {
                        Log.d(TAG, "onSuccess: " + test.code);
                        if (test.code.equals(Constants.SUCCESS)) {
                            for (Test data : test.res){
                                if (data.user_subscribed_test==1){
                                    testList.add(data);
                                }
                            }
                            binding.rvTestSeries.setAdapter(new TestsListAdapter(testList, (v, data) -> {
                                switch (v.getId()){
                                    case R.id.tvExplore:
                                        if (data.user_subscribed_test==1){
                                            Bundle args = new Bundle();
                                            Intent intent = new Intent(context, TestsSubListActivity.class);
                                            intent.putExtra("Title", data.title);
                                            intent.putExtra("test", (Serializable) data.test_sub_topic_list);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(context, TestsExploreActivity.class);
                                            Gson gson = new Gson();
                                            String testData = gson.toJson(data);
                                            intent.putExtra(Constants.KEY_CONTENT_DATA,testData);
                                            intent.putExtra("test", (Serializable) data.test_sub_topic_list);
                                            context.startActivity(intent);
                                        }
                                        break;

                                    case R.id.lyt:
                                        Bundle args = new Bundle();
                                        Intent intent = new Intent(context, TestsSubListActivity.class);
                                        intent.putExtra("Title", data.title);
                                        intent.putExtra("test", (Serializable) data.test_sub_topic_list);
                                        startActivity(intent);
                                        break;

                                }
                            }));
                        } else {
                        binding.testError.setVisibility(View.VISIBLE);
                        }
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
    }
    private void initMyLiveCourse() {
        mService.live_videos(user_id)
                .enqueue(new Callback<Video>() {
                    @Override
                    public void onResponse(@androidx.annotation.NonNull Call<Video> call, @androidx.annotation.NonNull Response<Video> response) {
                        if (response.body()!=null){
                            if (response.body().code.equals(Constants.SUCCESS)){
                                for (Video data : response.body().res){
                                    if (data.is_userSubscribed==1){
                                        liveList.add(data);
                                    }
                                }
                                binding.rvLiveCourse.setAdapter(new LiveVideoAdapter(liveList));
                            }else{
                                binding.testError.setVisibility(View.VISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Call<Video> call, @androidx.annotation.NonNull Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
}