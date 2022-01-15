package com.laboontech.scordemy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.activities.ebooks.PdfViewerActivity;
import com.laboontech.scordemy.adapters.NotesAdapter;
import com.laboontech.scordemy.api.RetrofitApi;
import com.laboontech.scordemy.api.RetrofitClient;
import com.laboontech.scordemy.databinding.FragmentPdfsBinding;
import com.laboontech.scordemy.databinding.FragmentVideoLectureBinding;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.entity.Notes;
import com.laboontech.scordemy.interfaces.IPdfDownloadListener;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.laboontech.scordemy.utils.Variables.chapter_id;
import static com.laboontech.scordemy.utils.Variables.home_data_id;


public class NotesFragment extends Fragment implements IPdfDownloadListener {

    private static final String TAG = "HomeFragmentTest";
    private Context context;
    private FragmentPdfsBinding binding;
    private DisposableSingleObserver<Notes> disposableSingleObserver;
    private RetrofitApi mService;
    private List<Notes> list;
    private NotesAdapter adapter;


    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPdfsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mService = RetrofitClient.getAPI();
        context = getContext();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list = new ArrayList<>();
        adapter = new NotesAdapter(context, list, this);
        binding.recyclerView.setAdapter(adapter);

        Variables.sharedPreferences = context.getSharedPreferences(Variables.my_shared_pref, Context.MODE_PRIVATE);
        initVideos();
        PRDownloader.initialize(context.getApplicationContext());
        return view;
    }

    private void initVideos() {
        disposableSingleObserver = mService.notes(MainContentActivity.chapter_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Notes>() {
                    @Override
                    public void onSuccess(Notes response) {

                        if (response.code.equals(Constants.SUCCESS)) {
                            list.clear();
                            list.addAll(response.res);
                            adapter.notifyDataSetChanged();
                            binding.progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        Functions.ShowToast(context, "error " + e.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposableSingleObserver!=null){
            disposableSingleObserver.dispose();
        }
    }



    @Override
    public void onPdfDownload(Notes response, NumberProgressBar progressBar, ImageView downloadImg) {
        String downloadUrl = Variables.IMAGE_PATH + response.pdf;
         String downloadFilePath = Variables.app_showing_folder;
         String downloadFileName = response.title + response.id + ".pdf";
        String filepath = downloadFilePath+downloadFileName;

        File file = new File(filepath);

        if (file.exists()){
            openPdfActivityOffline(filepath);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                Intent intent = new Intent(context, PdfViewerActivity.class);
                intent.putExtra("pdf_url",downloadUrl);
                context.startActivity(intent);
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
                    Functions.ShowToast(context, "Download Completed");
                    downloadImg.animate().cancel();
                    downloadImg.clearAnimation();
                    Functions.Scan_file(context,filepath);
                    openPdfActivityOffline(filepath);

                }

                @Override
                public void onError(Error error) {

                    Toast.makeText(context, "Error" + error.getServerErrorMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }


            });
        }
        }


    }

    @Override
    public void onEbookPdfDownload(Ebook response, NumberProgressBar progressBar) {

    }

    private void openPdfActivityOffline(String filepath) {
        Intent intent = new Intent(context, PdfViewerActivity.class);
        intent.putExtra("offline_pdf",filepath);
        startActivity(intent);
    }
}