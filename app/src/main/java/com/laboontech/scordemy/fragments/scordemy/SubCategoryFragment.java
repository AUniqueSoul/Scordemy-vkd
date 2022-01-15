package com.laboontech.scordemy.fragments.scordemy;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentCategoryBinding;
import com.laboontech.scordemy.databinding.FragmentSubCategoryBinding;
import com.laboontech.scordemy.entity.SubCategory;
import com.laboontech.scordemy.fragments.scordemy.adapters.SubCategoryAdapter;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceSubCategory;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubCategoryFragment extends Fragment  {
    private static final String TAG = "CategoryFragmentTest";
    private Context context;
    private FragmentSubCategoryBinding binding;
    private RetrofitApi mService;
    private SubCategoryAdapter adapter;


    public SubCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        fetchChapters();
        return view;
    }

    private void fetchChapters() {
        mService.getChapters(MainContentActivity.subject_id)
                .enqueue(new Callback<SubCategory>() {
                    @Override
                    public void onResponse(@NonNull Call<SubCategory> call, @NonNull Response<SubCategory> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                adapter = new SubCategoryAdapter(response.body().res, new InterfaceSubCategory() {
                                    @Override
                                    public void onChapterClickListener(SubCategory data) {
                                        MainContentActivity.chapter_id=data.chapter_id;
                                        ((MainContentActivity)requireActivity()).setTitle(data.chapter);
                                        ((MainContentActivity)requireActivity()).setFragment(new MainContentFragment());
                                    }
                                });
                                binding.rvChapters.setAdapter(adapter);
                                binding.progressBar.setVisibility(View.GONE);
                            }else{
                                Functions.ShowToast(context, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubCategory> call, @NonNull Throwable t) {
                        Functions.ShowToast(context, t.getMessage());
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }


}