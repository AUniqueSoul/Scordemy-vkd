package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.databinding.ItemStoreTestseriesBinding;
import com.laboontech.scordemy.databinding.ItemTestsBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.interfaces.IStorePurchaseListener;
import com.laboontech.scordemy.utils.Animations;

import java.util.List;

public class StoreTestSeriesAdapter extends RecyclerView.Adapter<StoreTestSeriesAdapter.TestSubTopicViewHolder> {

    private final List<Test> list;
    private Context context;
    private IStorePurchaseListener listener;
    private int selected_test_id = 0;
    public StoreTestSeriesAdapter(List<Test> list, IStorePurchaseListener listener) {
        this.list = list;
        this.listener = listener;

    }

    @NonNull
    @Override
    public TestSubTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
            ItemStoreTestseriesBinding binding = ItemStoreTestseriesBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
            return new TestSubTopicViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TestSubTopicViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


     class TestSubTopicViewHolder extends RecyclerView.ViewHolder {
        ItemStoreTestseriesBinding binding;

        public TestSubTopicViewHolder(ItemStoreTestseriesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(Test data){
            Context context = binding.getRoot().getContext();
            binding.testName.setText(data.title);

            if (data.test_topic_id==selected_test_id) {
                binding.root.setBackground(context.getResources().getDrawable(R.drawable.border_square));
                listener.onTestSeriesPurchaseClick(data);
            } else {
                binding.root.setBackground(context.getResources().getDrawable(R.drawable.border_transparent));
            }

            if (data.user_subscribed_test==1){
                binding.tvSubscribed.setVisibility(View.VISIBLE);
            }else{
                binding.tvSubscribed.setVisibility(View.GONE);
            }

            binding.root.setOnClickListener(v -> {
                selected_test_id=data.test_topic_id;
                notifyDataSetChanged();
            });
        }
    }

}