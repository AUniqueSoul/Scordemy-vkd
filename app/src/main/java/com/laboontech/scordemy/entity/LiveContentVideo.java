package com.laboontech.scordemy.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveContentVideo  extends Base_Response {

    public String ls_id, ls_title, live_course_id, ls_active, ls_created_at;
    public List<LiveContentVideo> res;

    public String lv_id, lv_title, lv_image, lv_schedule_datetime, lv_youtube_url,
            lv_active, lv_created_at;
    public List<LiveContentVideo> Video_list;


}
