package com.laboontech.scordemy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.TestsExploreActivity;
import com.laboontech.scordemy.activities.TestsSubListActivity;
import com.laboontech.scordemy.adapters.LiveVideoAdapter;
import com.laboontech.scordemy.adapters.TestListAdapter;
import com.laboontech.scordemy.adapters.TestsListAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentTestBinding;
import com.laboontech.scordemy.databinding.FragmentTestsBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class TestsFragment extends Fragment implements TestsListAdapter.OnItemClickListener{

    private static final String TAG = "TestFragmentTest";
    private FragmentTestsBinding binding;
    private RetrofitApi mService;
    private String user_id;
    private Context context;
    private User userData;
    private DisposableSingleObserver<Test> disposableSingleObserver;
    private SharedPreferenceUtil sharedPreferenceUtil;
    public static List<Test> list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTestsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        assert context != null;
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        list = new ArrayList<>();
        assert userData != null;
        user_id = userData.user_id;
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        initTestList();
        return  view;
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
        TestsListAdapter adapter = new TestsListAdapter(list,this);
        binding.rvTestTopic.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(View v, Test data) {
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

    }




}