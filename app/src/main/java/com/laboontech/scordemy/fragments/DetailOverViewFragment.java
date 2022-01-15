package com.laboontech.scordemy.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.LiveContentActivity;
import com.laboontech.scordemy.activities.MyStoreActivity;
import com.laboontech.scordemy.activities.ebooks.adapters.LiveContentAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentDetailOverViewBinding;
import com.laboontech.scordemy.entity.CouponCode;
import com.laboontech.scordemy.entity.LiveContentVideo;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailOverViewFragment extends DialogFragment {
    private static final String TAG = "DetailOverviewFragment";
    private Context context;
    private FragmentDetailOverViewBinding binding;

    private RetrofitApi mService;
    String title , price, schedule, instruction, discountPrice;
    String finalPrice;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailOverViewBinding.inflate(getLayoutInflater(), container, false);
        context = getContext();
        mService = RetrofitClient.getAPI();

        if (getArguments()!=null){
            title = getArguments().getString("Title");
            price = getArguments().getString("Price");
            schedule= getArguments().getString("Scheduled");
            instruction = getArguments().getString("Instructions");
            initViews();
        }


        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void initViews(){
        binding.tvTitle.setText(title);
        binding.tvTotalAmount.setText(context.getString(R.string.rupee)+" "+ price+" including taxes");
        binding.tvTimeDuration.setText(schedule);
        binding.tvTotalTimeDuration.setText(schedule);
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
            dismiss();
        });

        binding.tvClassroom.setOnClickListener(v->{
            dismiss();
        });
        binding.tvBuyNow.setOnClickListener(v->{
            Intent intent = new Intent(context, MyStoreActivity.class);
            intent.putExtra(Constants.KEY_TYPE, Constants.TYPE_LIVE);
            intent.putExtra(Constants.KEY_CONTENT_DATA, ((LiveContentActivity)requireActivity()).str_live_data);
            startActivity(intent);
        });

    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}