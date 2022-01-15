package com.laboontech.scordemy.activities.ebooks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.activities.ebooks.EbookBookSolutionsActivity;
import com.laboontech.scordemy.activities.ebooks.EbookClassesActivity;
import com.laboontech.scordemy.activities.ebooks.EbookPdfActivity;
import com.laboontech.scordemy.databinding.ItemEbookBinding;
import com.laboontech.scordemy.databinding.ItemStreamBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.utils.Constants;

import java.util.List;

public class EbookStreamAdapter extends RecyclerView.Adapter<EbookStreamAdapter.DataViewHolder> {

    private final List<Ebook> list;

    public EbookStreamAdapter(List<Ebook> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStreamBinding binding = ItemStreamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        ItemStreamBinding binding;

        public DataViewHolder(ItemStreamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(Ebook data) {
            Context context = binding.getRoot().getContext();
            binding.title.setText(data.es_title);
            Glide.with(context).load(data.es_image).into(binding.imageView);

            binding.getRoot().setOnClickListener(v -> {
                if (data.es_sub_table.equalsIgnoreCase("Solution")){
                    Intent intent = new Intent(context, EbookBookSolutionsActivity.class);
                    intent.putExtra("heading", data.es_title);
                    intent.putExtra("prefix", Constants.STREAM_PREFIX);
                    intent.putExtra("id", data.es_id);
                    context.startActivity(intent);
                } if (data.es_sub_table.equalsIgnoreCase("PDF")){
                    Intent intent = new Intent(context, EbookPdfActivity.class);
                    intent.putExtra("heading", data.es_title);
                    intent.putExtra("prefix", Constants.STREAM_PREFIX);
                    intent.putExtra("id", data.es_id);
                    context.startActivity(intent);
                }

            });
        }


    }


}
