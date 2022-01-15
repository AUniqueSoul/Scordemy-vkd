package com.laboontech.scordemy.activities.ebooks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.activities.ebooks.EbookPdfActivity;
import com.laboontech.scordemy.databinding.ItemBooksolutionBinding;
import com.laboontech.scordemy.databinding.ItemLiveContentBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.entity.LiveContentVideo;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LiveContentAdapter  extends RecyclerView.Adapter<LiveContentAdapter.DataViewHolder> {

    private final List<LiveContentVideo> list;
    LiveContentInnerAdapter adapter;
    private static final String TAG = "LiveContentAdapter";

    public LiveContentAdapter(List<LiveContentVideo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public LiveContentAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLiveContentBinding binding = ItemLiveContentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveContentAdapter.DataViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull LiveContentAdapter.DataViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class DataViewHolder extends RecyclerView.ViewHolder {
        ItemLiveContentBinding binding;

        public DataViewHolder(ItemLiveContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(LiveContentVideo data) {


            Context context = binding.getRoot().getContext();
            binding.tvTitle.setText(data.ls_title);
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            if (data.Video_list.size() > 0) {
                    binding.recyclerview.setAdapter(new LiveContentInnerAdapter(data.Video_list));
            } else {
                Log.d(TAG, "onBind: "+"no data found");
//                Functions.ShowToast(context, "No Data Found");
            }
        }


    }


}