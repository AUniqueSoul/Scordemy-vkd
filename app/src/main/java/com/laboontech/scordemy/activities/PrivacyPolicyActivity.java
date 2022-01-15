package com.laboontech.scordemy.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.utils.Variables;


public class PrivacyPolicyActivity extends AppCompatActivity {

    private String url;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String work = bundle.getString("work", "privacy_policy");

            String privacy_policy = Variables.DOMAIN+ "privacy_policy.html";
            String refund_policy =  Variables.DOMAIN+"refund_policy.html";
            String term_and_condition =  Variables.DOMAIN+"term_and_condition.html";
            String about_us =  Variables.DOMAIN+"about_us.html";
            String cancellation =  Variables.DOMAIN+"cancellation_policy.html";
            switch (work) {
                case "refund_policy":
                    url = refund_policy;
                    break;
                case "about_us":
                    url = about_us;
                    break;
                case "term_and_condition":
                    url = term_and_condition;
                    break;
                case "cancellation":
                    url = cancellation;
                    break;
                default:
                    url = privacy_policy;
                    break;
            }


        }



        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());
    }
}
