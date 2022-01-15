package com.laboontech.scordemy.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.laboontech.scordemy.R;

public class Animations {


    public static void leftToRight(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.enter_left_to_right);
        view.startAnimation(animation);
    }

    public static void rightToLeft(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.enter_right_to_left);
        view.startAnimation(animation);
    }

    public static void shake(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
        view.startAnimation(animation);
    }

    public static void bottomToTop(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.enter_bottom_to_top);
        view.startAnimation(animation);
    }

    public static void blink(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.blink);
        view.startAnimation(animation);
    }

    public static void zoom_in(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        view.startAnimation(animation);
    }

    public static void zoom_out(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
        view.startAnimation(animation);
    }

    public static void bounce(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
        view.startAnimation(animation);
    }

    public static void fade_in(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(animation);
    }

    public static void rotate_clockwise(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.clockwise_rotate);
        view.startAnimation(animation);
    }
}
