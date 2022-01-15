package com.laboontech.scordemy.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.laboontech.scordemy.BuildConfig;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityHomeBinding;
import com.laboontech.scordemy.entity.Test;
import com.laboontech.scordemy.entity.User;
import com.laboontech.scordemy.fragments.FeedsFragment;
import com.laboontech.scordemy.fragments.HomeFragment;
import com.laboontech.scordemy.fragments.LiveClassesFragment;
import com.laboontech.scordemy.fragments.MyContentFragment;
import com.laboontech.scordemy.fragments.StoreFragment;
import com.laboontech.scordemy.fragments.TeachersFragment;
import com.laboontech.scordemy.fragments.TestInstructionFragment;
import com.laboontech.scordemy.fragments.TestsFragment;
import com.laboontech.scordemy.fragments.WalletFragment;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Helper;
import com.laboontech.scordemy.utils.Permissions;
import com.laboontech.scordemy.utils.SharedPreferenceUtil;
import com.laboontech.scordemy.utils.Variables;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.laboontech.scordemy.utils.Constants.VERIFY_PERMISSIONS_REQUEST;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivityTest";
    private ActivityHomeBinding binding;
    private Activity activity;
    private AppBarConfiguration mAppBarConfiguration;
    private User userData;
    private DrawerLayout drawer;
    private SharedPreferenceUtil sharedPreferenceUtil;
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;
    private RetrofitApi mService;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        activity = this;
        mService = RetrofitClient.getAPI();
        sharedPreferenceUtil = new SharedPreferenceUtil(activity);
        userData = Helper.getLoggedInUser(sharedPreferenceUtil);
        if (userData == null) {
            startActivity(new Intent(activity, LogRegActivity.class));
            finish();
        } else {
            user_id = userData.user_id;
            updateFcmToken();
        }


        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));
        } catch (Exception e) {
            Log.d(TAG, "onCreate: " + e.getMessage());
        }


        setFragment(new HomeFragment(), "home_fragment");
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,
//                R.id.nav_notifications, R.id.nav_ebook, R.id.nav_offline_videos, R.id.nav_share_app
//                , R.id.nav_about_us, R.id.nav_contact_us, R.id.nav_term_and_condition, R.id.nav_logout)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


        //for header
        View header = navigationView.getHeaderView(0);
        ImageView edit_profile = header.findViewById(R.id.edit_profile);
        TextView user_name = header.findViewById(R.id.user_name);

        ImageView user_dp = header.findViewById(R.id.user_dp);
        if (userData != null) {
            user_name.setText(userData.name);
            Glide.with(activity).load(Variables.IMAGE_PATH + userData.profile_pic).placeholder(R.drawable.user).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(user_dp);
        }

        edit_profile.setOnClickListener(view1 -> startActivity(new Intent(activity, EditProfileActivity.class)));


        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        if (!checkPermissions(Permissions.WRITE_EXTERNAL_STORAGE[0])) {
            verifyPermissions(Permissions.PERMISSIONS);
        }

        setNavigationViewListener();
    }

    private void updateFcmToken() {
        Variables.sharedPreferences = Objects.requireNonNull(getSharedPreferences(Variables.my_shared_pref, MODE_PRIVATE));
        mService.update_token(user_id, Variables.sharedPreferences.getString(Variables.fcm_token, null))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body() != null) {
                            if (response.body().code.equals(Constants.SUCCESS)) {
                                Log.d(TAG, "onResponse: token updated ");
                            } else {
                                Log.d(TAG, "onResponse: error " + response.body().error_msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            toolbar.setVisibility(View.VISIBLE);
                            setFragment(new HomeFragment(), "home_fragment");
                            Variables.video_type = "video";
                            break;
                        case R.id.nav_test:
                            setFragment(new TestsFragment(), "test_fragment");
                            break;

                        case R.id.nav_live:
                            toolbar.setVisibility(View.VISIBLE);
                            setFragment(new LiveClassesFragment(), "live_classes_fragment");
                            break;


                        case R.id.nav_store:
                            toolbar.setVisibility(View.VISIBLE);
                            setFragment(new StoreFragment(), "store_fragment");
                            break;
                        case R.id.nav_feeds:
                            Variables.video_type = "feed";
                            setFragment(new FeedsFragment(), "feeds_fragment");
                            toolbar.setVisibility(View.VISIBLE);
                            break;


                    }
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                startActivity(new Intent(activity, NotificationActivity.class));
                return false;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void setFragment(Fragment fragment, String name) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft = ft.replace(R.id.nav_host_fragment, fragment, name);
        ft.commit();
    }


    public void showInstructionPage(Test data) {
        Log.d(TAG, "showInstructionPage: data  " + data.test_sub_topic_id);
        TestInstructionFragment frag = new TestInstructionFragment();
        Bundle args = new Bundle();
        args.putInt("test_sub_topic_id", data.test_sub_topic_id);
        args.putString("title", data.title);
        args.putString("mark", data.marks);
        args.putString("question_count", data.question_count);
        args.putString("duration", data.duration);
        args.putString("instruction", data.instruction);
        frag.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                .hide(Objects.requireNonNull(getSupportFragmentManager().findFragmentByTag("test_fragment")))
                .add(R.id.nav_host_fragment, frag, "instruction_fragment")
                .addToBackStack(null)
                .commit();

    }

    /**
     * Permissions methods =========================================
     *
     * @param permissions
     */
    private void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verify permissions");

        ActivityCompat.requestPermissions(
                activity, permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * Check an array of permissions
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "verifyPermissionsArray: checking permissions array");

        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check a single permission ,if it is verified
     *
     * @param permission
     * @return
     */
    public boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checking permissions " + permission);


        int permissionRequest = ActivityCompat.checkSelfPermission(activity, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: Permission was not granted for " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: Permission was granted for " + permission);
            return true;
        }


    }


    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_wallet:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft = ft.replace(R.id.nav_host_fragment, new WalletFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.privacy_policy:
                Intent privacyIntent = new Intent(activity, PrivacyPolicyActivity.class);
                privacyIntent.putExtra("work", "privacy_policy");
                startActivity(privacyIntent);
                break;

            case R.id.nav_about_us:
                Intent aboutIntent = new Intent(activity, PrivacyPolicyActivity.class);
                aboutIntent.putExtra("work", "about_us");
                startActivity(aboutIntent);
                break;

            case R.id.nav_share_app:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out " + getString(R.string.app_name) + " at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.refund_policy:
                Intent refundIntent = new Intent(activity, PrivacyPolicyActivity.class);
                refundIntent.putExtra("work", "refund_policy");
                startActivity(refundIntent);
                break;

            case R.id.nav_term_and_condition:
                Intent termIntent = new Intent(activity, PrivacyPolicyActivity.class);
                termIntent.putExtra("work", "term_and_condition");
                startActivity(termIntent);
                break;

            case R.id.cancellation_policy:
                Intent cancellationIntent = new Intent(activity, PrivacyPolicyActivity.class);
                cancellationIntent.putExtra("work", "cancellation");
                startActivity(cancellationIntent);
                break;

            case R.id.nav_notifications:
                startActivity(new Intent(activity, NotificationActivity.class));
                break;

            case R.id.nav_logout:
                Helper.setLoggedInUser(sharedPreferenceUtil, null);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(activity, LogRegActivity.class));
                GoogleSignIn.getClient(
                        activity,
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();
                finish();
                break;

            case R.id.nav_youtube:
                openUrl(Constants.YOUTUBE);
                break;

            case R.id.nav_instagram:
                openUrl(Constants.INSTAGRAM);
                break;

            case R.id.nav_facebook:
                openUrl(Constants.FACEBOOK);
                break;

            case R.id.nav_teachers:
                setFragment(new TeachersFragment(), "teachers_fragment");
                break;

            case R.id.nav_my_content:
                setFragment(new MyContentFragment(), "my_purchases_fragment");
                break;

        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        Snackbar.make(binding.getRoot(), "Do you want to exit?", Snackbar.LENGTH_SHORT)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();

    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}