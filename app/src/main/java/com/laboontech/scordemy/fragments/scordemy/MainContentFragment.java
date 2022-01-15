package com.laboontech.scordemy.fragments.scordemy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.adapters.ViewPagerAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentMainContentBinding;
import com.laboontech.scordemy.entity.HomeData;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.fragments.NotesFragment;
import com.laboontech.scordemy.fragments.PracticeQuizListFragment;
import com.laboontech.scordemy.fragments.VideoLectureFragment;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.observers.DisposableSingleObserver;


public class MainContentFragment extends Fragment {
    private static final String TAG = "ClassFragment";
    private Context context;
    private FragmentMainContentBinding binding;
    private DisposableSingleObserver<HomeData> disposableSingleObserver;
    private RetrofitApi mService;
    private List<HomeData> list;

    private String user_id;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public MainContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainContentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        list = new ArrayList<>();
        initViewPager();
        return view;
    }


    //set viewpager with tab layout
    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);


        viewPagerAdapter.addFragment(new VideoLectureFragment(), "Video Lectures");
        viewPagerAdapter.addFragment(new PracticeQuizListFragment(), "Question Bank");
        viewPagerAdapter.addFragment(new NotesFragment(), "Concept Notes");

        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposableSingleObserver != null) {
            disposableSingleObserver.dispose();
        }

    }


}