package com.laboontech.scordemy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.laboontech.scordemy.adapters.NotificationAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityNotificationBinding;
import com.laboontech.scordemy.entity.Notifications;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {


    private NotificationAdapter adapter;
    private RetrofitApi mService;
    private Activity activity;
    private ActivityNotificationBinding binding;
    private LinearLayoutManager layoutManager;
    private List<Notifications> list;
    private static final String TAG = "NotificationActiTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        mService = RetrofitClient.getAPI();
        list = new ArrayList<>();
        binding.recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new NotificationAdapter(list);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fetchNotifications();
    }

    private void fetchNotifications() {
        mService.notifications()
                .enqueue(new Callback<Notifications>() {
                    @Override
                    public void onResponse(@NonNull Call<Notifications> call, @NonNull Response<Notifications> response) {
                        if (response.body() != null) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                list.clear();
                                list.addAll(response.body().res);
                                adapter.notifyDataSetChanged();

                            } else {
                                Functions.ShowToast(activity, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Notifications> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        binding.progressBar.setVisibility(View.GONE);

                    }
                });
    }
}