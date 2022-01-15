package com.laboontech.scordemy.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Quiz extends Base_Response {
    public int id, chapter_id;
    public String title, question_count;
    @SerializedName("q_paid")
    public String paid_type;
    public List<Quiz> res;

    public int quiz_title_id;
    public String question, image, option1, option2, option3, option4,answer,solution_image,solution_text;
    public List<Quiz> quiz_details;
}
