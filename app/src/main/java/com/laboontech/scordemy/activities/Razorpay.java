package com.laboontech.scordemy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.laboontech.scordemy.adapters.SubscriptionPlansAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.entity.Subscription;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceSubscriptionPlan;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.databinding.ActivityRazorpayBinding;
import com.laboontech.scordemy.utils.Functions;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Razorpay extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = "RazorpayTest";
    public static String EXTRA_SUBSCRIPTION_STATUS = "subscription_status";
    public static String EXTRA_SUBSCRIPTION_HOME_TITLE = "home_title";
    public static String EXTRA_SUBSCRIPTION_CONTENT_NAME= "content_name";



    Activity activity;
    ActivityRazorpayBinding binding;

    private String home_id, title, subscription_plan_id;
    private RetrofitApi mService;
    private int razorpay_amount = 0;
    private String razorpay_currency = "";
    private String razorpay_theme = "";
    private String razorpay_name = "";
    private String razorpay_description = "";
    private String razorpay_image = "";
    private SharedPreferenceUtil sharedPreferenceUtil;
    private User userData;
    private String razorpay_key_id="";
    private String content_name, content_id, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRazorpayBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());
        sharedPreferenceUtil = new SharedPreferenceUtil(activity);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);

        initViews();
        initActions();
        getRazorPayDetail();


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
                                razorpay_key_id=razorPayDetail.key_id;
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

    @SuppressLint("SetTextI18n")
    private void initViews() {
        mService = RetrofitClient.getAPI();
        Variables.sharedPreferences = getSharedPreferences(Variables.my_shared_pref, MODE_PRIVATE);
        home_id = Variables.sharedPreferences.getString("home_id", "");
        title = Variables.sharedPreferences.getString(Constants.BACK_TITLE, "");
        content_id = Variables.sharedPreferences.getString("content_id", "");
        content_name = Variables.sharedPreferences.getString("content_name", "");
        type = Variables.sharedPreferences.getString("type", "");
        binding.tvTitle.setText(title + " - "+content_name);
        fetchSubscriptionPlans();
    }

    private void fetchSubscriptionPlans() {
        Log.d(TAG, "fetchSubscriptionPlans: "+content_id + " type - "+type);
        mService.getSubscriptionPlans(content_id,type)
                .enqueue(new Callback<Subscription>() {
                    @Override
                    public void onResponse(@NonNull Call<Subscription> call, @NonNull Response<Subscription> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                if (response.body().res.size()>0){
                                    SubscriptionPlansAdapter adapter = new SubscriptionPlansAdapter(response.body().res, new InterfaceSubscriptionPlan() {
                                        @Override
                                        public void onPlanSelectedListener(Subscription data) {
                                            subscription_plan_id = data.subs_id;
                                            razorpay_amount=Integer.parseInt(data.plan_price);
                                        }
                                    });
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.rvPlans.setAdapter(adapter);
                                }else{
                                    Functions.ShowToast(activity,"No Data Found");
//                                    onBackPressed();
                                }

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

    private void initActions() {
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.confirm.setOnClickListener(v -> {
            if (subscription_plan_id!=null){
                startPayment();
            }else{
                Snackbar.make(binding.getRoot(), "Please Select Subscription Plan", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    public void startPayment() {
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
            options.put("prefill.contact",userData.phone);
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
        Log.d(TAG, "onPaymentSuccess: "+s);
        ProgressDialog pd = new ProgressDialog(activity);
        pd.setTitle("Payment Success");
        pd.setMessage("Unlocking contents please wait...");
        pd.show();
        mService.createSubscriptionUser(userData.user_id,subscription_plan_id,content_id,home_id)
                .enqueue(new Callback<Subscription>() {
                    @Override
                    public void onResponse(@NonNull Call<Subscription> call, @NonNull Response<Subscription> response) {
                        if (response.body() != null) {
                            pd.dismiss();
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                closeWithSuccessResult();
                            }else{
                                Functions.ShowToast(activity, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Subscription> call, @NonNull Throwable t) {
                        Functions.ShowToast(activity, t.getMessage());
                        pd.dismiss();
                    }
                });
    }

    private void closeWithSuccessResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_SUBSCRIPTION_STATUS, "success");
        data.putExtra(EXTRA_SUBSCRIPTION_CONTENT_NAME, content_name);
        data.putExtra(EXTRA_SUBSCRIPTION_HOME_TITLE, title);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public void onPaymentError(int i, String s) {
        Log.d(TAG, "onPaymentError: "+s);
        Functions.ShowToast(activity, "Payment Failed ");
    }


}