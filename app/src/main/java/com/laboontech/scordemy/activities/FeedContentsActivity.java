package com.laboontech.scordemy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.adapters.CommentAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityFeedContentsBinding;
import com.laboontech.scordemy.entity.Comment;
import com.laboontech.scordemy.entity.Feed;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.interfaces.IOnItemClickListener;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedContentsActivity extends AppCompatActivity implements View.OnClickListener, IOnItemClickListener {
    private static final String TAG = "FeedContentActivityT";
    private ActivityFeedContentsBinding binding;
    private Activity activity;
    private List<Comment> list;
    private CommentAdapter commentAdapter;
    // Disposal
    DisposableSingleObserver<Comment> disposableSingleObserver;
    DisposableCompletableObserver disposableCompletableObserver;

    //Retrofit api
    RetrofitApi mService;
    private boolean isMessageMax = false;
    private String message;
    private int feed_id;
    private String liked;
    private boolean commentBool, clickCommentBool;
    private String user_id;
    private int views;
    private User userData;
    private SharedPreferenceUtil sharedPreferenceUtil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedContentsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        mService = RetrofitClient.getAPI();
        sharedPreferenceUtil = new SharedPreferenceUtil(activity);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        if (user_id == null) {
            user_id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        }
        initBundleIntentData();
        binding.lytBack.setOnClickListener(this);
        binding.like.setOnClickListener(this);
        binding.comment.setOnClickListener(this);
        binding.post.setOnClickListener(this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initBundleIntentData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            feed_id = bundle.getInt("feed_id");
            String image = bundle.getString("image");
            String title = bundle.getString("title");
            String date = bundle.getString("date");
             message = bundle.getString("message");
            liked = bundle.getString("liked");
            commentBool = bundle.getBoolean("comment");
            views=bundle.getInt("views");
            Glide.with(activity).load(Variables.IMAGE_PATH+image).into(binding.imageView);
            Animations.rotate_clockwise(activity,binding.imageView);
            binding.webView.setBackgroundColor(Color.parseColor("#ffffff"));
            binding.webView.setFocusableInTouchMode(false);
            binding.webView.setFocusable(false);
            binding.webView.getSettings().setDefaultTextEncodingName("UTF-8");

            WebSettings webSettings = binding.webView.getSettings();
            Resources res = getResources();
            int fontSize = res.getInteger(R.integer.font_size);
            webSettings.setDefaultFontSize(fontSize);

            String mimeType = "text/html; charset=UTF-8";
            String encoding = "utf-8";
            String htmlText = message;

            String text = "<html><head>"
                    + "<style type=\"text/css\">body{color: #525252;}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";

            binding.webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);


            binding.title.setText(title);
            binding.date.setText(Functions.dateFormat(date));

          setLikeView();
          setCommentView();
          initCommentView();
          onCommentTextWatcher();
          updateViews();


        }
    }




    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.lyt_back:
                onBackPressed();
                break;


            case R.id.like:
                onLikeUnlike(binding.like);
                likeApi();
                break;

            case R.id.comment:
                setClickCommentView();
                break;

            case R.id.post:
                commentingApi();
                break;
        }
    }


    /**
     * All Api Calls methods
     */
    private void likeApi() {
        disposableCompletableObserver = mService.like_unlike_feed(feed_id, user_id, liked)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete:   liked done ");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
    }
    
    private void commentingApi(){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.post.setVisibility(View.GONE);
        disposableSingleObserver = mService.feed_comment(user_id, feed_id ,binding.inputComment.getText().toString().trim(), Functions.getCurrentDateTime())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Comment>() {
                    @Override
                    public void onSuccess(@NonNull Comment response) {
                        Log.d(TAG, "onSuccess: "+response.code);
                        if (response.code.equals(Constants.SUCCESS)){
                            Log.d(TAG, "onSuccess: commented on video ");
                            showAllComments(0);
                            binding.progressBar.setVisibility(View.GONE);
                            binding.post.setVisibility(View.VISIBLE);
                            binding.inputComment.setText("");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                    }
                });
    }

    private void showAllComments(int last_id) {
        disposableSingleObserver = mService.show_feed_comment(feed_id,last_id,userData.user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Comment>() {
                    @Override
                    public void onSuccess(@NonNull Comment comment) {
                        Log.d(TAG, "onSuccess: response code "+comment.code);
                        if (comment.code.equals(Constants.SUCCESS)){
                            list.clear();
                            list.addAll(comment.res);
                            commentAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                    }
                });
    }




    /**
     * Common functions methods
     *
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLikeView() {
        if (liked != null) {
            if (liked.equals("1")) {
                binding.like.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked));
            } else {
                binding.like.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private  void setCommentView(){
        setuserDataCommentBox();
        if (commentBool){
            binding.comment.setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_colored));
            binding.bottomBar.setVisibility(View.VISIBLE);
            binding.rvComment.setVisibility(View.VISIBLE);
        }else {
            binding.bottomBar.setVisibility(View.GONE);
            binding.rvComment.setVisibility(View.GONE);
            binding.comment.setImageDrawable(getResources().getDrawable(R.drawable.ic_comment));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private  void setClickCommentView(){
      setuserDataCommentBox();
        if (!clickCommentBool){
            binding.comment.setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_colored));
            binding.bottomBar.setVisibility(View.VISIBLE);
            binding.rvComment.setVisibility(View.VISIBLE);
        }else {
            binding.bottomBar.setVisibility(View.GONE);
            binding.rvComment.setVisibility(View.GONE);
            binding.comment.setImageDrawable(getResources().getDrawable(R.drawable.ic_comment));
        }
        clickCommentBool = !clickCommentBool;
    }

    private void setuserDataCommentBox() {
        binding.inputComment.setHint(getString(R.string.comment_as) + " " + userData.name);
        Glide.with(this).load(Variables.IMAGE_PATH+userData.profile_pic).placeholder(getResources().getDrawable(R.drawable.user)).into(binding.userDp);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void onLikeUnlike(ImageView like_img) {
        if (liked.equals("1")) {
            liked = "0";
            like_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
        } else {
            liked = "1";
            like_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked));
        }

        Animations.fade_in(activity, like_img);

    }

    private void onCommentTextWatcher(){
        binding.inputComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0) {
                    binding.post.setEnabled(false);
                    binding.post.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                } else {
                    binding.post.setTextColor(getResources().getColor(R.color.colorBlack));
                    binding.post.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void initCommentView(){
        list = new ArrayList<>();
        binding.rvComment.setHasFixedSize(true);
        binding.rvComment.setLayoutManager(new LinearLayoutManager(activity));
        commentAdapter = new CommentAdapter(activity,list,this);
        binding.rvComment.setAdapter(commentAdapter);
        showAllComments(0);
    }

    private void updateViews(){
        Log.d(TAG, "updateViews: feed_id_ "+feed_id);
        mService.updateFeedViews(feed_id,views+1)
                .enqueue(new Callback<Feed>() {
                    @Override
                    public void onResponse(Call<Feed> call, Response<Feed> response) {
                        Log.d(TAG, "onResponse: "+response.body().code);
                    }

                    @Override
                    public void onFailure(Call<Feed> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableSingleObserver!=null){
            disposableSingleObserver.dispose();
        }

        if (disposableCompletableObserver!=null){
            disposableCompletableObserver.dispose();
        }
    }

    @Override
    public void onItemClickListener(Comment item, View view) {

    }
}