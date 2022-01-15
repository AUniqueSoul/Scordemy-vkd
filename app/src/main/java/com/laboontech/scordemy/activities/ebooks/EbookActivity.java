package com.laboontech.scordemy.activities.ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.ebooks.adapters.EbookAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityEbookBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EbookActivity extends AppCompatActivity {
    private static final String TAG = "EbookActivityTest";
    private Activity activity;
    private ActivityEbookBinding binding;
    private RetrofitApi mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEbookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        mService = RetrofitClient.getAPI();
        binding.heading.setText("Ebooks");
        binding.back.setOnClickListener(v -> onBackPressed());
        mService.getEbooks()
                .enqueue(new Callback<Ebook>() {
                    @Override
                    public void onResponse(@NonNull Call<Ebook> call, @NonNull Response<Ebook> response) {
                        if (response.body() != null){
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                binding.progressBar.setVisibility(View.GONE);
                                if (response.body().res.size()>0){
                                    binding.recyclerview.setAdapter(new EbookAdapter(response.body().res));
                                }else{
                                    Functions.ShowToast(activity,"No Data Found");
                                }
                            }else{
                                Functions.ShowToast(activity, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Ebook> call, Throwable t) {
                        Functions.ShowToast(activity,t.getMessage());
                    }
                });
    }
}