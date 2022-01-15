package com.laboontech.scordemy.entity;

import java.util.List;

public class Comment extends Base_Response {
    public List<Comment> res;
public String user_id, comment, date, name , profile_pic, liked,comment_likes_count;
public int video_id, last_id, comment_id, feed_id;
}
