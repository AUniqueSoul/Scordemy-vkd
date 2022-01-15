package com.laboontech.scordemy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.activities.Razorpay;
import com.laboontech.scordemy.databinding.BottomPremiumBinding;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Functions {
    public static void ShowToast(Context context, String toast) {
        Toast.makeText(context, "" + toast, Toast.LENGTH_SHORT).show();
    }

    public static String getAppVersion(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert packageInfo != null;
        return packageInfo.versionName;
    }
    public static void hideKeyboard(Activity activity, EditText edit_text) {
        //hide keyboard
        edit_text.clearFocus();
        InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert in != null;
        in.hideSoftInputFromWindow(edit_text.getWindowToken(), 0);
    }



    /**
     * Returns the date in relative time like "5 mins ago"
     *
     * @param date The date
     * @return {@link CharSequence} relative time
     */
    public static CharSequence timeDiff(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date startDate = new Date();
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            startDate = simpleDateFormat.parse(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DateUtils.getRelativeTimeSpanString(startDate.getTime(), calendar.getTimeInMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    public static CharSequence timeDiff(Long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return DateUtils.getRelativeTimeSpanString(new Date(milliseconds).getTime(), calendar.getTimeInMillis(), DateUtils.SECOND_IN_MILLIS);
    }


    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(Context context, String partName, String file_path) {
        File file;
        // use the FileUtils to get the actual file by uri
        file = new File(file_path);
//              file= FileUtils.getFile(context, Uri.parse(file_path));


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }



    public static void Scan_file(Context context,String filepath) {
        MediaScannerConnection.scanFile(context,
                new String[]{filepath},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {

                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    public static void openPdf(Context context, File file){
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+".provider",file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }



    public static String dateFormat(String date){

        Calendar current_cal = Calendar.getInstance();

        Calendar date_cal=Calendar.getInstance();

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date d = null;
        try {
            d = f.parse(date);
            date_cal.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long difference=(current_cal.getTimeInMillis()- date_cal.getTimeInMillis())/1000;

        if(difference<86400)
        {
            if(current_cal.get(Calendar.DAY_OF_YEAR)-date_cal.get(Calendar.DAY_OF_YEAR)==0) {

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                assert d != null;
                return sdf.format(d);
            }
            else
                return "yesterday";
        }

        else if(difference<172800){
            return "yesterday";
        }

        else
            return (difference/86400)+" day ago";




    }

    public static String GetSuffix(String value) {
        try {

            int count=Integer.parseInt(value);
            if (count < 1000) return "" + count;
            int exp = (int) (Math.log(count) / Math.log(1000));
            return String.format("%.1f %c",
                    count / Math.pow(1000, exp),
                    "kMGTPE".charAt(exp-1));
        }catch (Exception e){
            return value;
        }

    }


    public static String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }






    private static BottomSheetDialog bottomSheetDialog;
    private static BottomSheetBehavior bottomSheetBehavior;

    public static void showBottomSheet(Context context) {
        BottomPremiumBinding bottomSheetBinding = BottomPremiumBinding.inflate(LayoutInflater.from(context),null,false);
        View view = bottomSheetBinding.getRoot();
        bottomSheetDialog = new BottomSheetDialog(context, R.style.SheetDialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetBinding.close.setOnClickListener(v -> dismiss());
        bottomSheetBinding.subscribe.setOnClickListener(v -> {
            dismiss();
            Intent intent = new Intent(context, Razorpay.class);
            ((MainContentActivity)context).startActivityForResult(intent,Constants.REQUEST_SUBSCRIPTION_SUCCESS);
        });
    }

    public static void dismiss(){
        if (bottomSheetDialog.isShowing()){
            bottomSheetDialog.dismiss();
        }
    }



    public static String getYoutubeId(String url){
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        if (!TextUtils.isEmpty(url)){
            Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.

            if (matcher.find()) {
                return matcher.group();
            }
        }

        return "";
    }


    public static  void showSnackBar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
               .show();
    }




    public static String extension (String url){
        return url.substring(url.lastIndexOf("."));
    }

}

