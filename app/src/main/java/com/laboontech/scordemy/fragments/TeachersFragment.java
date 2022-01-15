package com.laboontech.scordemy.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.adapters.MentorAdapter;
import com.laboontech.scordemy.adapters.TeachersAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentTeachersBinding;
import com.laboontech.scordemy.entity.Teachers;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class TeachersFragment extends Fragment {
    FragmentTeachersBinding binding;
    Context context;
    private final List<Teachers> teacherList = new ArrayList();
    private final List<Teachers> mentorList = new ArrayList();

    public TeachersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTeachersBinding.inflate(getLayoutInflater());
        context = getContext();
        binding.rvMentors.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        initTeachersData();

        return binding.getRoot();
    }

    private void initTeachersData() {
        RetrofitApi mService = RetrofitClient.getAPI();
        mService.get_teachers()
                .enqueue(new Callback<Teachers>() {
                    @Override
                    public void onResponse(@NonNull Call<Teachers> call, @NonNull Response<Teachers> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {

                                for (int i = 0; i <= response.body().res.size() - 1; i++) {
                                    if (response.body().res.get(i).t_type.equals("1")) {
                                        mentorList.add(response.body().res.get(i));

                                    } else {
                                        teacherList.add(response.body().res.get(i));
                                    }
                                }
                                binding.rvTeachers.setAdapter(new TeachersAdapter(teacherList));
                                binding.rvMentors.setAdapter(new MentorAdapter(mentorList));
                            } else {
                                Functions.ShowToast(context, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Teachers> call, @NonNull Throwable t) {
                        Functions.ShowToast(context, t.getMessage());
                    }
                });
    }
}