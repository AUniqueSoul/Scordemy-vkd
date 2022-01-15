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
import com.laboontech.scordemy.activities.YoutubeVideoActivity;
import com.laboontech.scordemy.activities.LiveContentActivity;
import com.laboontech.scordemy.databinding.LiveContentInnerItemBinding;
import com.laboontech.scordemy.entity.LiveContentVideo;
import com.laboontech.scordemy.utils.Functions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LiveContentInnerAdapter  extends RecyclerView.Adapter<LiveContentInnerAdapter.DataViewHolder> {

    private final List<LiveContentVideo> list;

    public LiveContentInnerAdapter(List<LiveContentVideo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public LiveContentInnerAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LiveContentInnerItemBinding binding = LiveContentInnerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveContentInnerAdapter.DataViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull LiveContentInnerAdapter.DataViewHolder holder, int position) {
        holder.onBind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class DataViewHolder extends RecyclerView.ViewHolder {
        LiveContentInnerItemBinding binding;

        public DataViewHolder(LiveContentInnerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(LiveContentVideo data) {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String currentDate =   formatter.format(new Date(System.currentTimeMillis()));

            Date scheduleDate = new Date();
            Date todayDate = new Date();
            try {
                 scheduleDate = formatter.parse(data.lv_schedule_datetime);
                 todayDate = formatter.parse(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Context context = binding.getRoot().getContext();
            binding.tvTitle.setText(data.lv_title);
            binding.tvCreatedDate.setText(data.lv_schedule_datetime);

            Glide.with(context)
                    .load(data.lv_image)
                    .into(binding.imageView);



            if(((LiveContentActivity)context).is_userSubscribed==1){
                if (scheduleDate.after(todayDate)) {

                    binding.lock.setVisibility(View.VISIBLE);
                    binding.blackView.setVisibility(View.VISIBLE);

                    binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Functions.showSnackBar(binding.getRoot(),"Video will open at "+data.lv_schedule_datetime);
                        }
                    });

                } else {

                    binding.lock.setVisibility(View.GONE);
                    binding.blackView.setVisibility(View.GONE);
                    binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, YoutubeVideoActivity.class);
                            intent.putExtra("thumbnail",data.lv_image);
                            intent.putExtra("title",data.lv_title);
                            intent.putExtra("sub_title",data.lv_title);
                            intent.putExtra("video_url",data.lv_youtube_url);
                            intent.putExtra("video_id",data.lv_id);
                            context.startActivity(intent);
                        }
                    });
                }
            }else{
                binding.lock.setVisibility(View.VISIBLE);
                binding.blackView.setVisibility(View.VISIBLE);
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Functions.showSnackBar(binding.getRoot(),"Content Locked : Please Purchase To Unlock ");
                        ((LiveContentActivity)context).showDetailsOverview();
                    }
                });
            }



        }


    }


}