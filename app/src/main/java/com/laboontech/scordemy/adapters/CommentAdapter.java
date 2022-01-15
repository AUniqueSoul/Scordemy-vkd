package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.entity.Comment;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.interfaces.IOnItemClickListener;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;


import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.DataViewHolder> {
    private Context context;
    private List<Comment> commentList;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private String user_id;
private IOnItemClickListener listener;


    public CommentAdapter(Context context, List<Comment> commentList, IOnItemClickListener listener) {
        this.context = context;
        this.commentList = commentList;
        this.listener = listener;
    }




    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new DataViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Comment data = commentList.get(position);

        Glide.with(context).
                load(Variables.IMAGE_PATH + data.profile_pic)
                .placeholder(context.getResources().getDrawable(R.drawable.user))
                .into(holder.user_dp);
        holder.text_name.setText(data.name);
        holder.text_comment.setText(data.comment);
        holder.text_date.setText(Functions.dateFormat(data.date));
        if (Variables.video_type.equals("feed")){
            holder.text_likes.setVisibility(View.GONE);
            holder.like.setVisibility(View.GONE);
        }else {
            holder.text_likes.setText(data.comment_likes_count + " " + context.getString(R.string.likes));

        }

        sharedPreferenceUtil = new SharedPreferenceUtil(context);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        if (userData != null) {
            user_id = userData.user_id;
        }
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }

//        if (data.user_id.equals(user_id)){
//            holder.delete_comment.setVisibility(View.VISIBLE);
//        }else{
//            holder.delete_comment.setVisibility(View.GONE);
//        }


//        holder.comment_likes.setOnClickListener(view -> listener.onItemClickListener(data, view));
//        holder.comment_likes.setText(Functions.GetSuffix(data.comment_likes_count) + " " + context.getString(R.string.likes));


        if (data.liked != null) {
            if (data.liked.equals("1")) {
                holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb));
            } else {
                holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_outline_thumb));
            }
        }
//
//
        holder.like.setOnClickListener(view -> {
            onLikeUnlike(data, holder.like, holder.text_likes);
            listener.onItemClickListener(data, view);
        });
//
//
//        if (Variables.comment_type.equals(Constants.COMMENT_REPLIES)) {
//            holder.reply.setVisibility(View.GONE);
//            holder.view_replies.setVisibility(View.GONE);
//
//            Functions.clickify(holder.user_comment, Functions.getAtRateTag(data.comment), () -> {
//                // do something
//                Toast.makeText(context, Functions.getAtRateTag(data.comment), Toast.LENGTH_SHORT).show();
//            });
//
//        } else {
//            holder.reply.setVisibility(View.VISIBLE);
//            if (data.comment_replies_count != null) {
//                //if this comment has replies show view replies else hide view replies
//                if (!data.comment_replies_count.equals("0")) {
//                    holder.view_replies.setVisibility(View.VISIBLE);
//                    holder.view_replies.setText("View all " + data.comment_replies_count + " replies");
//
//                    Comment reply_data = data.comment_replies.get(0);
//                    Glide.with(context).load(Variables.BASE_URL + reply_data.profile_pic).into(holder.user_dp_reply);
//                    holder.username_reply.setText(reply_data.user_name);
//                    holder.user_comment_reply.setText(reply_data.comment);
//                    holder.date_reply.setText(Functions.dateFormaty(reply_data.date));
//                    holder.reply_layout.setVisibility(View.VISIBLE);
//
//                    holder.reply_layout.setOnClickListener(view ->
//                            listener.onItemClickListener(data, view)
//                    );
//
//                } else {
//                    holder.view_replies.setVisibility(View.GONE);
//                }
//                holder.reply.setOnClickListener(view -> listener.onItemClickListener(data, view));
//                holder.view_replies.setOnClickListener(view -> listener.onItemClickListener(data, view));
//            }
//        }
//
//        holder.delete_comment.setOnLongClickListener(view -> {
//            listener.onItemClickListener(data, view);
//            return false;
//        });
//        holder.delete_comment.setOnClickListener(view -> {
//       Functions.ShowToast(context,"Hold to delete comment");
//        });
    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
private TextView text_comment, text_name, text_date,text_likes;
private CircleImageView user_dp;
private ImageView like;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

//            recyclerView = itemView.findViewById(R.id.recycler_view);
            user_dp = itemView.findViewById(R.id.user_dp);
            text_comment = itemView.findViewById(R.id.text_comment);
            text_date = itemView.findViewById(R.id.text_date);
            text_name = itemView.findViewById(R.id.text_name);
            like = itemView.findViewById(R.id.like);
            text_likes = itemView.findViewById(R.id.text_likes);


        }
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void onLikeUnlike(Comment data, ImageView like_img, TextView like_txt) {
        if (data.liked.equals("1")) {
            data.liked = "0";
            like_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_outline_thumb));
            data.comment_likes_count = "" + (Integer.parseInt(data.comment_likes_count) - 1);
        } else {
            data.liked = "1";
            like_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb));
            data.comment_likes_count = "" + (Integer.parseInt(data.comment_likes_count) + 1);
        }

        Animations.fade_in(context, like_img);
        like_txt.setText(data.comment_likes_count + " " + context.getString(R.string.likes));
    }


}
