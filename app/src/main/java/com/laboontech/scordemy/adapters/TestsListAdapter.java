package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.TestsExploreActivity;
import com.laboontech.scordemy.databinding.ItemTestsBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.Video;

import java.io.Serializable;
import java.util.List;

public class TestsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Test> list;
    private Context context;
    private OnItemClickListener listener;


    public TestsListAdapter(List<Test> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        // for test sub topic layout
            ItemTestsBinding binding = ItemTestsBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new TestSubTopicViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Test data = list.get(position);
        TestsListAdapter.TestSubTopicViewHolder testSubHolder = ((TestsListAdapter.TestSubTopicViewHolder) holder);
        testSubHolder.binding.tvTestTittle.setText(data.title);
        testSubHolder.binding.tvTotalTest.setText(position+1+"");
        testSubHolder.binding.tvTestCount.setText(data.tests+" "+ context.getString(R.string.test_s));
        testSubHolder.binding.lyt.setOnClickListener(v->{
           listener.onItemClicked(v, data);
        });

        if (data.user_subscribed_test==1){
            testSubHolder.binding.tvExplore.setText(context.getString(R.string.subscribed));
            testSubHolder.binding.tvExplore.getBackground().setTint(context.getResources().getColor(R.color.green));
        }else{
            testSubHolder.binding.tvExplore.setText(context.getString(R.string.explore_go));
            testSubHolder.binding.tvExplore.getBackground().setTint(context.getResources().getColor(R.color.colorPrimary));
        }

        testSubHolder.binding.tvExplore.setOnClickListener(v->{
                listener.onItemClicked(v, data);
        });

        Glide.with(context)
                .load(data.tt_image)
                .placeholder(R.drawable.bg_curve)
                .into(testSubHolder.binding.imgTest);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class TestSubTopicViewHolder extends RecyclerView.ViewHolder {
        ItemTestsBinding binding;

        public TestSubTopicViewHolder(ItemTestsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface OnItemClickListener {
        void onItemClicked(View v, Test data);
    }


}