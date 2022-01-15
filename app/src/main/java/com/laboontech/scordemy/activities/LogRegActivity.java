package com.laboontech.scordemy.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityLogRegBinding;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.laboontech.scordemy.utils.Functions.getAppVersion;

public class LogRegActivity extends AppCompatActivity {
    private static final String TAG = "LogRegActivityTest";
    private ActivityLogRegBinding binding;
    private Activity activity;
    DisposableSingleObserver<User> disposableSingleObserver;
    private boolean loginForm = true;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private RetrofitApi mService;
    private int RC_SIGN_IN = 7;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogRegBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        activity = this;

        Glide.with(activity).load(R.drawable.scordemy_logo).into(binding.logLogo);

        createLoginForm();
        initActions();

        FirebaseApp.initializeApp(activity);
        mService = RetrofitClient.getAPI();

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.referralCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 10) {
                    validate_invite_code(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void validate_invite_code(CharSequence s) {
        ProgressDialog pd = new ProgressDialog(activity);
        pd.setTitle("Applying Referral Code");
        pd.setMessage("please wait...");
        pd.setCancelable(false);
        pd.show();
        mService.validate_invite_code(s.toString())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                Functions.ShowToast(activity, "Code Applied");
                            } else {
                                Animations.shake(activity, binding.referralCode);
                                Functions.ShowToast(activity, response.body().error_msg);
                            }
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        pd.dismiss();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void createLoginForm() {
        //animations
        Animations.leftToRight(activity, binding.logLogo);
        Animations.leftToRight(activity, binding.appTitle);

        //views visibility
        binding.txtJoinStudyfi.setVisibility(View.GONE);
        binding.name.setVisibility(View.GONE);
        binding.referralCode.setVisibility(View.GONE);
        binding.lytCheckbox.setVisibility(View.GONE);

        //set text
        binding.phone.setHint(getString(R.string.phone_number));
        binding.button.setText(getString(R.string.login));
        binding.bottomTxtLeft.setText(getString(R.string.dont_have_an_ac));
        binding.bottomTxtRight.setText(getString(R.string.sign_up));

    }

    private void createSignUpForm() {
        //animations
        Animations.leftToRight(activity, binding.logLogo);
        Animations.leftToRight(activity, binding.appTitle);

        //views visibility
        binding.txtJoinStudyfi.setVisibility(View.VISIBLE);
        binding.name.setVisibility(View.VISIBLE);
        binding.referralCode.setVisibility(View.GONE);
        binding.lytCheckbox.setVisibility(View.VISIBLE);

        //set text
        binding.phone.setHint(getString(R.string.sign_up_with_mobile));
        binding.button.setText(getString(R.string.join_for_free));
        binding.bottomTxtLeft.setText(getString(R.string.i_have_an_ac));
        binding.bottomTxtRight.setText(getString(R.string.login));

    }

    private void initActions() {
        binding.signInButton.setOnClickListener(v -> {
            binding.signInButton.setVisibility(View.GONE);
            binding.googleProgressBar.setVisibility(View.VISIBLE);
            signIn();
        });
        binding.button.setOnClickListener(view -> {
            String phone = binding.phone.getText().toString();
            String name = binding.name.getText().toString();

            if (loginForm) {
                if (!TextUtils.isEmpty(phone)) {
                    checkUser(phone);
                } else {
                    Functions.ShowToast(activity, "Enter your 10 digit phone number");
                }
            } else {
                //sign up
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(name)) {
                    Intent otpIntent = new Intent(activity, OtpActivity.class);
                    otpIntent.putExtra("phone", phone);
                    otpIntent.putExtra("name", name);
                    if (binding.checkBox.isChecked()) {
                        if (!TextUtils.isEmpty(binding.referralCode.getText().toString().trim())) {
                            otpIntent.putExtra("entered_referral_code", binding.referralCode.getText().toString().trim());
                            startActivity(otpIntent);
                        } else {
                            binding.referralCode.setError("referral code required");
                            binding.referralCode.requestFocus();
                        }
                    } else {
                        otpIntent.putExtra("entered_referral_code", "");
                        startActivity(otpIntent);
                    }

                } else {
                    Functions.ShowToast(activity, "All field required");
                }


            }
        });

        binding.bottomTxtRight.setOnClickListener(view -> {
            if (loginForm) {
                createSignUpForm();
            } else {
                createLoginForm();
            }
            loginForm = !loginForm;
        });


        binding.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                binding.referralCode.setVisibility(View.VISIBLE);
            } else {
                binding.referralCode.setVisibility(View.GONE);

            }
        });

        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 10) {
                    binding.button.setEnabled(true);
                    Functions.hideKeyboard(activity, binding.phone);
                    binding.button.setBackground(getResources().getDrawable(R.drawable.bg_button));

                } else {
                    binding.button.setEnabled(false);
                    binding.button.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void checkUser(String phone) {
        ProgressDialog pd = new ProgressDialog(activity);
        pd.setMessage("please wait...");
        pd.setTitle("Sending Otp");
        pd.show();
        mService.check_user(phone)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.body() != null) {
                            User data = response.body();
                            if (data.code.equals(Constants.SUCCESS)) {
                                Intent otpIntent = new Intent(activity, OtpActivity.class);
                                otpIntent.putExtra("phone", phone);
                                startActivity(otpIntent);
                            } else {
                                Functions.ShowToast(activity, data.error_msg);
                            }
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Functions.ShowToast(activity, t.getMessage());
                        pd.dismiss();

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e);

                binding.signInButton.setVisibility(View.VISIBLE);
                binding.googleProgressBar.setVisibility(View.GONE);
                // ...
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Login", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Toast.makeText(LogRegActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            // HIt Login APi
                            mUser = mAuth.getCurrentUser();
                            sign_up_api_call();
                        } else {
                            Log.w("Login", "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(LogRegActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // ...
    }


    private void sign_up_api_call() {
        disposableSingleObserver = mService.sign_up(mUser.getUid(),
                mUser.getDisplayName(),
                mUser.getPhoneNumber(),
                getAppVersion(activity),
                mUser.getPhoneNumber(),
                "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onSuccess(User res) {

                        if (res.code.equals(Constants.SUCCESS)) {

                            try {
                                User user = res.res.get(0);
                                SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(activity);
                                Helper.setLoggedInUser(sharedPreferenceUtil, user);
                            } catch (Exception e) {
                                Log.d(TAG, "onException: " + e.getMessage());
                            }

                            binding.signInButton.setVisibility(View.VISIBLE);
                            binding.googleProgressBar.setVisibility(View.GONE);
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

    private void openHome() {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}