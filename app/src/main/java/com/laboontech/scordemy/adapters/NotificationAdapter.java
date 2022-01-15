package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.databinding.ItemNotificationBinding;
import com.laboontech.scordemy.entity.Notifications;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.DataViewHolder> {


    private List<Notifications> list;


    public NotificationAdapter(List<Notifications> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DataViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationBinding binding;

        public DataViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(Notifications data) {
            binding.tvTitle.setText(data.not_title);
            binding.tvMessage.setText(data.not_message);
            binding.tvDate.setText(data.date);
            Glide.with(binding.getRoot().getContext())
                    .load(Variables.IMAGE_PATH+data.not_image)
                    .into(binding.imageView);

        }
    }

}
