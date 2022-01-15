package com.laboontech.scordemy.activities.ebooks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.activities.ebooks.EbookPdfActivity;
import com.laboontech.scordemy.databinding.ItemBooksolutionBinding;
import com.laboontech.scordemy.databinding.ItemStreamBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.utils.Constants;

import java.util.List;

public class EbookBookSolutionAdapter extends RecyclerView.Adapter<EbookBookSolutionAdapter.DataViewHolder> {
    private static final String TAG = "EbookBookSolutionAdapt";
    private final List<Ebook> list;

    public EbookBookSolutionAdapter(List<Ebook> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBooksolutionBinding binding = ItemBooksolutionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        ItemBooksolutionBinding binding;

        public DataViewHolder(ItemBooksolutionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(Ebook data) {
            Context context = binding.getRoot().getContext();
            binding.title.setText(data.ebs_title);
            Log.d(TAG, "onBind: "+data.ebs_image);
            Glide.with(context).load(data.ebs_image).into(binding.imageView);

            binding.btnOpen.setOnClickListener(v -> {
                if (data.ebs_sub_table.equalsIgnoreCase("pdf")){
                    Intent intent = new Intent(context, EbookPdfActivity.class);
                    intent.putExtra("heading",data.ebs_title);
                    intent.putExtra("prefix", Constants.BOOKSOLUTION_PREFIX);
                    intent.putExtra("id",data.ebs_id);
                    context.startActivity(intent);
                }

            });
        }


    }


}
