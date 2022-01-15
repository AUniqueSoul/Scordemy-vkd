package com.laboontech.scordemy.activities.report;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityReportBinding;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.model.ResItem;
import com.laboontech.scordemy.model.savescore.ScoreResponse;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private ActivityReportBinding binding;
    private Dialog internetDialog;
    private String time;
    int Correctcount = 0;
    int CorrectMarks = 0;
    int WrongMarks = 0;
    int wrongecount = 0;
    int skipCount = 0;
    int completeCount = 0;
    double accuracy = 0;
    double complete = 0;
    private RetrofitApi apiClient;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private String user_id;
    private User userData;
    private int testId;
    int Score = 0;
    String json;
    Gson gson;
    String title, marks, quizType;
    int marksPerQuestion;  int givenTestId;
    private static final String TAG = "ReportActivityTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;


        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;

        gson = new Gson();
        apiClient = RetrofitClient.getAPI();


            testId = bundle.getInt("id");

            if (testId==0){
                 givenTestId = bundle.getInt("given_test_id");
                SharedPreferences sharedPreferences = getSharedPreferences("PREF"+givenTestId,MODE_PRIVATE);
                json = sharedPreferences.getString("json"+givenTestId,"");
                time = sharedPreferences.getString("time"+givenTestId,"");
                title = sharedPreferences.getString("title"+givenTestId,"");
                marks = sharedPreferences.getString("marks"+givenTestId,"");
                quizType = sharedPreferences.getString("quizType"+givenTestId,"");
                marksPerQuestion = sharedPreferences.getInt("marksPerQuestion"+givenTestId,0);
                binding.retest.setVisibility(View.VISIBLE);
            }else{
                json = getIntent().getStringExtra("data");
                time = getIntent().getStringExtra("timeMain");
                title = getIntent().getStringExtra("testName");
                marks = getIntent().getStringExtra("marks");
                quizType = getIntent().getStringExtra("type");
                marksPerQuestion = Integer.parseInt(marks);
                binding.retest.setVisibility(View.GONE);
            }





        if (json!=null && json.isEmpty()) {
            Toast.makeText(this, "There is something error", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Type type = new TypeToken<List<ResItem>>() {
            }.getType();
            List<ResItem> restItems = gson.fromJson(json, type);
            binding.time.setText(time);
            binding.totalQuestions.setText("" + restItems.size());
            Correctcount = 0;
            for (int i = 0; i < restItems.size(); i++) {
                if (restItems.get(i).isCorrectAnswerSelected()) {
                    Correctcount = Correctcount + 1;
                    CorrectMarks = CorrectMarks + marksPerQuestion;
                    completeCount = completeCount + 1;
                } else {
                    wrongecount = wrongecount + 1;
                    WrongMarks = WrongMarks + marksPerQuestion;
                    completeCount = completeCount + 1;
                }
                if (restItems.get(i).isSkipQuestion()) {
                    skipCount = skipCount + 1;
                }
            }
            int totalMarks = marksPerQuestion * restItems.size();
            completeCount = completeCount - skipCount;
            wrongecount = wrongecount - skipCount;
            complete = (completeCount * 100) / restItems.size();
            accuracy = (Correctcount * 100) / restItems.size();
            binding.score.setText("" + CorrectMarks);
            Score = Correctcount;
            binding.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            assert quizType != null;
            if (quizType.equalsIgnoreCase("online")) {
                binding.ViewSolution.setVisibility(View.VISIBLE);
                UpdateOnlineScore();
            } else {
                binding.ViewSolution.setVisibility(View.VISIBLE);
            }
            // viewmodel.UpdateTheScore(String.valueOf(Score));
            binding.scoreTotal.setText("" + CorrectMarks + "/" + totalMarks);
            binding.correctTotal.setText("" + Correctcount + "/" + restItems.size());
            binding.skippedTotal.setText("" + skipCount + "/" + restItems.size());
            binding.wrongTotal.setText("" + wrongecount + "/" + restItems.size());
            binding.completePersent.setText("" + complete + " %");
            binding.accuracyPercent.setText("" + accuracy + " %");
            binding.title.setText(title);
        }

        binding.overViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.overviewLay.setVisibility(View.VISIBLE);
                binding.reportLay.setVisibility(View.GONE);
                binding.overViewBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.reportBtn.setTextColor(getResources().getColor(R.color.colorGray));
            }
        });
        binding.reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.overviewLay.setVisibility(View.GONE);
                binding.reportLay.setVisibility(View.VISIBLE);
                binding.overViewBtn.setTextColor(getResources().getColor(R.color.colorGray));
                binding.reportBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        binding.ViewSolution.setOnClickListener(v -> startActivityForResult(new Intent(ReportActivity.this, SubmitActivity.class).putExtra("data", json), 001));

        binding.back.setOnClickListener(view -> {
            finish();
        });

        binding.retest.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("PREF"+givenTestId, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            finish();
        });
    }

    void UpdateOnlineScore() {
        apiClient.SubmitScore(user_id, String.valueOf(testId),
                String.valueOf(completeCount),
                String.valueOf(Score),
                String.valueOf(accuracy),
                String.valueOf(Correctcount),
                String.valueOf(wrongecount),
                String.valueOf(skipCount)).enqueue(new Callback<ScoreResponse>() {
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode().equalsIgnoreCase("200")) {
                    // Toast.makeText(ReportActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    //Store data in shared preference
                    SharedPreferences sharedPreferences = getSharedPreferences("PREF"+testId, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("json" + testId, json);
                    editor.putString("time" + testId, time);
                    editor.putString("title" + testId, title);
                    editor.putString("marks" + testId, marks);
                    editor.putString("quizType" + testId, quizType);
                    editor.putInt("marksPerQuestion" + testId, marksPerQuestion);
                    editor.putInt("testId" + testId, testId);
                    editor.apply();

                } else {
                    Toast.makeText(ReportActivity.this, response.body().getError_msg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable t) {
                Toast.makeText(ReportActivity.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


}