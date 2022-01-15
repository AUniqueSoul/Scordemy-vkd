package com.laboontech.scordemy.interfaces;

import android.view.View;

import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.Video;

public interface IStorePurchaseListener {
void onTestSeriesPurchaseClick(Test data);
    void onLiveCoursePurchaseClick(Video data);
}
