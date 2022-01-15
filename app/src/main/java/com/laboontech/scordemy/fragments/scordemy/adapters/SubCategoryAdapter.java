package com.laboontech.scordemy.fragments.scordemy.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.databinding.ItemChaptersBinding;
import com.laboontech.scordemy.databinding.ItemMediumBinding;
import com.laboontech.scordemy.entity.Medium;
import com.laboontech.scordemy.entity.SubCategory;
import com.laboontech.scordemy.fragments.scordemy.interfaces.InterfaceSubCategory;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;

import retrofit2.Callback;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.RecyclerViewHolder> {
    private List<SubCategory> list;
    private InterfaceSubCategory listener;

    public SubCategoryAdapter(List<SubCategory> list, InterfaceSubCategory listener) {
        this.list = list;
        this.listener = listener;
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
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void onBind(SubCategory data) {
            Glide.with(binding.getRoot().getContext())
                    .load(Variables.IMAGE_PATH+data.image)
                    .into(binding.imageView);
            binding.tvChapterName.setText(data.chapter);
            binding.tvNumbering.setText(String.valueOf(getAdapterPosition()+1));
            binding.tvVideosNum.setText(data.video_count+" Video(s)");
            binding.tvQuizNum.setText(data.quiz_title_count+" Quiz(s)");
            binding.tvConceptNum.setText(data.notes_count+" Note(s)");

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChapterClickListener(data);
                }
            });
        }
    }
}
