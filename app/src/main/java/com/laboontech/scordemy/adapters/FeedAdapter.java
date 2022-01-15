package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.laboontech.scordemy.activities.FeedContentsActivity;
import com.laboontech.scordemy.entity.Feed;
import com.laboontech.scordemy.interfaces.IFeedOnItemClickListener;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.DataViewHolder> {
    private static final String TAG = "BlogAdapterTest";
    private Context context;
    private List<Feed> list;
    private IFeedOnItemClickListener listener;

    public FeedAdapter(Context context, List<Feed> list, IFeedOnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new DataViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Feed data = list.get(position);
        holder.title.setText(data.title);
        holder.date.setText(Functions.dateFormat(data.date));
        Log.d(TAG, "onBindViewHolder: "+Variables.IMAGE_PATH + data.image);
        Glide.with(context).load(Variables.IMAGE_PATH + data.image).into(holder.imageView);
        holder.likes.setText(data.like_count + " " + context.getString(R.string.likes));
        holder.comments.setText(data.comment_count + " " + context.getString(R.string.comments));
        holder.views.setText(data.views + " " + context.getString(R.string.views));

        Animations.fade_in(context, holder.itemView);
        if (data.liked != null) {
            if (data.liked.equals("1")) {
                holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_liked));
            } else {
                holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
            }
        }

        holder.itemView.setOnClickListener(view -> {
            openFeedContentActivity(context, data, false);

        });

        holder.comment.setOnClickListener(view -> {
            openFeedContentActivity(context, data, true);
        });


        holder.like.setOnClickListener(view -> {
            onLikeUnlike(data, holder.like, holder.likes);
            listener.onItemClickListener(data, view);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, like, share, comment;
        private TextView title, date, likes, views, comments;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            like = itemView.findViewById(R.id.like);
//            share = itemView.findViewById(R.id.share);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
            likes = itemView.findViewById(R.id.likes);
            views = itemView.findViewById(R.id.views);
            comments = itemView.findViewById(R.id.comments);
        }
    }

    private void openFeedContentActivity(Context context, Feed data, boolean comment) {
        Intent intent = new Intent(context, FeedContentsActivity.class);
        intent.putExtra("feed_id", data.id);
        intent.putExtra("image", data.image);
        intent.putExtra("title", data.title);
        intent.putExtra("date", data.date);
        intent.putExtra("message", data.message);
        intent.putExtra("comment", comment);
        intent.putExtra("liked", data.liked);
        intent.putExtra("views", data.views);
        context.startActivity(intent);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void onLikeUnlike(Feed data, ImageView like_img, TextView like_txt) {
        if (data.liked.equals("1")) {
            data.liked = "0";
            like_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
            data.like_count = "" + (Integer.parseInt(data.like_count) - 1);
        } else {
            data.liked = "1";
            like_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_liked));
            data.like_count = "" + (Integer.parseInt(data.like_count) + 1);
        }

        Animations.fade_in(context, like_img);
        like_txt.setText(data.like_count + " " + context.getString(R.string.likes));
    }


}
