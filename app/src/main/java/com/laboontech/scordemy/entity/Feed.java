package com.laboontech.scordemy.entity;

import java.util.List;

public class Feed extends Base_Response {
    public String title, image, message, date, like_count, comment_count, liked, comment;
    public int id,views;
    public List<Feed> res;

}
