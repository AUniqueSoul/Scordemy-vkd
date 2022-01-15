package com.laboontech.scordemy.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category extends Base_Response {
    public String class_id, medium_id, subject_id, type_id, type, subject,image;

    @SerializedName("class")
    public String class_name;

    public List<Category> res;
}
