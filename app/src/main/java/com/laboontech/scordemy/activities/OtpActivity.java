package com.laboontech.scordemy.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityOtpBinding;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.laboontech.scordemy.utils.Functions.getAppVersion;

public class OtpActivity extends AppCompatActivity {
    private static final String TAG = "OtpActivityTest";
    private ActivityOtpBinding binding;
    private Activity activity;

    // user otp data String
    private String user_number, user_name, referral_code;
    private String country_code;
    private String otp;
    private String entered_referral_code;
    //timer
    private CountDownTimer mCountDownTimer;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private String verificationId, full_number;
    private PhoneAuthProvider.ForceResendingToken token;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    // Disposal
    DisposableSingleObserver<User> disposableSingleObserver;

    //Retrofit api
    RetrofitApi mService;

    public OtpActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        Glide.with(activity).load(R.drawable.scordemy_logo).into(binding.logLogo);
        FirebaseApp.initializeApp(activity);
        mAuth = FirebaseAuth.getInstance();
        mService = RetrofitClient.getAPI();
        initBundleIntentData();
        //animations
        Animations.leftToRight(activity,binding.logLogo);
        Animations.leftToRight(activity,binding.appTitle);
        binding.continuee.setOnClickListener(view1 -> {
            if (isOtpEntered()) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                verifyOtp(credential);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    // Received data from previous activity
    @SuppressLint("SetTextI18n")
    private void initBundleIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_name = bundle.getString("name","");
            user_number = bundle.getString("phone","");
            referral_code = bundle.getString("referral_code","");
            full_number = "+" + "91" + user_number;
            entered_referral_code =  bundle.getString("entered_referral_code","");
            binding.otpSentToTxt.setText(getString(R.string.otp_sent_to_91) + user_number);
            requestOTP(full_number);
            mTimeLeftInMillis = 60000;
            startTimer();
        } else {
            Log.d(TAG, "initBundleIntentData: null data");
        }
    }

    //====================== OTP request has sent=================//
    private void requestOTP(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(activity, "Otp Expired, re-request otp", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                verifyOtp(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    // ============================ Verify Received Otp=================//
    @SuppressLint("SetTextI18n")
    private void verifyOtp(final PhoneAuthCredential phoneAuthCredential) {
        Log.d(TAG, "verifyOtp: " + phoneAuthCredential.getSmsCode());
        mCountDownTimer.cancel();
        binding.enterOtp.setText(phoneAuthCredential.getSmsCode());
        binding.tvDetectingOtpTime.setText("checking otp...");
        binding.tvDetectingOtpTime.setTextColor(Color.WHITE);
        binding.continuee.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                mUser = mAuth.getCurrentUser();

                binding.tvDetectingOtpTime.setText("OTP verified");
                binding.tvDetectingOtpTime.setTextColor(Color.BLACK);
                mCountDownTimer.cancel();

                sign_up_api_call();
            }

        }).addOnFailureListener(e -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.sendCodeAgain.setVisibility(View.VISIBLE);
            binding.tvDetectingOtpTime.setText("failed to detect otp");
            binding.continuee.setVisibility(View.VISIBLE);
            Toast.makeText(activity, "failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openHome() {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    //check if otp box is empty
    private boolean isOtpEntered() {
        try {
            otp = binding.enterOtp.getText().toString();
            if (TextUtils.isEmpty(otp)) {
                Functions.ShowToast(activity, "Otp required");
            } else {
                return true;
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "isOtpEntered: exception" + e.getMessage());
        }

        return false;
    }


    //TIME Text
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                binding.sendCodeAgain.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void updateCountDownText() {
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;

        timeLeftFormatted = String.format(Locale.getDefault(),
                "%02d", seconds);

        binding.tvDetectingOtpTime.setText("Detecting OTP : " + timeLeftFormatted);
    }

    // ============================ API CALLS ======================== //
    private void sign_up_api_call() {
        Log.d(TAG, "sign_up_api_call: user id "+mUser.getUid() + " - " + user_name +" - " + user_number
                + getAppVersion(activity)  + entered_referral_code);
        disposableSingleObserver = mService.sign_up(mUser.getUid(),
                user_name,
                user_number,
                getAppVersion(activity),
                user_number,
                entered_referral_code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onSuccess(User res) {

                        Log.d(TAG, "onSuccess: " + res.code);
                        if (res.code.equals(Constants.SUCCESS)) {

                            try {
                                User user = res.res.get(0);
                                SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(activity);
                                Helper.setLoggedInUser(sharedPreferenceUtil,user);
                            } catch (Exception e) {
                                Log.d(TAG, "onException: " + e.getMessage());
                            }

                            binding.progressBar.setVisibility(View.GONE);
                            binding.continuee.setVisibility(View.VISIBLE);
                            openHome();
                        } else {
                            Functions.ShowToast(activity, res.error_msg);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: usr " + e.getMessage());
                        Functions.ShowToast(activity, e.getMessage());
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableSingleObserver!=null){
            disposableSingleObserver.dispose();
        }

    }


}