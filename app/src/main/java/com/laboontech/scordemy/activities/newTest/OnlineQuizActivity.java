package com.laboontech.scordemy.activities.newTest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.newTest.adapter.OnlineQuizAdapter;
import com.laboontech.scordemy.activities.report.ReportActivity;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityOnlineQuizBinding;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.model.QuizResponse;
import com.laboontech.scordemy.model.ResItem;
import com.laboontech.scordemy.model.savescore.ScoreResponse;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineQuizActivity extends AppCompatActivity implements OnlineQuizAdapter.NoQuestionSelected {
    private OnlineQuizAdapter adapter;
    private ActivityOnlineQuizBinding binding;
    private static long QUESTION_TOTAL_TIME = 30000;
    private static long TIMER_VARIATION = 1000;
    private long saveTime = 0;
    private LinearLayoutManager manager;
    private CountDownTimer timer;
    Handler dialogHandler = new Handler();
    private Runnable dialogRunnable;
    private SnapHelper postionHelper, quizHelper;
    private boolean isSubmited = false;
    Snackbar snackBar;
    private RetrofitApi apiClient;
    private String testName, duration, marks, marksPerQuestion;
    private int testId;
    private static final String TAG = "OnlineQuizActivityTest";
    private RetrofitApi mService;
    private Activity activity;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private User userData;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnlineQuizBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mService = RetrofitClient.getAPI();
        activity = this;
        sharedPreferenceUtil = new SharedPreferenceUtil(activity);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            testId = bundle.getInt("subtestid");
            testName = bundle.getString("testName");
            duration = bundle.getString("duration");
            marks = bundle.getString("marks");
        }

        try {
            if (duration != null && !duration.isEmpty()) {
                QUESTION_TOTAL_TIME = Long.parseLong(duration) * 1000 * 60;
            }
        } catch (Exception e) {

        }

        binding.tvTitle.setText(testName);
        apiClient = RetrofitClient.getAPI();
        adapter = new OnlineQuizAdapter(this, this);

        CallQuizApi(String.valueOf(testId));


        manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.questionsList.setLayoutManager(manager);
        quizHelper = new PagerSnapHelper();
        quizHelper.attachToRecyclerView(binding.questionsList);
        binding.time.setVisibility(View.VISIBLE);
        binding.questionsList.setAdapter(adapter);
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateNextButton();
            }
        });
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePreviousButton();
            }
        });
        timer = new CountDownTimer(QUESTION_TOTAL_TIME, TIMER_VARIATION) {

            public void onTick(long millisUntilFinished) {
                saveTime = QUESTION_TOTAL_TIME - TIMER_VARIATION;
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                binding.time.setText(v + ":" + String.format("%02d", va));
            }

            public void onFinish() {
                askToSubmitTest();
            }
        };

        binding.submit.setOnClickListener(v -> {
            askToSubmitTest();
        });


    }

    private void CallQuizApi(String testId) {
        apiClient.getQuiz(testId).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getRes() != null && response.body().getRes().size() > 0) {
                    marksPerQuestion = String.valueOf(Long.parseLong(marks) / response.body().getRes().size());
                    adapter.UpdateList(response.body().getRes());
                    timer.start();

                } else {
                    Toast.makeText(OnlineQuizActivity.this, "Quiz Not Found", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {

            }
        });
    }

    private void askToSubmitTest() {
        new MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_App_Button)
                .setCancelable(false)
                .setTitle("Confirm Submission")
                .setMessage("do you want submit the test?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            if (isSubmited) {
                                showCustomAlert("Test Already Submitted!!");
                            } else {
                                timer.cancel();
                                isSubmited = true;
                                adapter.UpdateSubmit(true);
                            }
                        } catch (Exception e) {

                        }
                    }
                }).setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
            finish();
        }).show();

    }

    @Override
    public void onBackPressed() {
        try {
            dialogRunnable = new Runnable() {
                @Override
                public void run() {
                    showCustomAlert("Do You want to cancel Your Test", " Yes", true, new RetrySnackBarClickListener() {
                        @Override
                        public void onClickRetry() {
                            timer.cancel();
                            finish();
                        }
                    });
                }
            };
            dialogHandler.postDelayed(dialogRunnable, 500);
        } catch (Exception e) {

        }
    }

    private void showCustomAlert(String s, String button, boolean isRetryOptionAvailable, RetrySnackBarClickListener retrySnackBarClickListener) {
        if (isRetryOptionAvailable) {
            snackBar = Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_LONG).setAction(button, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retrySnackBarClickListener.onClickRetry();
                }
            });
        } else {
            snackBar = Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_LONG);
        }

        snackBar.setActionTextColor(Color.WHITE);
        View snackBarView = snackBar.getView();
        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBar.show();
    }


    @Override
    public void NoQuestionSelected() {
        try {
            showCustomAlert("Sorry!! You have Not Selected Any Question");
            dialogRunnable = new Runnable() {
                @Override
                public void run() {
                    //  showCustomAlert("Sorry!! You have Not Selected Any Question",binding.getRoot());
                    finish();
                }
            };
            dialogHandler.postDelayed(dialogRunnable, 2000);

        } catch (Exception e) {

        }
    }


    @Override
    public void getResult(List<ResItem> resItems) {
        showCustomAlert("Test Submited Succesfully!!");
        Gson gson = new Gson();
        String json = gson.toJson(resItems);
        String timerNew = String.valueOf((saveTime) / 1000);
        startActivity(new Intent(OnlineQuizActivity.this, ReportActivity.class)
                .putExtra("data", json)
                .putExtra("timeMain", timerNew)
                .putExtra("testName", testName)
                .putExtra("type", "online")
                .putExtra("id", testId)
                .putExtra("marks", marksPerQuestion)
        );
        finish();
    }


    public void UpdatePreviousButton() {
        int firstVisible = manager.findFirstVisibleItemPosition() - 1;
        int lastVisible = manager.findLastVisibleItemPosition() + 1;

        if (firstVisible <= adapter.getItemCount()) {
            manager.scrollToPosition(firstVisible);
        }
    }


    public void UpdateNextButton() {
        int firstVisible = manager.findFirstVisibleItemPosition() - 1;
        int lastVisible = manager.findLastVisibleItemPosition() + 1;

        if (lastVisible <= adapter.getItemCount()) {
            manager.scrollToPosition(lastVisible);
        }
        if (lastVisible == adapter.getItemCount()) {
            showCustomAlert("No More Question Available !! Please Submit this Test ");
        }
    }

    private void showCustomAlert(String s) {
        snackBar = Snackbar.make(binding.getRoot(), s, Snackbar.LENGTH_LONG);
        snackBar.setActionTextColor(Color.BLUE);
        View snackBarView = snackBar.getView();
        TextView textView = snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBar.show();
    }


    // Check if this test already solved or not
    private void getMyScore() {
        mService.get_my_score(user_id, testId)
                .enqueue(new Callback<ScoreResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ScoreResponse> call, @NonNull Response<ScoreResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getCode().equals(Constants.SUCCESS) && response.body().getRes().size() > 0) {
                                //go to report activity
                                timer.cancel();
                                ScoreResponse data = response.body().getRes().get(0);
                                Intent intent = new Intent(activity, ReportActivity.class);
                                intent.putExtra("id", testId);
                                intent.putExtra("title", testName);
                                intent.putExtra("already_submitted", true);
                                intent.putExtra("accuracy", data.getAccuracy());
                                intent.putExtra("completed", data.getCompleted());
                                intent.putExtra("correct", data.getCorrect());
                                intent.putExtra("score", data.getScore());
                                intent.putExtra("wrong", data.getWrong());
                                intent.putExtra("skipped", data.getSkipped());
                                startActivity(intent);
                                finish();
                            } else {
                                CallQuizApi(String.valueOf(testId));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ScoreResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        CallQuizApi(String.valueOf(testId));
                    }
                });
    }


}