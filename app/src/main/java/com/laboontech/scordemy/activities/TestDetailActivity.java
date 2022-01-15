package com.laboontech.scordemy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.newTest.OnlineQuizActivity;
import com.laboontech.scordemy.databinding.ActivityTestDetailBinding;
import com.laboontech.scordemy.databinding.ActivityTestsSubListBinding;
import com.laboontech.scordemy.entity.Test;

import java.util.List;

public class TestDetailActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityTestDetailBinding binding;
    public static List<Test> list;
    private String  title,marks, duration, questions, instruction;
    private int test_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
activity = this;
        if(getIntent()!=null){
            test_id = getIntent().getIntExtra("id",0);
            title = getIntent().getStringExtra("Title");
            marks = getIntent().getStringExtra("Marks");
            duration = getIntent().getStringExtra("Duration");
            questions = getIntent().getStringExtra("Questions");
            instruction = getIntent().getStringExtra("Instruction");

            initViews();
        }
    }

    private void initViews(){
        binding.tvTestTitle.setText(title);
        binding.tvMarks.setText(marks+" Marks");
        binding.tvQuestions.setText(questions);
        binding.tvTime.setText(duration + " Mins");


        binding.webView.setBackgroundColor(Color.parseColor("#ffffff"));
        binding.webView.setFocusableInTouchMode(false);
        binding.webView.setFocusable(false);
        binding.webView.getSettings().setDefaultTextEncodingName("UTF-8");

        WebSettings webSettings = binding.webView.getSettings();
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

        binding.webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);


        binding.imgBack.setOnClickListener(v->{
            onBackPressed();
        });

        binding.tvStartTest.setOnClickListener(v->{
            Intent intent = new Intent(activity, OnlineQuizActivity.class);
            intent.putExtra("subtestid", test_id);
            intent.putExtra("testName", title);
            intent.putExtra("marks", marks);
            intent.putExtra("duration", duration);
            startActivity(intent);
        });
    }
}