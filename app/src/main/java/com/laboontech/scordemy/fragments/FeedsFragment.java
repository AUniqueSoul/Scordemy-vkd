package com.laboontech.scordemy.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.adapters.FeedAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentFeedsBinding;
import com.laboontech.scordemy.entity.Feed;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.interfaces.IFeedOnItemClickListener;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class FeedsFragment extends Fragment implements View.OnClickListener , IFeedOnItemClickListener {
    private static final String TAG = "HomeFragmentTest";
    private Context context;
    private FragmentFeedsBinding binding;
    private DisposableSingleObserver<Feed> disposableSingleObserver;
    private DisposableCompletableObserver disposableCompletableObserver;
    private RetrofitApi mService;
    private List<Feed> list;


    private FeedAdapter adapter;

    private String user_id;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public FeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        binding.blog.setOnClickListener(this);
        binding.news.setOnClickListener(this);
        binding.videos.setOnClickListener(this);
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        list = new ArrayList<>();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
         adapter = new FeedAdapter(context,list,this);
        binding.recyclerView.setAdapter(adapter);
        getFeed("blog",0);
        return view;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blog:
                binding.blogBorder.setBackground(getResources().getDrawable(R.drawable.border));
                binding.newsBorder.setBackground(getResources().getDrawable(R.drawable.border_transparent));
                binding.videosBorder.setBackground(getResources().getDrawable(R.drawable.border_transparent));
                getFeed("blog",0);
                break;
            case R.id.news:
                binding.blogBorder.setBackground(getResources().getDrawable(R.drawable.border_transparent));
                binding.newsBorder.setBackground(getResources().getDrawable(R.drawable.border));
                binding.videosBorder.setBackground(getResources().getDrawable(R.drawable.border_transparent));
                getFeed("news",0);
                break;
            case R.id.videos:
                binding.blogBorder.setBackground(getResources().getDrawable(R.drawable.border_transparent));
                binding.newsBorder.setBackground(getResources().getDrawable(R.drawable.border_transparent));
                binding.videosBorder.setBackground(getResources().getDrawable(R.drawable.border));
                Variables.video_type = "feed";
                setFragment(new VideoLectureFragment());
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft = ft.replace(R.id.fragment_container, fragment);
        ft.commit();
        binding.recyclerView.setVisibility(View.GONE);
        binding.fragmentContainer.setVisibility(View.VISIBLE);
    }




    private void getFeed(String type,int last_id){
       disposableSingleObserver =  mService.feed(type,user_id,last_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Feed>() {
                    @Override
                    public void onSuccess(@NonNull Feed response) {

                        if (response.code.equals(Constants.SUCCESS)){
                            list.clear();
                            list.addAll(response.res);
                            adapter.notifyDataSetChanged();
                            binding.progressBar.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            binding.fragmentContainer.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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
        if (disposableCompletableObserver!=null){
            disposableCompletableObserver.dispose();
        }

    }


    /**
     * Feed adapter listener methods
     * @param item
     * @param view
     */
    @Override
    public void onItemClickListener(Feed item, View view) {
        switch (view.getId()) {
            case R.id.like:
                likeApi(item);
                break;
        }
    }


    private void likeApi(Feed data) {
        disposableCompletableObserver = mService.like_unlike_feed(data.id, user_id, data.liked)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete:   liked done ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
    }

}