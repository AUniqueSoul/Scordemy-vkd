package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.activities.VideoActivity;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.DataViewHolder> {
    private static final String TAG = "VideosAdapterTest";
    private Context context;
    private List<Video> list;


    public VideosAdapter(Context context, List<Video> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_videos, parent, false);
        return new DataViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Video data = list.get(position);

        Log.d(TAG, "onBindViewHolder: "+Variables.IMAGE_PATH + data.thumbnail);
        Glide.with(context).load(Variables.IMAGE_PATH + data.thumbnail).into(holder.imageView);
        if (Variables.video_type.equals("feed")){
            holder.title.setText(data.title);
        }else
        {
            holder.title.setText(context.getString(R.string.lecture) + " " + (position + 1));
        }

        Animations.fade_in(context, holder.itemView);

        if (data.paid_type.equals(Constants.PAID)) {
            if (MainContentActivity.user_subscription_status.equals(Constants.ACTIVE)){
                holder.lock.setVisibility(View.GONE);
                holder.blackView.setVisibility(View.GONE);
            }else{
                holder.lock.setVisibility(View.VISIBLE);
                holder.blackView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.lock.setVisibility(View.GONE);
            holder.blackView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            if (data.paid_type.equals(Constants.PAID)) {
                if (MainContentActivity.user_subscription_status.equals(Constants.ACTIVE)){
                    Intent intent = new Intent(context, VideoActivity.class);
                    Variables.videoData = data;
                    context.startActivity(intent);
                }else{
                    Functions.showBottomSheet(context);
                }
            } else {
                Intent intent = new Intent(context, VideoActivity.class);
                Variables.videoData = data;
                context.startActivity(intent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView title, video_duration;
        private ImageView imageView,lock;
        private  View blackView;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            lock = itemView.findViewById(R.id.lock);
            title = itemView.findViewById(R.id.title);
            video_duration = itemView.findViewById(R.id.video_duration);
            blackView = itemView.findViewById(R.id.blackView);
        }
    }

}
