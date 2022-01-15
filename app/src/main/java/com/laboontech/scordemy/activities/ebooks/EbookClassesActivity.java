package com.laboontech.scordemy.activities.ebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.ebooks.adapters.EbookAdapter;
import com.laboontech.scordemy.activities.ebooks.adapters.EbookClassAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityEbookBinding;
import com.laboontech.scordemy.databinding.ActivityEbookClassesBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EbookClassesActivity extends AppCompatActivity {
    private static final String TAG = "EbookClassesActiyTest";
    private Activity activity;
    private ActivityEbookClassesBinding binding;
    private RetrofitApi mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEbookClassesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        mService= RetrofitClient.getAPI();
        Log.d(TAG, "onCreate: Ebook");
        binding.back.setOnClickListener(v -> onBackPressed());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            binding.heading.setText(bundle.getString("heading"));
            Log.d(TAG, "onCreate: "+bundle.getString("prefix")+bundle.getString("id"));
            mService.getEbookClasses(bundle.getString("prefix")+bundle.getString("id"))
                    .enqueue(new Callback<Ebook>() {
                        @Override
                        public void onResponse(@NonNull Call<Ebook> call, @NonNull Response<Ebook> response) {
                            if (response.body() != null){
                                if (response.body().code.equals(Constants.SUCCESS)) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    if (response.body().res.size()>0){
                                        binding.recyclerview.setAdapter(new EbookClassAdapter(response.body().res));
                                    }else{
                                        Functions.ShowToast(activity,"No Data Found");
                                    }
                                }else{
                                    Functions.ShowToast(activity, response.body().error_msg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Ebook> call, @NonNull Throwable t) {
                            Functions.ShowToast(activity,t.getMessage());
                        }
                    });
        }

    }
}