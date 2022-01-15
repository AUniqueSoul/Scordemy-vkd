package com.laboontech.scordemy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.laboontech.scordemy.utils.Functions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.laboontech.scordemy.activities.chatmanger.adapter.ChatAdapter;
import com.laboontech.scordemy.activities.chatmanger.models.ChatModel;
import com.laboontech.scordemy.activities.chatmanger.snaphelper.GravitySnapHelper;
import com.laboontech.scordemy.databinding.ActivityPracticeQuizBinding;
import com.laboontech.scordemy.databinding.ActivityYoutubeVideoBinding;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;

import java.util.ArrayList;

public class YoutubeVideoActivity extends AppCompatActivity {
    private static final String TAG = "YoutubeVideoActivity";
    private ActivityYoutubeVideoBinding binding;
    private Activity activity;
    private Firebase studyFiChatManger;
    private static String FIREBASE_DATABASE_URL = "https://studyfi-e5732.firebaseio.com/";
    private String thumbnail, title, sub_title, video_url, video_id;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private String user_id;
    private User userData;
    private ChatAdapter adapter;
    YouTubePlayer yt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = ActivityYoutubeVideoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            thumbnail = bundle.getString("thumbnail");
            title = bundle.getString("title");
            sub_title = bundle.getString("sub_title");
            video_url = bundle.getString("video_url");
            video_id = bundle.getString("video_id");
        }
        Log.d(TAG, "onCreate: video url "+video_url);
        Firebase.setAndroidContext(this);
        studyFiChatManger = new Firebase(FIREBASE_DATABASE_URL);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        assert userData != null;
        user_id = userData.user_id;
        getLifecycle().addObserver(binding.youtubePlayerView);
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(Functions.getYoutubeId(video_url), 0);
            }
        });
        adapter = new ChatAdapter(this, new ArrayList<>());
        binding.chatRecycler.setAdapter(adapter);
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.BOTTOM);
        snapHelper.attachToRecyclerView(binding.chatRecycler);
        ChatListner();

        binding.tvMobileNo.setText(userData.phone);
        Animations.blink(this, binding.tvMobileNo);


        binding.sendMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {

                    binding.sentMessageNow.setVisibility(View.VISIBLE);
                    Animations.fade_in(activity,binding.sentMessageNow);
                } else {
                    binding.sentMessageNow.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.sentMessageNow.setOnClickListener(v -> sendMessage());


        binding.youtubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                binding.chatlyt.setVisibility(View.GONE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                binding.chatlyt.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.youtubePlayerView.release();
    }

    void sendMessage() {
        if (!binding.sendMessage.getText().toString().trim().equals("")){
            String messageTime = String.valueOf(System.currentTimeMillis());
            ChatModel chatModel = new ChatModel(video_id, user_id, userData.name, Variables.IMAGE_PATH + userData.profile_pic, trimFun(binding.sendMessage.getText().toString()), messageTime);
            studyFiChatManger.child(video_id).child(messageTime).setValue(chatModel);
            binding.sendMessage.setText("");
            binding.chatRecycler.smoothScrollToPosition(adapter.getItemCount());
        }
    }

    public String trimFun(String str) {
        if (str != null) {
            return str.replaceAll("\\s+", " ").trim();
        } else {
            return "";
        }

    }

    public void ChatListner() {
        studyFiChatManger.child(video_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatModel model = dataSnapshot.getValue(ChatModel.class);
                adapter.AddChat(model);
                binding.chatRecycler.smoothScrollToPosition(adapter.getItemCount());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ChatModel model = dataSnapshot.getValue(ChatModel.class);
                adapter.UpdateChat(model);
                binding.chatRecycler.smoothScrollToPosition(adapter.getItemCount());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ChatModel model = dataSnapshot.getValue(ChatModel.class);
                adapter.DeleteChatHistoryFromFirebaseConsole(model);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.youtubePlayerView.isFullScreen()){
            binding.youtubePlayerView.exitFullScreen();
        }else{
            super.onBackPressed();
        }

    }
}