package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.HomeActivity;
import com.laboontech.scordemy.activities.TestDetailActivity;
import com.laboontech.scordemy.activities.report.ReportActivity;
import com.laboontech.scordemy.databinding.ItemTestsBinding;
import com.laboontech.scordemy.databinding.ItemTestsSublistBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TestsSubListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Test> list;
    private Context context;
    private OnItemClickListener listener;

    public TestsSubListAdapter(List<Test> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        // for test sub topic layout
        ItemTestsSublistBinding binding = ItemTestsSublistBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new TestsSubListAdapter.TestSubTopicViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Test data = list.get(position);
        TestsSubListAdapter.TestSubTopicViewHolder testSubHolder = ((TestsSubListAdapter.TestSubTopicViewHolder) holder);
        testSubHolder.binding.tvTestDetail.setText(data.title);
        testSubHolder.binding.tvMarks.setText(data.marks+" "+context.getString(R.string.marks));
        testSubHolder.binding.tvQuestions.setText(data.question_count +" "+context.getString(R.string.questions));
        testSubHolder.binding.tvTime.setText(data.duration+ " "+context.getString(R.string.min));

        testSubHolder.binding.mainLyt.setOnClickListener(v->{
            //shared preference init
            SharedPreferences sharedPreferences = context.getSharedPreferences("PREF"+data.test_sub_topic_id,MODE_PRIVATE);
            int testId = sharedPreferences.getInt("testId"+data.test_sub_topic_id,0);

            if (data.user_subscribed_test==1){
             //Checking if user has given this test already
                if (testId==data.test_sub_topic_id){
                    context.startActivity(new Intent(context, ReportActivity.class).putExtra("given_test_id",data.test_sub_topic_id));
                }else{
                    //else show instruction page
                    Intent intent = new Intent(context, TestDetailActivity.class);
                    intent.putExtra("id", data.test_sub_topic_id);
                    intent.putExtra("Title", data.title);
                    intent.putExtra("Instruction", data.instruction);
                    intent.putExtra("Duration", data.duration);
                    intent.putExtra("Marks", data.marks);
                    intent.putExtra("Questions", data.question_count);
                    context.startActivity(intent);
                }

            }else{
                if (data.paid_type.equals(Constants.PAID)){
                    //content locked
                    Functions.showSnackBar(testSubHolder.binding.getRoot(),"Content Locked - Purchase From Store");
                }else{
                    if (testId==data.test_sub_topic_id){
                        context.startActivity(new Intent(context, ReportActivity.class).putExtra("given_test_id",data.test_sub_topic_id));
                    }else{
                        Intent intent = new Intent(context, TestDetailActivity.class);
                        intent.putExtra("id", data.test_sub_topic_id);
                        intent.putExtra("Title", data.title);
                        intent.putExtra("Instruction", data.instruction);
                        intent.putExtra("Duration", data.duration);
                        intent.putExtra("Marks", data.marks);
                        intent.putExtra("Questions", data.question_count);
                        context.startActivity(intent);
                    }

                }
            }


        });

        if (data.user_subscribed_test!=1){
            if (data.paid_type.equals(Constants.PAID)){
                testSubHolder.binding.tvFree.setText(context.getString(R.string.paid));
                testSubHolder.binding.tvFree.getBackground().setTint(context.getResources().getColor(R.color.black));
            }else{
                testSubHolder.binding.tvFree.setText(context.getString(R.string.free));
                testSubHolder.binding.tvFree.getBackground().setTint(context.getResources().getColor(R.color.colorPrimary));
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class TestSubTopicViewHolder extends RecyclerView.ViewHolder {
        ItemTestsSublistBinding binding;

        public TestSubTopicViewHolder(ItemTestsSublistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface OnItemClickListener {
        void onItemClicked(View v, Test data);
    }
}