package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.databinding.ItemSubscriptionPlanBinding;
import com.laboontech.scordemy.entity.Subscription;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceSubscriptionPlan;

import java.util.List;

public class SubscriptionPlansAdapter extends RecyclerView.Adapter<SubscriptionPlansAdapter.RecyclerViewHolder> {
    private List<Subscription> list;
    private InterfaceSubscriptionPlan listener;
    private String plan_id = "";

    public SubscriptionPlansAdapter(List<Subscription> list, InterfaceSubscriptionPlan listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscriptionPlanBinding binding = ItemSubscriptionPlanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        ItemSubscriptionPlanBinding binding;

        public RecyclerViewHolder(ItemSubscriptionPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        void onBind(Subscription data) {
            Context context = binding.getRoot().getContext();
            binding.tvPlanPrice.setText(data.plan_price+ " INR");
           binding.tvPlanType.setText(data.plan_type);
           binding.tvDescription.setText(data.description);



            if (data.subs_id.equals(plan_id)) {
                binding.root.setBackground(context.getResources().getDrawable(R.drawable.border));
                listener.onPlanSelectedListener(data);
            } else {
                binding.root.setBackground(context.getResources().getDrawable(R.drawable.border_transparent));
            }

            binding.root.setOnClickListener(v -> {
                plan_id = data.subs_id;
                notifyDataSetChanged();
            });


        }
    }
}
