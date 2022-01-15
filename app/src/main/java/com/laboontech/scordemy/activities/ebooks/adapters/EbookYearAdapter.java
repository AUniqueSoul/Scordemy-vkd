package com.laboontech.scordemy.activities.ebooks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.activities.ebooks.EbookPdfActivity;
import com.laboontech.scordemy.activities.ebooks.EbookStreamsActivity;
import com.laboontech.scordemy.databinding.ItemEbookBinding;
import com.laboontech.scordemy.databinding.ItemYearBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.utils.Constants;

import java.util.List;

public class EbookYearAdapter extends RecyclerView.Adapter<EbookYearAdapter.DataViewHolder> {

    private final List<Ebook> list;

    public EbookYearAdapter(List<Ebook> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemYearBinding binding = ItemYearBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        ItemYearBinding binding;

        public DataViewHolder(ItemYearBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(Ebook data) {
            Context context = binding.getRoot().getContext();
            binding.title.setText(data.ey_title);


            binding.getRoot().setOnClickListener(v -> {
                if (data.ey_subtable.equalsIgnoreCase("pdf")){
                    Intent intent = new Intent(context, EbookPdfActivity.class);
                    intent.putExtra("heading",data.ey_title);
                    intent.putExtra("prefix", Constants.YEAR_PREFIX);
                    intent.putExtra("id",data.ey_id);
                    context.startActivity(intent);
                }

            });
        }


    }


}
