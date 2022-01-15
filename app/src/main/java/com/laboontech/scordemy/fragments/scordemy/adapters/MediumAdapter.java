package com.laboontech.scordemy.fragments.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.adapters.QuizAdapter;
import com.laboontech.scordemy.databinding.ItemMediumBinding;
import com.laboontech.scordemy.databinding.ItemQuizBinding;
import com.laboontech.scordemy.entity.Medium;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceMedium;

import java.util.List;

public class MediumAdapter extends RecyclerView.Adapter<MediumAdapter.RecyclerViewHolder> {
    private List<Medium> list;
    private InterfaceMedium listener;
    private String medium_id = "";

    public MediumAdapter(List<Medium> list, InterfaceMedium listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMediumBinding binding = ItemMediumBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ItemMediumBinding binding;

        public RecyclerViewHolder(ItemMediumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        void onBind(Medium data) {
            Context context = binding.getRoot().getContext();
            binding.medium.setText(data.medium);

            if (TextUtils.isEmpty(medium_id)) {
              medium_id=list.get(0).medium_id;
            }else{

            }

            if (data.medium_id.equals(medium_id)) {
                binding.medium.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                binding.medium.setBackground(context.getResources().getDrawable(R.drawable.border));
                listener.onItemClickListener(data);
            } else {
                binding.medium.setBackground(context.getResources().getDrawable(R.drawable.border_transparent));
                binding.medium.setTextColor(context.getResources().getColor(R.color.colorBlack));
            }

            binding.medium.setOnClickListener(v -> {
                medium_id = data.medium_id;
                notifyDataSetChanged();
            });
        }
    }
}
