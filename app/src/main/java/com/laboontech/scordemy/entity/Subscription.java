package com.laboontech.scordemy.entity;

import java.util.List;

public class Subscription extends Base_Response{
    public String subs_id, home_id ,  plan_type,  plan_price ,  description,
            razorpay_id,key_id ,name ,  image ,currency,theme_color ,status;

    //live test
    public int content_id, coupon_id;
    public String id,user_id , type,live_subscription;

public List<Subscription> res;
}
