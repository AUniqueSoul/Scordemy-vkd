package com.laboontech.scordemy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityEditProfileBinding;
import com.laboontech.scordemy.databinding.ActivityMyStoreBinding;
import com.laboontech.scordemy.entity.CouponCode;
import com.laboontech.scordemy.entity.Subscription;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Objects;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyStoreActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = "MyStoreActivityTest";
    private ActivityMyStoreBinding binding;
    private Activity activity;
    private RetrofitApi mService;
    private String user_id, coupon_id = "",price, finalPrice, discountPrice;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private int content_id;
    private int type;
    private Gson gson;


    private int razorpay_amount = 0;
    private String razorpay_currency = "";
    private String razorpay_theme = "";
    private String razorpay_name = "";
    private String razorpay_description = "";
    private String razorpay_image = "";
    private String razorpay_key_id = "";

    ProgressDialog pd;
    Test src;
    Video srcVideo;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);
        binding = ActivityMyStoreBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());
        gson = new Gson();
        activity = this;
        mService = RetrofitClient.getAPI();
        sharedPreferenceUtil = new SharedPreferenceUtil(activity);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;

        //Bundle Data
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            type = bundle.getInt(Constants.KEY_TYPE);
            if (type == Constants.TYPE_TEST) {
                String test_data = bundle.getString(Constants.KEY_CONTENT_DATA);
                 src = gson.fromJson(test_data, Test.class);
                binding.tvTitle.setText(src.title);
                binding.tvDuration.setText(src.tt_duration + " " + getString(R.string.days));
                binding.tvPrice.setText(getString(R.string.rupee) + " " + src.tt_price);
               price=src.tt_price;
                content_id=src.test_topic_id;
            } else {
                String live_data = bundle.getString(Constants.KEY_CONTENT_DATA);
                 srcVideo = gson.fromJson(live_data, Video.class);
                binding.tvTitle.setText(srcVideo.lc_title);
                binding.tvDuration.setText(srcVideo.duration + " " + getString(R.string.days));
                binding.tvPrice.setText(getString(R.string.rupee) + " " + srcVideo.ic_price);
                price=srcVideo.ic_price;
                content_id=Integer.parseInt(srcVideo.lc_id);
                binding.tvSchedule.setText(srcVideo.lc_schedule_period);
                binding.tvSchedule.setVisibility(View.VISIBLE);

            }
            finalPrice=price;
        }

        getRazorPayDetail();


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

        binding.tvApplyCoupon.setOnClickListener(v->{
            if (binding.etApplyCoupon.getText().toString().trim().equals("")){
                Functions.ShowToast(this, "Enter Coupon code");
            }else {
                applyCouponCode(binding.etApplyCoupon.getText().toString().trim());
            }
        });

    }

    private void applyCouponCode(String couponCode){
        binding.progressBar.setVisibility(View.VISIBLE);
        mService.applyCouponCode(type, couponCode)
                .enqueue(new Callback<CouponCode>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<CouponCode> call, @NonNull Response<CouponCode> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                binding.progressBar.setVisibility(View.GONE);

                                discountPrice =response.body().discount_price;
                                if (Integer.parseInt(discountPrice)>=Integer.parseInt(price)){
                                    Functions.ShowToast(activity,"This Coupon code is not applicable on this item");
                                }else{
                                    int totalAmount = Integer.parseInt(price)- Integer.parseInt(discountPrice);
                                    finalPrice = Integer.toString(totalAmount);
                                    binding.tvCouponApplied.setVisibility(View.VISIBLE);
                                   StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
                                    binding.tvPrice.setText(getString(R.string.rupee)+" "+price +
                                            "  "+getString(R.string.rupee)+" "+finalPrice , TextView.BufferType.SPANNABLE);
                                    Spannable spannable = (Spannable)  binding.tvPrice.getText();
                                    spannable.setSpan(STRIKE_THROUGH_SPAN, 0, price.length()+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                }


                                Log.e("Final Price", finalPrice);
                            } else {
                                Functions.ShowToast(getApplicationContext(), response.body().error_msg);
                                binding.progressBar.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CouponCode> call, @NonNull Throwable t) {
                        Functions.ShowToast(getApplicationContext(), t.getMessage());
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void getRazorPayDetail() {
        mService.getRazorPayDetail("scordemy_pay")
                .enqueue(new Callback<Subscription>() {
                    @Override
                    public void onResponse(@NonNull Call<Subscription> call, @NonNull Response<Subscription> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                Subscription razorPayDetail = response.body().res.get(0);
                                razorpay_currency = razorPayDetail.currency;
                                razorpay_theme = razorPayDetail.theme_color;
                                razorpay_name = razorPayDetail.name;
                                razorpay_description = razorPayDetail.description;
                                razorpay_image = razorPayDetail.image;
                                razorpay_key_id = razorPayDetail.key_id;
                            } else {
                                Functions.ShowToast(activity, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Subscription> call, @NonNull Throwable t) {
                        Functions.ShowToast(activity, t.getMessage());
                    }
                });
    }

    public void startPayment() {
        razorpay_amount=Integer.parseInt(finalPrice);
        Checkout checkout = new Checkout();  /**   * Set your logo here   */
        checkout.setKeyID(razorpay_key_id);    /**   * Instantiate Checkout   */
        checkout.setImage(R.drawable.scordemy_logo);  /**   * Reference to current activity   */
        try {
            JSONObject options = new JSONObject();
            options.put("name", razorpay_name);
            options.put("description", razorpay_description);
            options.put("image", razorpay_image);
            //  options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", razorpay_theme);
            options.put("currency", razorpay_currency);
            options.put("amount", razorpay_amount * 100);//pass amount in currency subunits
            options.put("prefill.email", userData.email);
            options.put("prefill.contact", userData.phone);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.d(TAG, "startPayment: " + e.getMessage());
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.d(TAG, "onPaymentSuccess: " + s);
        pd = new ProgressDialog(activity);
        pd.setTitle("Payment Success");
        pd.setMessage("Unlocking contents please wait...");
        pd.show();
        subscribeToLiveTest();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.d(TAG, "onPaymentError: " + s);
        Functions.ShowToast(activity, "Payment Failed ");
    }

    private void subscribeToLiveTest() {
        mService.subscribeLiveTest(user_id
                , content_id
                , type
                , coupon_id)
                .enqueue(new Callback<Subscription>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<Subscription> call, @NonNull Response<Subscription> response) {
                        if (response.body() != null) {
                            pd.dismiss();
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                //success
                                final Dialog dialog = new Dialog(activity);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                                dialog.setContentView(R.layout.dialog_subscription_success);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.setCancelable(false);
                                dialog.findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(activity,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                TextView title = dialog.findViewById(R.id.tvTitle);
                                if (type==Constants.TYPE_TEST){
                                    title.setText(src.title + " Unlocked For - " + src.tt_duration + " "+getString(R.string.days));
                                }else{
                                    title.setText(srcVideo.lc_title + " Unlocked For - " + srcVideo.duration);
                                }

                                dialog.show();
                            } else {
                                Functions.ShowToast(activity, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Subscription> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(activity, "Failed ", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
    }

}