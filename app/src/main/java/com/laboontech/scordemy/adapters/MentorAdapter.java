package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.databinding.ItemMentorsBinding;
import com.laboontech.scordemy.databinding.ItemTeachersBinding;
import com.laboontech.scordemy.entity.Teachers;
import com.laboontech.scordemy.utils.Functions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.DataViewHolder> {

    private final List<Teachers> list;

    public MentorAdapter(List<Teachers> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MentorAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMentorsBinding binding = ItemMentorsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MentorAdapter.DataViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MentorAdapter.DataViewHolder holder, int position) {
        holder.onBind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class DataViewHolder extends RecyclerView.ViewHolder {
        ItemMentorsBinding binding;

        public DataViewHolder(ItemMentorsBinding binding) {
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
                binding.lytMainMentors.setOnClickListener(v->{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    ViewGroup viewGroup = binding.getRoot().findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.youtube_dialog, viewGroup, false);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

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

                   // youTubePlayerView.enterFullScreen();
                });
            }
        }


    }


}
