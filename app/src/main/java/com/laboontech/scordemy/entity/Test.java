package com.laboontech.scordemy.entity;

import java.io.Serializable;
import java.util.List;

public class Test  extends Base_Response  {
    public int test_topic_id, test_sub_topic_id,user_subscribed_test;
    public String title, description,tests,  paid_type, marks,duration,tt_duration,instruction,tt_price;
    public List<Test> res;
    public List<Test> test_sub_topic_list;

    private int setViewType;

    public int getSetViewType() {
        return setViewType;
    }

    public void setSetViewType(int setViewType) {
        this.setViewType = setViewType;
    }

    public int quiz_title_id;
    public String question, image,tt_image, question_count;
    public List<Test> quiz_details;
}
