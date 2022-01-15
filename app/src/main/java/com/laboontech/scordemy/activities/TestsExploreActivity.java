package com.laboontech.scordemy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityTestsExploreBinding;
import com.laboontech.scordemy.databinding.ActivityTestsSubListBinding;
import com.laboontech.scordemy.entity.CouponCode;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestsExploreActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityTestsExploreBinding binding;
    private RetrofitApi mService;
    private String title, description, image, price;
    public static List<Test> list;
    Test testData;
    Gson gson;
    String test_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestsExploreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        setContentView(view);
        activity = this;
        gson = new Gson();
        if (getIntent()!=null){
             test_data =  getIntent().getStringExtra(Constants.KEY_CONTENT_DATA);
            testData = gson.fromJson(test_data,Test.class);
            title=testData.title;
            description=testData.description;
            image=testData.tt_image;
            price=testData.tt_price;
            list = (List<Test>) getIntent().getSerializableExtra("test");
            initViews();
        }
    }

    private void initViews(){
        binding.tvTitle.setText(title);
        binding.tvTestTitle.setText(title);
        binding.tvPrice.setText("₹ "+price +"/-");
        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.bg_curve)
                .into(binding.imgTest);

        binding.webView.setFocusableInTouchMode(false);
        binding.webView.setFocusable(false);
        binding.webView.getSettings().setDefaultTextEncodingName("UTF-8");

        WebSettings webSettings = binding.webView.getSettings();
        Resources res = getResources();
        int fontSize = res.getInteger(R.integer.font_size);
        webSettings.setDefaultFontSize(fontSize);

        String mimeType = "text/html; charset=UTF-8";
        String encoding = "utf-8";
        String htmlText = description;

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



        binding.tvExplore.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TestsSubListActivity.class);
            intent.putExtra("Title", title);
            intent.putExtra("test", (Serializable) list);
            startActivity(intent);
        });

        binding.tvBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MyStoreActivity.class);
                intent.putExtra(Constants.KEY_TYPE, Constants.TYPE_TEST);
                intent.putExtra(Constants.KEY_CONTENT_DATA, test_data);
                startActivity(intent);
            }
        });
    }



}