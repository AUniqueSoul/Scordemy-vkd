package com.laboontech.scordemy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.adapters.QuizListAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentPracticeQuizBinding;
import com.laboontech.scordemy.entity.Quiz;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class PracticeQuizListFragment extends Fragment {
    private static final String TAG = "PracticeQuizListTest";
    private Context context;
    private FragmentPracticeQuizBinding binding;
    private DisposableSingleObserver<Quiz> disposableSingleObserver;
    private RetrofitApi mService;
    public PracticeQuizListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPracticeQuizBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        initQuizTitleList();
        return view;
    }

    private void initQuizTitleList() {
    disposableSingleObserver = mService.show_practice_quiz(MainContentActivity.chapter_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<Quiz>() {
                @Override
                public void onSuccess(@io.reactivex.annotations.NonNull Quiz quiz) {
                    Log.d(TAG, "onSuccess: " + quiz.code);
                    if (quiz.code.equals(Constants.SUCCESS)){
                        QuizListAdapter adapter = new QuizListAdapter(context,quiz.res);
                        binding.recyclerView.setAdapter(adapter);
                    }else{
                        Functions.ShowToast(context,""+quiz.error_msg);
                    }
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    Log.d(TAG, "onError: "+e.getMessage());
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