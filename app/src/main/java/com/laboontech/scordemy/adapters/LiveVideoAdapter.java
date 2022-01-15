package com.laboontech.scordemy.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.laboontech.scordemy.activities.LiveContentActivity;
import com.laboontech.scordemy.databinding.ItemLiveVideosBinding;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.utils.Constants;


import java.util.List;


public class LiveVideoAdapter extends RecyclerView.Adapter<LiveVideoAdapter.DataViewHolder> {

    private List<Video> list;
    private FragmentManager fragmentManager1;

    public LiveVideoAdapter(List<Video> list) {
        this.list = list;
    }



    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLiveVideosBinding binding = ItemLiveVideosBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        ItemLiveVideosBinding binding;

        public DataViewHolder(ItemLiveVideosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(Video data) {
            Context context = binding.getRoot().getContext();
            Glide.with(context)
                    .load(data.lc_image)
                    .into(binding.imageView);
            binding.tvTitle.setText(data.lc_title);
            binding.tvSchedule.setText("Scheduled at : "+data.lc_schedule_period);
            binding.getRoot().setOnClickListener(v -> {

                Log.e("Live_video_id", data.lc_id);
                Intent intent = new Intent(context, LiveContentActivity.class);
                Gson gson = new Gson();
                String liveData = gson.toJson(data);
                intent.putExtra(Constants.KEY_CONTENT_DATA, liveData);
                intent.putExtra(Constants.KEY_TYPE, Constants.TYPE_LIVE);
                context.startActivity(intent);
            });


        }
    }


}
