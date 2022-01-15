package com.laboontech.scordemy.activities.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laboontech.scordemy.activities.report.adapter.ViewSolutionAdapter;
import com.laboontech.scordemy.databinding.ActivitySubmitBinding;
import com.laboontech.scordemy.model.ResItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubmitActivity extends AppCompatActivity {
    private static SubmitActivity instance;
    private ActivitySubmitBinding binding;
    private ViewSolutionAdapter adapter;
    private static List<ResItem> finalAnswerSheet=new ArrayList<>();


    @NonNull
    @Override
    public String toString() {
        return SubmitActivity.class.getName();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySubmitBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Gson gson = new Gson();
        String json = getIntent().getStringExtra("data");
        if (json.isEmpty()) {
            Toast.makeText(this, "There is something error", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Type type = new TypeToken<List<ResItem>>() {
            }.getType();
            List<ResItem> restItems = gson.fromJson(json, type);
            adapter = new ViewSolutionAdapter(this,restItems );
            binding.submitListRecycler.setAdapter(adapter);
        }
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}