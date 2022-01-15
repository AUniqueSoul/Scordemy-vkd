package com.laboontech.scordemy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.adapters.QuizAdapter;
import com.laboontech.scordemy.databinding.ActivityPracticeQuizBinding;
import com.laboontech.scordemy.interfaces.IQuizListener;
import com.laboontech.scordemy.utils.Variables;

public class PracticeQuizActivity extends AppCompatActivity implements IQuizListener {
    private static final String TAG = "PracticeQuyTest";
    private ActivityPracticeQuizBinding binding;
    private Activity activity;
    private QuizAdapter adapter;


    //summary detail
    private int total_attempted = 0;
    private int correct_answer = 0;
    private int incorrect_answer = 0;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPracticeQuizBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            binding.backTitle.setText(bundle.getString("title"));
        }

        if (Variables.quizQuestionsList != null) {
            //send quiz data to quiz questions adapter
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(activity));
            adapter = new QuizAdapter(activity, Variables.quizQuestionsList, this);
            binding.recyclerview.setAdapter(adapter);
            binding.progressBar.setVisibility(View.GONE);

            binding.ivQuestions.setImageDrawable(getDrawable(R.drawable.ic_select_question));
            binding.tvQues.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.rlQue.setOnClickListener(view1 -> {
                binding.ivQuestions.setImageDrawable(getDrawable(R.drawable.ic_select_question));
                binding.tvQues.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.ivSummary.setImageDrawable(getDrawable(R.drawable.ic_unselect_summary));
                binding.tvSummary.setTextColor(Color.parseColor("#000000"));
                binding.mlSummary.setVisibility(View.GONE);
                binding.recyclerview.setVisibility(View.VISIBLE);
                binding.btnResetResponse.setVisibility(View.GONE);
            });
            binding.rlSummary.setOnClickListener(view12 -> {
                binding.ivSummary.setImageDrawable(getDrawable(R.drawable.ic_select_summary));
                binding.tvSummary.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.ivQuestions.setImageDrawable(getDrawable(R.drawable.ic_unselect_question));
                binding.tvQues.setTextColor(Color.parseColor("#000000"));
                binding.mlSummary.setVisibility(View.VISIBLE);
                binding.recyclerview.setVisibility(View.GONE);
//                binding.btnResetResponse.setVisibility(View.VISIBLE);

                //set summary data
                binding.tvTotalQue.setText(String.valueOf(Variables.quizQuestionsList.size()));
                binding.tvAttempted.setText(String.valueOf(total_attempted));
                binding.tvCorrectAnswer.setText(String.valueOf(correct_answer));
                binding.tvInCorrectAnswer.setText(String.valueOf(incorrect_answer));
            });

            binding.ivBack.setOnClickListener(view13 -> onBackPressed());
        }

    }




    /**
     * Quiz listener methods
     */
    @Override
    public void onAnswerAttempted() {
        total_attempted++;
    }

    @Override
    public void onCorrectAnswer() {
        correct_answer++;
    }

    @Override
    public void onInCorrectAnswer() {
        incorrect_answer++;
    }
}