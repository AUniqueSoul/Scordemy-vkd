package com.laboontech.scordemy.utils;

import android.content.SharedPreferences;
import android.os.Environment;

import com.laboontech.scordemy.entity.Quiz;
import com.laboontech.scordemy.entity.Video;

import java.util.ArrayList;
import java.util.List;

public class Variables {

    public final static String DOMAIN = "https://scordemy.in/";
    private final static String PATH = "App/";
    public final static String BASE_URL = DOMAIN +PATH;
    public final static String api = "index.php?p=";
    public final static String IMAGE_PATH = BASE_URL + "AdminPanel/";
    public final static String my_shared_pref = "my_shared_pref";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public final static String name = "name";
    public final static String email = "email";
    public final static String phone = "phone";
    public static String user_id = "user_id";
    public final static String invite_code = "invite_code";
    public final static String fcm_token = "fcm_token";
    public final static String image = "image";




    public  static int  home_data_id=0;
    public  static int  subject_id;
    public  static int  medium_id;
    public  static int  chapter_id;
    public  static String back_title;
    public  static String class_name;
    public  static String sub_table_name;


    public static final String root= Environment.getExternalStorageDirectory().toString();
    public static final String app_showing_folder =root+"/.elscrmy/.12452ABHDNS/";

    public static Video videoData;

    //inserted data in quizQuestionsList from quiz list adapter
    public static  List<Quiz> quizQuestionsList = new ArrayList<>();

    public static String video_type = "video"; // video or feed
}
