package com.laboontech.scordemy.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.BuildConfig;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentWalletBinding;
import com.laboontech.scordemy.entity.Setting;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletFragment extends Fragment {
    private static final String TAG = "WalletFragmentTest";
    private Context context;
    private FragmentWalletBinding binding;
    private DisposableSingleObserver<User> disposableSingleObserver;
    private RetrofitApi mService;
    private String user_id, inviteCode;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

        ProgressDialog pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("please wait...");
        pd.show();
     disposableSingleObserver =   mService.user_data(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull User user) {
                        Log.d(TAG, "onSuccess: " + user.code);
                        if (user.code.equals(Constants.SUCCESS)){
                            userData.wallet = user.res.get(0).wallet;
                            binding.tvPoints.setText(getText(R.string.rupee)+ " " + userData.wallet + ".00");
                            binding.tvCodeValue.setText(userData.invite_code);

                        }else {
                            Functions.ShowToast(context,""+user.error_msg);
                        }  pd.dismiss();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());pd.dismiss();
                    }
                });

     mService.get_setting()
             .enqueue(new Callback<Setting>() {
                 @Override
                 public void onResponse(Call<Setting> call, Response<Setting> response) {
                     if (response.body() != null) {
                         if (response.body().code.equals(Constants.SUCCESS)){
                             Setting data = response.body().res.get(0);
                             binding.inviteAmount.setText(data.referral_points);
                         }
                     }
                 }

                 @Override
                 public void onFailure(Call<Setting> call, Throwable t) {

                 }
             });


     binding.inviteView.setOnClickListener(view1 -> {
         String text = getString(R.string.invite_start) +
                 " " + userData.invite_code +
                 " " + getString(R.string.invite_end) +
                 " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
         Helper.openShareIntent(context, binding.ivIcon ,text);
     });

     binding.imgCopy.setOnClickListener(v->{
         if (!binding.tvCodeValue.getText().toString().equals("")){
             inviteCode = binding.tvCodeValue.getText().toString();
             Toast.makeText(context, "Invite Code copied successfully!!", Toast.LENGTH_LONG).show();
         }
     });

     binding.redeemBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Functions.showSnackBar(binding.getRoot(),"Coming soon");
         }
     });
        return  view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposableSingleObserver!=null){
            disposableSingleObserver.dispose();
        }
    }
}