package com.laboontech.scordemy.activities.ebooks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.activities.ebooks.EbookClassesActivity;
import com.laboontech.scordemy.activities.ebooks.PdfViewerActivity;
import com.laboontech.scordemy.databinding.ItemEbookBinding;
import com.laboontech.scordemy.databinding.ItemPdfBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.interfaces.IPdfDownloadListener;

import java.util.List;

public class EbookPdfAdapter extends RecyclerView.Adapter<EbookPdfAdapter.DataViewHolder> {

    private final List<Ebook> list;
    private IPdfDownloadListener listener;

    public EbookPdfAdapter(List<Ebook> list, IPdfDownloadListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPdfBinding binding = ItemPdfBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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


     class DataViewHolder extends RecyclerView.ViewHolder {
        ItemPdfBinding binding;

        public DataViewHolder(ItemPdfBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(Ebook data) {
            Context context = binding.getRoot().getContext();
            binding.title.setText(data.ep_title);


            binding.btnOpen.setOnClickListener(v -> {

                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra("heading",data.ep_title);
                    intent.putExtra("pdf_url",data.ep_pdf);
                    context.startActivity(intent);
            });


            binding.btnOffline.setOnClickListener(v -> listener.onEbookPdfDownload(data,binding.numberProgressBar));
        }


    }


}
