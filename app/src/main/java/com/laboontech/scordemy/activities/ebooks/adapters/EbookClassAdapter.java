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
import com.laboontech.scordemy.activities.ebooks.EbookStreamsActivity;
import com.laboontech.scordemy.databinding.ItemEbookBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.utils.Constants;

import java.util.List;

public class EbookClassAdapter extends RecyclerView.Adapter<EbookClassAdapter.DataViewHolder> {

    private final List<Ebook> list;

    public EbookClassAdapter(List<Ebook> list) {
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
            binding.title.setText(data.ec_title);
            binding.subTitle.setText("");
            Glide.with(context).load(data.ec_image).into(binding.imageView);

            binding.getRoot().setOnClickListener(v -> {
                if (data.ec_subtable.equalsIgnoreCase("Stream")) {
                    Intent intent = new Intent(context, EbookStreamsActivity.class);
                    intent.putExtra("heading", data.ec_title);
                    intent.putExtra("prefix", Constants.CLASS_PREFIX);
                    intent.putExtra("id", data.ec_id);
                    context.startActivity(intent);
                } else if (data.ec_subtable.equalsIgnoreCase("Solution")) {
                    Intent intent = new Intent(context, EbookBookSolutionsActivity.class);
                    intent.putExtra("heading", data.ec_title);
                    intent.putExtra("prefix", Constants.CLASS_PREFIX);
                    intent.putExtra("id", data.ec_id);
                    context.startActivity(intent);
                }
            });
        }


    }


}
