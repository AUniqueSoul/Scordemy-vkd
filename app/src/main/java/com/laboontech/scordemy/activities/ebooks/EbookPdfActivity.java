package com.laboontech.scordemy.activities.ebooks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.laboontech.scordemy.activities.ebooks.adapters.EbookPdfAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.ActivityEbookPdfBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.entity.Notes;
import com.laboontech.scordemy.interfaces.IPdfDownloadListener;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EbookPdfActivity extends AppCompatActivity  {
    private static final String TAG = "EbookPdfActivity";
    private Activity activity;
    private ActivityEbookPdfBinding binding;
    private RetrofitApi mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEbookPdfBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        mService = RetrofitClient.getAPI();
        binding.back.setOnClickListener(v -> onBackPressed());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            binding.heading.setText(bundle.getString("heading"));
            mService.getEbookPdf(bundle.getString("prefix")+bundle.getString("id"))
                    .enqueue(new Callback<Ebook>() {
                        @Override
                        public void onResponse(@NonNull Call<Ebook> call, @NonNull Response<Ebook> response) {
                            if (response.body() != null){
                                if (response.body().code.equals(Constants.SUCCESS)) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    if (response.body().res.size()>0){
                                        binding.recyclerView.setAdapter(new EbookPdfAdapter(response.body().res, new IPdfDownloadListener() {
                                            @Override
                                            public void onPdfDownload(Notes response, NumberProgressBar progressBar, ImageView downloadImg) {

                                            }

                                            @Override
                                            public void onEbookPdfDownload(Ebook response, NumberProgressBar progressBar) {
                                                Log.d(TAG, "onEbookPdfDownload: "+response.ep_pdf);
                                                String downloadUrl =  response.ep_pdf;
                                                String downloadFilePath = Variables.app_showing_folder;
                                                String downloadFileName = response.ep_title + response.ep_id + ".pdf";
                                                String filepath = downloadFilePath+downloadFileName;

                                                File file = new File(filepath);

                                                if (file.exists()){
                                                    openPdfActivityOffline(filepath);
//                                                    Functions.openPdf(activity,file);
                                                }else{
                                                    DownloadRequest prDownloader = PRDownloader.download(downloadUrl,downloadFilePath,downloadFileName)
                                                            .build()
                                                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                                                @Override
                                                                public void onStartOrResume() {
                                                                    progressBar.setVisibility(View.VISIBLE);
                                                                }
                                                            })
                                                            .setOnPauseListener(new OnPauseListener() {
                                                                @Override
                                                                public void onPause() {

                                                                }
                                                            })
                                                            .setOnCancelListener(new OnCancelListener() {
                                                                @Override
                                                                public void onCancel() {

                                                                }
                                                            })
                                                            .setOnProgressListener(new OnProgressListener() {
                                                                @Override
                                                                public void onProgress(Progress progress) {

                                                                    int prog = (int) ((progress.currentBytes * 100) / progress.totalBytes);
                                                                    progressBar.setProgress(prog);

                                                                }
                                                            });


                                                    prDownloader.start(new OnDownloadListener() {
                                                        @Override
                                                        public void onDownloadComplete() {
                                                            progressBar.setVisibility(View.GONE);
                                                            Functions.ShowToast(activity, "Download Completed");
                                                            Functions.Scan_file(activity,filepath);
                                                            try {
                                                                encrypt(file);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            } catch (NoSuchAlgorithmException e) {
                                                                e.printStackTrace();
                                                            } catch (NoSuchPaddingException e) {
                                                                e.printStackTrace();
                                                            } catch (InvalidKeyException e) {
                                                                e.printStackTrace();
                                                            }
//                                                            Functions.openPdf(activity,file);
                                                            openPdfActivityOffline(filepath);
                                                        }

                                                        @Override
                                                        public void onError(Error error) {
                                                            Toast.makeText(activity, "Error" + error.getServerErrorMessage(), Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }



                                                    });
                                                }
                                            }
                                        }));

                                    }else{
                                        Functions.ShowToast(activity,"No Data Found");
                                    }
                                }else{
                                    Functions.ShowToast(activity, response.body().error_msg);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Ebook> call, @NonNull Throwable t) {
                            Functions.ShowToast(activity,t.getMessage());
                        }
                    });
        }
    }

    private void openPdfActivityOffline(String filepath) {
        Intent intent = new Intent(activity,PdfViewerActivity.class);
        intent.putExtra("offline_pdf",filepath);
        startActivity(intent);
    }


    static void encrypt(File file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        // Here you read the cleartext.
        FileInputStream fis = new FileInputStream("data/cleartext");
        // This stream write the encrypted text. This stream will be wrapped by another stream.
        FileOutputStream fos = new FileOutputStream("data/encrypted");

        // Length is 16 byte
        // Careful when taking user input!!! https://stackoverflow.com/a/3452620/1188357
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
    }

}