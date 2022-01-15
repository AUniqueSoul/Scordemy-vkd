package com.laboontech.scordemy.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.laboontech.scordemy.adapters.TestsSubListAdapter;
import com.laboontech.scordemy.databinding.ActivityTestsSubListBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.utils.Functions;

import java.util.List;

public class TestsSubListActivity extends AppCompatActivity implements TestsSubListAdapter.OnItemClickListener {
    private Activity activity;
    private ActivityTestsSubListBinding binding;
    public static List<Test> list;
    private String title ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestsSubListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        if (getIntent()!=null){
            title = getIntent().getStringExtra("Title");
            list = (List<Test>) getIntent().getSerializableExtra("test");
            Log.e("TestList", list+"");
            binding.tvTestTitle.setText(title);
            if (list.size() > 0) {

                binding.rvTestTopic.setLayoutManager(new LinearLayoutManager(this));
                TestsSubListAdapter adapter = new TestsSubListAdapter(list,this::onItemClicked);
                binding.rvTestTopic.setAdapter(adapter);

            } else {
                Functions.ShowToast(activity, "No Data Found");
            }

            initViews();
        }


    }
    private void initViews(){
        binding.imgBack.setOnClickListener(v->{
            onBackPressed();
        });
    }

    @Override
    public void onItemClicked(View v, Test data) {

    }
}