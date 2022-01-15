package com.laboontech.scordemy.activities.ebooks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.activities.ebooks.EbookClassesActivity;
import com.laboontech.scordemy.activities.ebooks.EbookYearsActivity;
import com.laboontech.scordemy.databinding.ItemChaptersBinding;
import com.laboontech.scordemy.databinding.ItemEbookBinding;
import com.laboontech.scordemy.entity.Chapter;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.DataViewHolder> {

    private final List<Ebook> list;

    public EbookAdapter(List<Ebook> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEbookBinding binding = ItemEbookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DataViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class DataViewHolder extends RecyclerView.ViewHolder {
        ItemEbookBinding binding;

        public DataViewHolder(ItemEbookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(Ebook data) {
            Context context = binding.getRoot().getContext();
            binding.title.setText(data.e_tittle);
            binding.subTitle.setText(data.sub_title);
            Glide.with(context).load(data.e_image).into(binding.imageView);

            binding.getRoot().setOnClickListener(v -> {
                if (data.sub_table.equalsIgnoreCase("Class")){
                    Intent intent = new Intent(context, EbookClassesActivity.class);
                    intent.putExtra("heading",data.e_tittle);
                    intent.putExtra("prefix",Constants.EBOOK_PREFIX);
                    intent.putExtra("id",data.ebook_id);
                    context.startActivity(intent);
                }else if (data.sub_table.equalsIgnoreCase("Year")){
                    Intent intent = new Intent(context, EbookYearsActivity.class);
                    intent.putExtra("heading",data.e_tittle);
                    intent.putExtra("prefix",Constants.EBOOK_PREFIX);
                    intent.putExtra("id",data.ebook_id);
                    context.startActivity(intent);
                }

            });
        }


    }


}
