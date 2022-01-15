package com.laboontech.scordemy.api;


import com.laboontech.scordemy.entity.Book;
import com.laboontech.scordemy.entity.Category;
import com.laboontech.scordemy.entity.Chapter;
import com.laboontech.scordemy.entity.Classes;
import com.laboontech.scordemy.entity.CouponCode;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.entity.Feed;
import com.laboontech.scordemy.entity.Comment;
import com.laboontech.scordemy.entity.HomeData;
import com.laboontech.scordemy.entity.LiveContentVideo;
import com.laboontech.scordemy.entity.Medium;
import com.laboontech.scordemy.entity.Notes;
import com.laboontech.scordemy.entity.Notifications;
import com.laboontech.scordemy.entity.Quiz;
import com.laboontech.scordemy.entity.Setting;
import com.laboontech.scordemy.entity.SubCategory;
import com.laboontech.scordemy.entity.Subject;
import com.laboontech.scordemy.entity.Subscription;
import com.laboontech.scordemy.entity.Teachers;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.entity.Video;
import com.laboontech.scordemy.model.QuizResponse;
import com.laboontech.scordemy.model.savescore.ScoreResponse;
import com.laboontech.scordemy.utils.Variables;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApi {

    @FormUrlEncoded
    @POST(Variables.api + "sign_up")
    Single<User> sign_up(@Field("user_id") String user_id,
                         @Field("name") String name,
                         @Field("phone") String phone,
                         @Field("app_version") String app_version,
                         @Field("invite_code") String invite_code,
                         @Field("entered_referral_code") String entered_referral_code);



    @FormUrlEncoded
    @POST(Variables.api + "validate_invite_code")
    Call<User> validate_invite_code(@Field("entered_referral_code") String entered_referral_code);


    @FormUrlEncoded
    @POST(Variables.api + "check_user")
    Call<User> check_user(@Field("phone") String phone);

    @FormUrlEncoded
    @POST(Variables.api + "user_data")
    Single<User> user_data(@Field("user_id") String user_id);


    @GET(Variables.api + "home_data")
    Single<HomeData> home_data();

    @FormUrlEncoded
    @POST(Variables.api + "update_token")
    Call<User> update_token(@Field("user_id") String user_id,
                            @Field("token") String token);


    @FormUrlEncoded
    @POST(Variables.api + "notes")
    Single<Notes> notes(@Field("chapter_id") String chapter_id);


    @Multipart
    @POST(Variables.api + "update_user_dp")
    Single<User> update_user_dp(@Part MultipartBody.Part profile_pic, @Part("user_id") RequestBody user_id);

    @FormUrlEncoded
    @POST(Variables.api + "update_user_data")
    Single<User> update_user_data(@Field("user_id") String user_id,
                                  @Field("name") String name,
                                  @Field("email") String email,
                                  @Field("phone") String phone,
                                  @Field("guardian_phone") String guardian_phone,
                                  @Field("gender") String gender,
                                  @Field("city") String city,
                                  @Field("class_std") String class_std);

    @FormUrlEncoded
    @POST(Variables.api + "show_practice_quiz")
    Single<Quiz> show_practice_quiz(@Field("chapter_id") String chapter_id);


    @FormUrlEncoded
    @POST(Variables.api + "fetch_medium")
    Call<Medium> fetch_medium(@Field("home_id") String home_id);


    @FormUrlEncoded
    @POST(Variables.api + "fetch_class")
    Call<Classes> fetch_class(@Field("medium_id") String medium_id);


    @FormUrlEncoded
    @POST(Variables.api + "fetch_subject")
    Call<Subject> fetch_subject(@Field("class_id") String class_id);

    @FormUrlEncoded
    @POST(Variables.api + "fetch_subjects_other")
    Call<Subject> fetch_subjects_other(@Field("medium_id") String medium_id,
                                       @Field("home_id") String home_id);

    @FormUrlEncoded
    @POST(Variables.api + "fetch_chapter")
    Call<Chapter> fetch_chapter(@Field("subject_id") String subject_id);

    @FormUrlEncoded
    @POST(Variables.api + "fetch_chapter_other")
    Call<Chapter> fetch_chapter_other(@Field("home_id") String home_id);

    @GET(Variables.api + "fetch_ebooks")
    Call<Book> fetch_ebooks();

    @FormUrlEncoded
    @POST(Variables.api + "fetch_medium_ebook_wise")
    Call<Medium> fetch_medium_ebook_wise(@Field("ebook_id") String ebook_id);


    //---------------------------- Video Content ------------------------//
    @FormUrlEncoded
    @POST(Variables.api + "videos")
    Single<Video> videos(@Field("chapter_id") String chapter_id,
                         @Field("user_id") String user_id,
                         @Field("type") String type);

    @FormUrlEncoded
    @POST(Variables.api + "like_unlike_video")
    Completable like_unlike_video(@Field("video_id") int video_id,
                                  @Field("user_id") String user_id,
                                  @Field("action") String action);


    @FormUrlEncoded
    @POST(Variables.api + "show_all_comments")
    Single<Comment> show_all_comments(@Field("video_id") int video_id,
                                      @Field("last_id") int last_id,
                                      @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(Variables.api + "comment")
    Single<Comment> comment(@Field("user_id") String user_id,
                            @Field("video_id") int video_id,
                            @Field("comment") String comment,
                            @Field("date") String date);

    @FormUrlEncoded
    @POST(Variables.api + "like_unlike_comment")
    Completable like_unlike_comment(@Field("comment_id") int comment_id
            , @Field("user_id") String user_id
            , @Field("action") String action);


    //------------------- Feed Content -----------------------------//
    @FormUrlEncoded
    @POST(Variables.api + "feed")
    Single<Feed> feed(@Field("type") String type,
                      @Field("user_id") String user_id,
                      @Field("last_id") int last_id);

    @FormUrlEncoded
    @POST(Variables.api + "feed_comment")
    Single<Comment> feed_comment(@Field("user_id") String user_id,
                                 @Field("feed_id") int feed_id,
                                 @Field("comment") String comment,
                                 @Field("date") String date);

    @FormUrlEncoded
    @POST(Variables.api + "like_unlike_feed")
    Completable like_unlike_feed(@Field("feed_id") int feed_id
            , @Field("user_id") String user_id
            , @Field("action") String action);

    @FormUrlEncoded
    @POST(Variables.api + "show_feed_comment")
    Single<Comment> show_feed_comment(@Field("feed_id") int feed_id,
                                      @Field("last_id") int last_id,
                                      @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(Variables.api + "updateFeedViews")
    Call<Feed> updateFeedViews(@Field("feed_id") int feed_id,
                               @Field("views") int views);


    //------------------------------ Test List --------------------------------//
    @FormUrlEncoded
    @POST(Variables.api + "test")
    Single<Test> test(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(Variables.api + "show_online_quiz")
    Call<QuizResponse> getQuiz(@Field("subtopic_id") String subTopicId);

    @FormUrlEncoded
    @POST(Variables.api + "insert_test_score")
    Call<ScoreResponse> SubmitScore(@Field("user_id") String user_id, @Field("subtest_id") String subtest_id
            , @Field("completed") String completed, @Field("score") String score, @Field("accuracy") String accuracy
            , @Field("correct") String correct, @Field("wrong") String wrong, @Field("skipped") String skipped);


    //------------------------ Notification -----------------------//
    @GET(Variables.api + "notifications")
    Call<Notifications> notifications();


    //------------------ live videos ------------------------//
    @FormUrlEncoded
    @POST(Variables.api + "get_live_course")
    Call<Video> live_videos(@Field("user_id") String user_id);


    //---------------------------- NEW APIs--------------------- //
    @FormUrlEncoded
    @POST(Variables.api + "get_medium")
    Call<Medium> getMedium(@Field("home_data_id") String home_id);

    @FormUrlEncoded
    @POST(Variables.api + "get_class")
    Call<Category> getClass(@Field("medium_id") String medium_id);

    @FormUrlEncoded
    @POST(Variables.api + "get_subject")
    Call<Category> getSubjects(@Field("class_id") String class_id);

    @FormUrlEncoded
    @POST(Variables.api + "get_chapter")
    Call<SubCategory> getChapters(@Field("subject_id") String subject_id);


    @FormUrlEncoded
    @POST(Variables.api + "get_subscriptions")
    Call<Subscription> getSubscriptionPlans(@Field("content_id") String content_id,
                                            @Field("type") String type);

    @FormUrlEncoded
    @POST(Variables.api + "getRazorPayDetail")
    Call<Subscription> getRazorPayDetail(@Field("data") String data);


    @GET(Variables.api + "getEbooks")
    Call<Ebook> getEbooks();

    @FormUrlEncoded
    @POST(Variables.api + "getEbookClasses")
    Call<Ebook> getEbookClasses(@Field("content_id") String content_id);

    @FormUrlEncoded
    @POST(Variables.api + "getEbookStreams")
    Call<Ebook> getEbookStreams(@Field("content_id") String content_id);

    @FormUrlEncoded
    @POST(Variables.api + "getEbookPdf")
    Call<Ebook> getEbookPdf(@Field("content_id") String content_id);

    @FormUrlEncoded
    @POST(Variables.api + "getEbookBookSolutions")
    Call<Ebook> getEbookBookSolutions(@Field("content_id") String content_id);

    @FormUrlEncoded
    @POST(Variables.api + "getEbookYears")
    Call<Ebook> getEbookYears(@Field("content_id") String content_id);

    @FormUrlEncoded
    @POST(Variables.api + "createSubscriptionUser")
    Call<Subscription> createSubscriptionUser(@Field("user_id") String user_id,
                                              @Field("plan_id") String plan_id,
                                              @Field("content_id") String content_id,
                                              @Field("home_id") String home_id);

    @GET(Variables.api + "get_teachers")
    Call<Teachers> get_teachers();


    @FormUrlEncoded
    @POST(Variables.api + "checkMySubscriptionStatus")
    Call<Subscription> checkMySubscriptionStatus(@Field("user_id") String user_id,
                                                 @Field("content_id") String content_id,
                                                 @Field("home_id") String home_id);

    @FormUrlEncoded
    @POST(Variables.api + "get_my_score")
    Call<ScoreResponse> get_my_score(@Field("user_id") String user_id,
                                     @Field("subtest_id") int subtest_id);


    @FormUrlEncoded
    @POST(Variables.api + "get_live_content")
    Call<LiveContentVideo> getLiveContent(@Field("live_course_id") String live_course_id,
                                          @Field("type") String type);


    @FormUrlEncoded
    @POST(Variables.api + "validate_coupon")
    Call<CouponCode> applyCouponCode(@Field("type") int type,
                                     @Field("coupon_code") String coupon_code);


    @FormUrlEncoded
    @POST(Variables.api + "subscribeLiveTest")
    Call<Subscription> subscribeLiveTest(@Field("user_id") String user_id,
                                         @Field("content_id") int content_id,
                                         @Field("type") int type,
                                         @Field("coupon_id") String coupon_id);


    @GET(Variables.api + "get_setting")
    Call<Setting> get_setting();

}

