package com.laboontech.scordemy.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laboontech.scordemy.adapters.HomeDataAdapter;
import com.laboontech.scordemy.adapters.TeachersAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentHomeBinding;
import com.laboontech.scordemy.entity.HomeData;
import com.laboontech.scordemy.entity.Teachers;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragmentTest";
    private Context context;
    private FragmentHomeBinding binding;
    private DisposableSingleObserver<HomeData> disposableSingleObserver;
    private RetrofitApi mService;
    private HomeDataAdapter adapter;
    private List<HomeData> list;


    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        list = new ArrayList<>();
        context = getContext();
        assert context != null;
        Variables.sharedPreferences = context.getSharedPreferences(Variables.my_shared_pref,Context.MODE_PRIVATE);

        binding.txtUserName.setText("Hi !, "+ Variables.sharedPreferences.getString(Variables.name,"Welcome"));
        Animations.bottomToTop(context,binding.txtUserName);
        Animations.bottomToTop(context,binding.txtToday);
//        Animations.shake(context,binding.cvResume);
        initHomeData();

        return view;
    }

    private void initHomeData() {
    disposableSingleObserver = mService.home_data()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<HomeData>() {
                @Override
                public void onSuccess(HomeData homeData) {
                    if (homeData.code.equals(Constants.SUCCESS)){
                        list.clear();
                        list = homeData.res;
                        adapter = new HomeDataAdapter(context,list);
                        binding.recyclerView.setHasFixedSize(true);
                        binding.recyclerView.setLayoutManager(new GridLayoutManager(context,2));
                        binding.recyclerView.setAdapter(adapter);
                        Log.d(TAG, "onSuccess: data has been set into recycler view");
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "onError: exception " + e.getMessage());
                }
            });
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposableSingleObserver!=null){
            disposableSingleObserver.dispose();
        }

    }
}