package com.laboontech.scordemy.fragments.scordemy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentCategoryBinding;
import com.laboontech.scordemy.entity.Category;
import com.laboontech.scordemy.entity.Medium;
import com.laboontech.scordemy.fragments.scordemy.adapters.CategoryAdapter;
import com.laboontech.scordemy.fragments.scordemy.adapters.MediumAdapter;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceCategory;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceMedium;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class CategoryFragment extends Fragment implements InterfaceMedium, InterfaceCategory {
    private static final String TAG = "CategoryFragmentTest";
    private Context context;
    private FragmentCategoryBinding binding;
    private RetrofitApi mService;

    private List<Medium> medium_list;
    private Medium medium_selected;

    private MediumAdapter mediumAdapter;
    private CategoryAdapter categoryAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();

//        setMediumAdapter();


        fetchMediums();

        return view;
    }

    //    private void setMediumAdapter() {
//        medium_list=new ArrayList<>();
//        mediumAdapter = new MediumAdapter(medium_list,this);
//        binding.rvMediums.setAdapter(mediumAdapter);
//    }
    private void setCategoryAdapter(List<Category> list, String type) {
        categoryAdapter = new CategoryAdapter(list, this, type);
        binding.rvCategory.setAdapter(categoryAdapter);
        binding.progressBar.setVisibility(View.GONE);
        binding.rvCategory.setVisibility(View.VISIBLE);
        binding.lytNoDataFound.setVisibility(View.GONE);
    }

    private void fetchMediums() {
        mService.getMedium(MainContentActivity.home_id)
                .enqueue(new Callback<Medium>() {
                    @Override
                    public void onResponse(@NonNull Call<Medium> call, @NonNull Response<Medium> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {

//                                medium_list.addAll(response.body().res);
//                                mediumAdapter.notifyDataSetChanged();
                                binding.progressBar.setVisibility(View.GONE);

                                ArrayList<Medium> arrayList = new ArrayList<>();
                                for (Medium catData : response.body().res) {
                                    arrayList.add(new Medium(catData.medium_id, catData.medium));
                                }

                                ArrayAdapter<Medium> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayList);
                                binding.atMedium.setAdapter(adapter);
                                binding.atMedium.setOnItemClickListener((AdapterView.OnItemClickListener) (parent, view, position, id) -> {
                                    Functions.hideKeyboard(getActivity(),binding.atMedium);
                                    medium_selected = (Medium) adapter.getItem(position);
                                    fetchCLass(medium_selected.getMedium_id());
                                });

                            } else {
                                Functions.ShowToast(context, response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Medium> call, @NonNull Throwable t) {
                        Functions.ShowToast(context, t.getMessage());
                    }
                });
    }


    private void fetchCLass(String medium_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvCategory.setVisibility(View.GONE);
        mService.getClass(medium_id)
                .enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(@NonNull Call<Category> call, @NonNull Response<Category> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS) && response.body().res.size()>0) {
                                setCategoryAdapter(response.body().res, Constants.TYPE_CLASS);
                            } else {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.rvCategory.setVisibility(View.GONE);
                                binding.lytNoDataFound.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Category> call, @NonNull Throwable t) {
                        binding.rvCategory.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.lytNoDataFound.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void fetchSubjects(String class_id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvCategory.setVisibility(View.GONE);
        mService.getSubjects(class_id)
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


    //Listener Methods
    @SuppressLint("CommitPrefEdits")
    @Override
    public void onItemClickListener(Medium data) {
        fetchCLass(data.medium_id);
     /*   //Some home data have classes some not
        if (MainContentActivity.home_id.equals(Constants.SEBA) || MainContentActivity.home_id.equals(Constants.NEET)
                || MainContentActivity.home_id.equals(Constants.CBSE) || MainContentActivity.home_id.equals(Constants.HS)
                || MainContentActivity.home_id.equals(Constants.ASSAM_JATIYA_BIDYALAY) || MainContentActivity.home_id.equals(Constants.SHANKAR_VIDYANIKETAN)
                || MainContentActivity.home_id.equals(Constants.JEE)) {
            fetchCLass(data.medium_id);
        } else {
            //Type predefined in table_subject
            fetchSubjects(data.getMedium_id());

            //store medium_id and medium name as content_id and content_name for subscription
            Variables.sharedPreferences = context.getSharedPreferences(Variables.my_shared_pref, MODE_PRIVATE);
            Variables.editor = Variables.sharedPreferences.edit();
            Variables.editor.putString("content_id", data.medium_id);
            Variables.editor.putString("content_name", data.medium);
            Variables.editor.putString("type", "Medium");
            Variables.editor.apply();

            //Check if user have subscription plan active or not for this content id
            ((MainContentActivity) requireActivity()).checkUserSubscriptionStatus(data.medium_id);
        }*/

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCategoryItemClickListener(Category data, String type) {
        if (type.equals(Constants.TYPE_CLASS)) {
            //open subject
            MainContentActivity.class_id = data.class_id;
            ((MainContentActivity) requireActivity()).setTitle(data.class_name);
            ((MainContentActivity) requireActivity()).setFragment(new SubjectFragment());

            //Check if user have subscription plan active or not for this content id
            ((MainContentActivity) requireActivity()).checkUserSubscriptionStatus(data.class_id);

            //store class id and class name for content_id and content_name for subscription
            Variables.sharedPreferences = context.getSharedPreferences(Variables.my_shared_pref, MODE_PRIVATE);
            Variables.editor = Variables.sharedPreferences.edit();
            Variables.editor.putString("content_id", data.class_id);
            Variables.editor.putString("content_name", data.class_name);
            Variables.editor.putString("type", "Class");
            Variables.editor.apply();

        } else {
            //open chapters
            MainContentActivity.subject_id = data.subject_id;
            ((MainContentActivity) requireActivity()).setTitle(data.subject);
            ((MainContentActivity) requireActivity()).setFragment(new SubCategoryFragment());

            if (MainContentActivity.home_id.equals(Constants.COMPUTER)) {
                //Check if user have subscription plan active or not for this content id
                ((MainContentActivity) requireActivity()).checkUserSubscriptionStatus(data.subject_id);
                //for computer subject will be locked for subscription content
                //if user is viewing computer then store subject id
                //store class id and class name for content_id and content_name for subscription
                Variables.sharedPreferences = context.getSharedPreferences(Variables.my_shared_pref, MODE_PRIVATE);
                Variables.editor = Variables.sharedPreferences.edit();
                Variables.editor.putString("content_id", data.subject_id);
                Variables.editor.putString("content_name", data.subject);
                Variables.editor.putString("type", "Subject");
                Variables.editor.apply();
            }

        }

    }
}