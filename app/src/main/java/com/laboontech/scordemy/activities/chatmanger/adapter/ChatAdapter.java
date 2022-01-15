/*
 * Copyright (c) Ishant Sharma
 * ishant@coretechies.com
 * Android Developer
 */
package com.laboontech.scordemy.activities.chatmanger.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.laboontech.scordemy.activities.chatmanger.models.ChatModel;
import com.laboontech.scordemy.activities.chatmanger.time.TimeAgo;
import com.laboontech.scordemy.databinding.ChatMessageItemBinding;


import java.util.ArrayList;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private static boolean IsSelected = false;
    private Context activity;
    private List<ChatModel> singleChatTables = new ArrayList<>();
    private static String VideoUrl = null;
    private Activity activity1;

    public ChatAdapter(Activity activity, List<ChatModel> singleChatHistoryTables) {
        this.activity = activity;
        this.activity1 = activity;
        this.singleChatTables = singleChatHistoryTables;

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatMessageItemBinding binding = ChatMessageItemBinding.inflate(LayoutInflater.from(activity), parent, false);
        return new ChatViewHolder(binding);
    }

    public void UpdateVideoUrl(String VideoUrl) {
        this.VideoUrl = VideoUrl;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, final int position) {
        holder.ViewBind(singleChatTables.get(position));
    }


    public List<ChatModel> getSingleChatHistoryTables() {
        return singleChatTables;
    }


    public void removeItem(int position) {
        singleChatTables.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return singleChatTables.size();
    }

    private Filter fRecords;


    public void AddChat(List<ChatModel> singleChatTables) {
        this.singleChatTables=singleChatTables;
        notifyDataSetChanged();
    }

    public synchronized void UpdateChat(ChatModel model) {
        for(int i=0;i<singleChatTables.size();i++){
            if(singleChatTables.get(i).getMessageTime().equalsIgnoreCase(model.getMessageTime())){
                singleChatTables.remove(i);
            }
        }
        singleChatTables.add(model);
       notifyDataSetChanged();
    }

    public void DeleteChatHistoryFromFirebaseConsole(ChatModel model) {
        for(int i=0;i<singleChatTables.size();i++){
            if(singleChatTables.get(i).getMessageTime().equalsIgnoreCase(model.getMessageTime())){
                singleChatTables.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public void AddChat(ChatModel model) {
        boolean isFound=false;
        if(singleChatTables!=null && singleChatTables.size()>0){
            for(int i=0;i<singleChatTables.size();i++){
                if(singleChatTables.get(i).getMessageTime().equalsIgnoreCase(model.getMessageTime())){
                  isFound=true;
                }
            }
            if(!isFound){
                singleChatTables.add(model);
            }
        }else{
            singleChatTables.add(model);
        }
        notifyDataSetChanged();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private ChatMessageItemBinding binding;

        public ChatViewHolder(@NonNull ChatMessageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void ViewBind(final ChatModel model) {
            binding.username.setText(model.getSendername());
            Glide.with(activity).load(model.getSenderImage()).into(binding.userImage);
            binding.receiverMessageView.setText(model.getMessage());
            binding.time.setText(TimeAgo.getRelativeTime(String.valueOf(model.getMessageTime())));
        }
    }
}
