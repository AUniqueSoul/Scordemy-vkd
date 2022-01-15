package com.laboontech.scordemy.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.laboontech.scordemy.entity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Helper {

    public static void saveProfileMe(SharedPreferenceUtil sharedPreferenceUtil, User userMe) {
        sharedPreferenceUtil.setStringPreference(Constants.KEY_USER_PROFILE, new Gson().toJson(userMe, new TypeToken<User>() {
        }.getType()));
        setLoggedInUser(sharedPreferenceUtil, userMe);
    }

    public static User getProfileMe(SharedPreferenceUtil sharedPreferenceUtil) {
        User toReturn = null;
        String savedUserString = sharedPreferenceUtil.getStringPreference(Constants.KEY_USER_PROFILE, null);
        if (savedUserString != null)
            toReturn = new Gson().fromJson(savedUserString, new TypeToken<User>() {
            }.getType());
        return toReturn;
    }

    public static void setLoggedInUser(SharedPreferenceUtil sharedPreferenceUtil, User user) {
        sharedPreferenceUtil.setStringPreference(Constants.USER, new Gson().toJson(user, new TypeToken<User>() {
        }.getType()));
    }

    public static User getLoggedInUser(SharedPreferenceUtil sharedPreferenceUtil) {
        String savedUserPref = sharedPreferenceUtil.getStringPreference(Constants.USER, null);
        if (savedUserPref != null)
            return new Gson().fromJson(savedUserPref, new TypeToken<User>() {
            }.getType());
        else return null;
    }





    /**
     * Opens a share intent with text and icon_picture
     *
     * @param context
     * @param itemview  View for which icon_picture has to be created
     * @param shareText Text to be shared
//     */
    public static void openShareIntent(Context context, @Nullable View itemview, String shareText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (itemview != null) {
            try {
                Uri imageUri = getImageUri(context, itemview, "postBitmap.jpeg");
                intent.setType("icon_picture/*");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (IOException e) {
                intent.setType("text/plain");
                e.printStackTrace();
            }
        } else {
            intent.setType("text/plain");
        }
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(intent, "Share Via:"));
    }


    /**
     * Creates icon_picture for the view passed, saves it to a temporary file and returns the {@link Uri}
     *
     * @param context
     * @param view     View for which icon_picture has to be created
     * @param fileName Name of the file to save the icon_picture
     * @return Image {@link Uri}
     * @throws IOException
     */
    private static Uri getImageUri(Context context, View view, String fileName) throws IOException {
        Bitmap bitmap = loadBitmapFromView(view);
        File pictureFile = new File(context.getExternalCacheDir(), fileName);
        FileOutputStream fos = new FileOutputStream(pictureFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        fos.close();
        return FileProvider.getUriForFile(
                context,
                context.getApplicationContext()
                        .getPackageName() + ".provider", pictureFile);
        //return Uri.parse("file://" + pictureFile.getAbsolutePath());
    }

    /**
     * Creates bitmap from the view supplied
     *
     * @param v View
     * @return {@link Bitmap}
     */
    private static Bitmap loadBitmapFromView(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            v.setDrawingCacheEnabled(true);
            return v.getDrawingCache();
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

}
