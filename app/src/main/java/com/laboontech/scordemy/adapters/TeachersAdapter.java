package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.databinding.ItemChaptersBinding;
import com.laboontech.scordemy.databinding.ItemTeachersBinding;
import com.laboontech.scordemy.entity.Chapter;
import com.laboontech.scordemy.entity.Teachers;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.DataViewHolder> {

    private final List<Teachers> list;

    public TeachersAdapter(List<Teachers> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTeachersBinding binding = ItemTeachersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        ItemTeachersBinding binding;

        public DataViewHolder(ItemTeachersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBind(Teachers data) {
            Context context = binding.getRoot().getContext();
            Glide.with(context).load(data.profile_pic).into(binding.ivUser);
            binding.tvBio.setText(data.bio);
            binding.tvName.setText(data.name);

            if (!data.youtube_url.equals("")){
                binding.lytMainTeacher.setOnClickListener(v->{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    ViewGroup viewGroup = binding.getRoot().findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.youtube_dialog, viewGroup, false);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                    YouTubePlayerView youTubePlayerView = dialogView.findViewById(R.id.youtube_player_view);


                    ImageView imgClose = dialogView.findViewById(R.id.imgClose);
                    imgClose.setOnClickListener(dialogV->{
                        youTubePlayerView.release();
                        alertDialog.dismiss();
                    });

                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(Functions.getYoutubeId(data.youtube_url), 0);
                        }
                    });
                });
            }

        }


    }


}
