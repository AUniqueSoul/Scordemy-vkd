package com.laboontech.scordemy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.adapters.VideosAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentVideoLectureBinding;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.laboontech.scordemy.utils.Variables.chapter_id;
import static com.laboontech.scordemy.utils.Variables.home_data_id;
import static com.laboontech.scordemy.utils.Variables.user_id;


public class VideoLectureFragment extends Fragment {
    private static final String TAG = "VideoLectureFragTest";
    private Context context;
    private FragmentVideoLectureBinding binding;
    private DisposableSingleObserver<Video> disposableSingleObserver;
    private RetrofitApi mService;
private List<Video> list;
private VideosAdapter adapter ;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;
    public VideoLectureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoLectureBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();

        binding.recyclerView.setHasFixedSize(true);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        list = new ArrayList<>();
        adapter = new VideosAdapter(context,list);
        binding.recyclerView.setAdapter(adapter);
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        initVideos();
        return view;
    }

    private void initVideos() {
        Log.d(TAG, "initVideos: "+Variables.video_type);
        disposableSingleObserver =  mService.videos(MainContentActivity.chapter_id,user_id, Variables.video_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Video>() {
                    @Override
                    public void onSuccess(Video response) {
                        Log.d(TAG, "onSuccess: "+response.code);
                        if (response.code.equals(Constants.SUCCESS)){
                            list.clear();
                            list.addAll(response.res);
                            adapter.notifyDataSetChanged();
                            binding.progressBar.setVisibility(View.GONE);

                        }else{
                            Functions.ShowToast(context,response.error_msg);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                        Functions.ShowToast(context,"error "+e.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposableSingleObserver!=null){
        disposableSingleObserver.dispose();
    }
    }
}