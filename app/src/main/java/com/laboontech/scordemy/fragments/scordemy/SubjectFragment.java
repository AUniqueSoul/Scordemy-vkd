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
import com.laboontech.scordemy.databinding.FragmentSubjectBinding;
import com.laboontech.scordemy.entity.Category;
import com.laboontech.scordemy.fragments.scordemy.adapters.CategoryAdapter;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceCategory;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubjectFragment extends Fragment implements InterfaceCategory {
    private static final String TAG = "SubjectFragmentTest";
    private Context context;
    private FragmentSubjectBinding binding;
    private RetrofitApi mService;
    private CategoryAdapter categoryAdapter;


    public SubjectFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubjectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();
        fetchSubjects(MainContentActivity.class_id);
        return view;

    }


    private void fetchSubjects(String clas_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvSubjects.setVisibility(View.GONE);
        mService.getSubjects(clas_id)
                .enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(@NonNull Call<Category> call, @NonNull Response<Category> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                setCategoryAdapter(response.body().res, Constants.TYPE_SUBJECT);
                            } else {
                                Functions.ShowToast(context, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Category> call, @NonNull Throwable t) {
                        Functions.ShowToast(context, t.getMessage());
                    }
                });
    }

    private void setCategoryAdapter(List<Category> list, String type) {
        categoryAdapter = new CategoryAdapter(list, this, type);
        binding.rvSubjects.setAdapter(categoryAdapter);
        binding.progressBar.setVisibility(View.GONE);
        binding.rvSubjects.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCategoryItemClickListener(Category data, String type) {
        if (type.equals(Constants.TYPE_CLASS)){
            //open subject
            MainContentActivity.class_id=data.class_id;
            ((MainContentActivity)requireActivity()).setTitle(data.class_name);
            ((MainContentActivity)requireActivity()).setFragment(new SubjectFragment());

        }else{
            //open chapters
            MainContentActivity.subject_id=data.subject_id;
            ((MainContentActivity)requireActivity()).isSubjectFragment=true;
            ((MainContentActivity)requireActivity()).setTitle(data.subject);
            ((MainContentActivity)requireActivity()).setFragment(new SubCategoryFragment());

        }
    }
}