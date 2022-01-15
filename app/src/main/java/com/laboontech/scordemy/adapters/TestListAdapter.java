package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.HomeActivity;
import com.laboontech.scordemy.activities.report.ReportActivity;
import com.laboontech.scordemy.databinding.ItemTestSubTopicBinding;
import com.laboontech.scordemy.databinding.ItemTestTopicBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.interfaces.ITestListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int TYPE_TOPIC = 1;
    private final static int TYPE_SUB_TOPIC = 2;
    private final List<Test> list;
    private int view_type;
    private Context context;
    private final ITestListener listener;

    public TestListAdapter(List<Test> list, int view_type, ITestListener listener) {
        this.list = list;
        this.view_type = view_type;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_TOPIC) { // for test topic layout
            ItemTestTopicBinding binding = ItemTestTopicBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new TestTopicViewHolder(binding);

        } else { // for test sub topic layout
            ItemTestSubTopicBinding binding = ItemTestSubTopicBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new TestSubTopicViewHolder(binding);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Test data = list.get(position);
        if (getItemViewType(position) == TYPE_TOPIC) {

            TestTopicViewHolder testHolder = ((TestTopicViewHolder) holder);

            testHolder.binding.tvNum.setText(String.valueOf(position + 1));
            testHolder.binding.tvTestTopic.setText(data.title);
            testHolder.binding.tvSubTestNumbering.setText(data.tests + " " + context.getString(R.string.test_s));

            testHolder.itemView.setOnClickListener(view -> {
                listener.onItemClickListener(data,position);
            });

        } else {
            TestSubTopicViewHolder testSubHolder = ((TestSubTopicViewHolder) holder);
            testSubHolder.binding.tvSubTopic.setText(data.title);
            testSubHolder.binding.tvQuestions.setText(data.question_count + " " + context.getString(R.string.questions));
            testSubHolder.binding.tvMarks.setText(data.marks + " " + context.getString(R.string.marks));
            testSubHolder.binding.tvDuration.setText(data.duration + " "+ context.getString(R.string.min));

            testSubHolder.itemView.setOnClickListener(view -> {
                SharedPreferences sharedPreferences = context.getSharedPreferences("PREF"+data.test_sub_topic_id,MODE_PRIVATE);
                int testId = sharedPreferences.getInt("testId"+data.test_sub_topic_id,0);
                if (testId==data.test_sub_topic_id){
                    context.startActivity(new Intent(context, ReportActivity.class).putExtra("given_test_id",data.test_sub_topic_id));
                }else{
                    ((HomeActivity)context).showInstructionPage(data);
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (view_type == TYPE_TOPIC) {
            return TYPE_TOPIC;
        } else {
            return TYPE_SUB_TOPIC;
        }
    }

    static class TestTopicViewHolder extends RecyclerView.ViewHolder {
        ItemTestTopicBinding binding;

        public TestTopicViewHolder(ItemTestTopicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class TestSubTopicViewHolder extends RecyclerView.ViewHolder {
        ItemTestSubTopicBinding binding;

        public TestSubTopicViewHolder(ItemTestSubTopicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
