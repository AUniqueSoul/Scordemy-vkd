package com.laboontech.scordemy.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.HomeActivity;
import com.laboontech.scordemy.adapters.TestListAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentHomeBinding;
import com.laboontech.scordemy.databinding.FragmentTestBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.interfaces.ITestListener;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class TestFragment extends Fragment implements ITestListener {
    private static final String TAG = "TestFragmentTest";
    private Context context;
    private FragmentTestBinding binding;
    private DisposableSingleObserver<Test> disposableSingleObserver;
    private RetrofitApi mService;
    private String user_id;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;
    public static int view_type = 1; //1 for TYPE_TOPIC default , 2 for TYPE_SUB_TOPIC
    public static List<Test> list;

    public TestFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        assert context != null;
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }
        list = new ArrayList<>();


        if (view_type == 1) {
            binding.tvTestTitle.setText(getString(R.string.test_series));
            binding.tvDescription.setText(getString(R.string.take_tests_and) + " " + getString(R.string.know_your_standings_in_the_race));
            binding.close.setVisibility(View.GONE);
        } else {
            binding.close.setVisibility(View.VISIBLE);
            setTestAdapter();

        }
        binding.close.setOnClickListener(view1 -> {
            view_type = 1;
            HomeActivity.toolbar.setVisibility(View.VISIBLE);
            ((HomeActivity) requireActivity()).setFragment(new TestFragment(),"test_fragment");
        });
        initTestList();
        return view;
    }


    private void initTestList() {
        disposableSingleObserver = mService.test(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Test>() {
                    @Override
                    public void onSuccess(@NonNull Test test) {
                        Log.d(TAG, "onSuccess: " + test.code);
                        if (test.code.equals(Constants.SUCCESS)) {
                            list.clear();
                            list = test.res;
                            setTestAdapter();
                            binding.progressBar.setVisibility(View.GONE);
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


    private void setTestAdapter() {
        binding.rvTestTopic.setHasFixedSize(true);
        binding.rvTestTopic.setLayoutManager(new LinearLayoutManager(context));
        TestListAdapter adapter = new TestListAdapter(list, view_type, this);
        binding.rvTestTopic.setAdapter(adapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        view_type = 1;
        if (disposableSingleObserver != null) {
            disposableSingleObserver.dispose();
        }
    }

    @Override
    public void onItemClickListener(Test data, int position) {
        binding.tvTestTitle.setText(data.title);
        binding.tvDescription.setText(data.description);
        list.clear();
        list = data.test_sub_topic_list;
        view_type = 2;
        binding.close.setVisibility(View.VISIBLE);
        HomeActivity.toolbar.setVisibility(View.GONE);
        setTestAdapter();
    }
}