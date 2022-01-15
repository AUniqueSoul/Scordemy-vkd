package com.laboontech.scordemy.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityEditProfileBinding;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.FileUtils;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;

import java.io.File;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "ClassActivityTest";
    private static final int IMAGE_FILE_PICKER = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 2;
    private ActivityEditProfileBinding binding;
    private Activity activity;
    // Disposal
    DisposableSingleObserver<User> disposableSingleObserver;

    //Retrofit api
    RetrofitApi mService;

    private String profile_pic;
    private String user_id;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //above kitkat version : make full screen
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.profile));
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        mService = RetrofitClient.getAPI();
        sharedPreferenceUtil = new SharedPreferenceUtil(activity);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        binding.dpFrame.setOnClickListener(view1 -> openImageChooser());
        binding.saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();
            }
        });
        pd = new ProgressDialog(activity);
        initProfileData();

    }

    private void initProfileData() {
        try {

            if (userData.profile_pic!=null){
                binding.progressBar.setVisibility(View.VISIBLE);
            }
            Glide.with(activity).load(Variables.IMAGE_PATH + userData.profile_pic).placeholder(R.drawable.user).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    binding.progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    binding.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(binding.profilePic);
            binding.name.setText(userData.name);
            binding.phoneNumber.setText(userData.phone);
            if (TextUtils.isEmpty(userData.phone)){
                binding.phoneNumber.setEnabled(true);
            }
            binding.email.setText(userData.email);
            binding.guardianPhone.setText(userData.guardian_phone);
            binding.city.setText(userData.city);

            if (userData.gender.equals("Male")) {
                binding.genderSpinner.setSelection(1);
            } else if (userData.gender.equals("Female")) {
                binding.genderSpinner.setSelection(2);
            } else {
                binding.genderSpinner.setSelection(0);
            }


            if (userData.gender.equals("Class V")) {
                binding.genderSpinner.setSelection(1);
            } else if (userData.gender.equals("Class VI")) {
                binding.genderSpinner.setSelection(2);
            }else if (userData.gender.equals("Class VII")) {
                binding.genderSpinner.setSelection(3);
            }else if (userData.gender.equals("Class VIII")) {
                binding.genderSpinner.setSelection(4);
            }else if (userData.gender.equals("Class IX")) {
                binding.genderSpinner.setSelection(5);
            }else if (userData.gender.equals("Class X")) {
                binding.genderSpinner.setSelection(6);
            }else if (userData.gender.equals("Class XI")) {
                binding.genderSpinner.setSelection(7);
            }else if (userData.gender.equals("Class XII")) {
                binding.genderSpinner.setSelection(8);
            } else
                {
                binding.genderSpinner.setSelection(0);
            }


        } catch (NullPointerException e) {
            Log.d(TAG, "initProfileData: " + e.getMessage());
        }


    }


    private void updateUserDp() {
        MultipartBody.Part user_dp = null;
        RequestBody userid = null;
        if (profile_pic != null) {
            user_dp = Functions.prepareFilePart(activity, "file", profile_pic);
            userid = Functions.createPartFromString(user_id);
        } else {
            Functions.ShowToast(activity, "no file selected");
        }
        disposableSingleObserver = mService.update_user_dp(user_dp, userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user.code.equals(Constants.SUCCESS)) {
                            Functions.ShowToast(activity, "Profile pic updated");
                            if (user.res != null) {
                                Helper.saveProfileMe(sharedPreferenceUtil, user.res.get(0));
                            }
                        } else {
                            Functions.ShowToast(activity, "" + user.error_msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
    }

    private void updateUserData() {
        pd.setCancelable(false);
        pd.setTitle("Updating Profile");
        pd.setMessage("please wait...");
        pd.show();

        disposableSingleObserver = mService.update_user_data(user_id, binding.name.getText().toString(), binding.email.getText().toString(),binding.phoneNumber.getText().toString(), binding.guardianPhone.getText().toString(),
                binding.genderSpinner.getSelectedItem().toString(), binding.city.getText().toString(), binding.classSpinner.getSelectedItem().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user.code.equals(Constants.SUCCESS)) {
                            Log.d(TAG, "onSuccess: profile data updated");

                            if (user.res != null) {
                                Helper.saveProfileMe(sharedPreferenceUtil, user.res.get(0));
                            }   Functions.ShowToast(activity, "Profile updated successfully");
                        } else {
                            Functions.ShowToast(activity, "" + user.error_msg);
                        }
                        pd.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        pd.dismiss();
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });

    }


    //On permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openImageChooser();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_FILE_PICKER && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // MEDIA GALLERY
            profile_pic = FileUtils.getPath(activity, selectedImageUri);

            try {
                assert profile_pic != null;
                Uri uri = Uri.fromFile(new File(profile_pic));
                Glide.with(this).load(uri).into(binding.profilePic);
                updateUserDp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openImageChooser() {
        if (FileUtils.doesUserHavePermission(activity)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), IMAGE_FILE_PICKER);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableSingleObserver!=null){
            disposableSingleObserver.dispose();
        }

    }
}