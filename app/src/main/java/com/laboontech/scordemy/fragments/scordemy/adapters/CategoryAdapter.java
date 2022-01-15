package com.laboontech.scordemy.fragments.scordemy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.databinding.ItemChaptersBinding;
import com.laboontech.scordemy.databinding.ItemMediumBinding;
import com.laboontech.scordemy.entity.Category;
import com.laboontech.scordemy.entity.Medium;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceCategory;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecyclerViewHolder> {
    private List<Category> list;
    private InterfaceCategory listener;
    private String type;

    public CategoryAdapter(List<Category> list, InterfaceCategory listener, String type) {
        this.list = list;
        this.listener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChaptersBinding binding = ItemChaptersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        ItemChaptersBinding binding;

        public RecyclerViewHolder(ItemChaptersBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void onBind(Category data) {
            Glide.with(binding.getRoot().getContext())
                .load(Variables.IMAGE_PATH+data.image)
                .into(binding.imageView);
            if (type.equals(Constants.TYPE_CLASS)){

                binding.tvChapterName.setText(data.class_name);
                binding.tvNumbering.setText(String.valueOf(getAdapterPosition()+1));
                binding.getRoot().setOnClickListener(v -> {
                    //open Subject
                    listener.onCategoryItemClickListener(data,Constants.TYPE_CLASS);
                });
            }else{
                binding.tvChapterName.setText(data.subject);
                binding.tvNumbering.setText(String.valueOf(getAdapterPosition()+1));
                binding.getRoot().setOnClickListener(v -> {
                    //open chapters
                    listener.onCategoryItemClickListener(data,Constants.TYPE_SUBJECT);
                });
            }

        }
    }
}
