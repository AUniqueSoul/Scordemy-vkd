package com.laboontech.scordemy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.adapters.LiveVideoAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentLiveClassesBinding;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveClassesFragment extends Fragment  {
    private static final String TAG = "LiveClassesFragment";
    private Context context;
    private FragmentLiveClassesBinding binding;
    private RetrofitApi mService;
    private List<Video> list ;
    private LiveVideoAdapter adapter ;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private String user_id;
    private User userData;
    public LiveClassesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLiveClassesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        list = new ArrayList<>();
        context = getContext();
        assert context != null;
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        fetchLiveVideos();

        return view;
    }

    private void fetchLiveVideos() {
    mService.live_videos(user_id)
            .enqueue(new Callback<Video>() {
                @Override
                public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                    if (response.body()!=null){
                        if (response.body().code.equals(Constants.SUCCESS)){
                            if(response.body().res!=null && response.body().res.size()>0){
                                Log.d(TAG, "onResponse: "+response.body().res);
                                adapter = new LiveVideoAdapter(response.body().res);
                                binding.recyclerview.setAdapter(adapter);

                            }else{
                                Functions.ShowToast(context,"No data found");

                            }
                            binding.progressBar.setVisibility(View.GONE);
                        }else{
                            Functions.ShowToast(context,response.body().error_msg);
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Video> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                }
            });
    }


}