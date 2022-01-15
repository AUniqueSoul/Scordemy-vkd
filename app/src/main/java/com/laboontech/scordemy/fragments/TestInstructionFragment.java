package com.laboontech.scordemy.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.HomeActivity;
import com.laboontech.scordemy.activities.newTest.OnlineQuizActivity;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentTestInstructionBinding;
import com.laboontech.scordemy.entity.HomeData;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.model.savescore.ScoreResponse;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestInstructionFragment extends Fragment {
    private static final String TAG = "OnlineQuizActivityTest";
    private Context context;
    private FragmentTestInstructionBinding binding;
    private DisposableSingleObserver<HomeData> disposableSingleObserver;
    private RetrofitApi mService;

    private int test_sub_topic_id;
    String mark,duration;

    public TestInstructionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTestInstructionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        assert context != null;

        initViews();

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFragment.view_type=1;
                ((HomeActivity)requireActivity()).setFragment(new TestFragment(),"test_fragment");
            }
        });
        binding.startBtn.setOnClickListener(view12 -> {
            TestFragment.view_type=1;
            ((HomeActivity)requireActivity()).setFragment(new TestFragment(),"test_fragment");
            Intent intent = new Intent(context, OnlineQuizActivity.class);
            Log.d(TAG, "onCreateView: "+test_sub_topic_id);
            intent.putExtra("subtestid", test_sub_topic_id);
            intent.putExtra("testName", binding.tvTitle.getText().toString());
            intent.putExtra("marks", mark);
            intent.putExtra("duration", duration);
            startActivity(intent);

        });
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {

        Log.d(TAG, "initViews: ");
        if (getArguments() != null) {
            Log.d(TAG, "initViews: view set");
            test_sub_topic_id = getArguments().getInt("test_sub_topic_id");
            String title = getArguments().getString("title");
            String question_count = getArguments().getString("question_count");
             mark = getArguments().getString("mark");
             duration = getArguments().getString("duration");
            String instruction = getArguments().getString("instruction");

            //set views
            binding.tvTitle.setText(title);
            binding.tvQuestions.setText(question_count + " " + context.getString(R.string.questions));
            binding.tvMarks.setText(mark + " " + context.getString(R.string.marks));
            binding.tvDuration.setText(duration + " " + context.getString(R.string.min));



            binding.webInstruction.setBackgroundColor(Color.parseColor("#ffffff"));
            binding.webInstruction.setFocusableInTouchMode(false);
            binding.webInstruction.setFocusable(false);
            binding.webInstruction.getSettings().setDefaultTextEncodingName("UTF-8");

            WebSettings webSettings = binding.webInstruction.getSettings();
            Resources res = getResources();
            int fontSize = res.getInteger(R.integer.font_size);
            webSettings.setDefaultFontSize(fontSize);

            String mimeType = "text/html; charset=UTF-8";
            String encoding = "utf-8";
            String htmlText = instruction;

            String text = "<html><head>"
                    + "<style type=\"text/css\">body{color: #525252;}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";

            binding.webInstruction.loadDataWithBaseURL(null, text, mimeType, encoding, null);


        }


    }



}