package com.laboontech.scordemy.activities.ebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.databinding.ActivityEbookPdfBinding;
import com.laboontech.scordemy.databinding.ActivityPdfViewerBinding;
import com.laboontech.scordemy.utils.Variables;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PdfViewerActivity extends AppCompatActivity {
    private static final String TAG = "PdfViewerActivity";
    private ActivityPdfViewerBinding binding;
    private Activity activity;
    private static String pdf_url, offline_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfViewerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        activity = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pdf_url = bundle.getString("pdf_url");
            offline_pdf = bundle.getString("offline_pdf");
            if (!TextUtils.isEmpty(offline_pdf)){
                showPdfOfflineMode(offline_pdf);
            }else {
                showPdf();
            }

            Log.d(TAG, "onCreate: " + Uri.parse(Variables.app_showing_folder+"Prelims28.pdf"));
        }

    }

    private void showPdf() {
        new AsyncTask<Void, Void, Void>() {

            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                @SuppressLint("StaticFieldLeak") InputStream input = null;
                try {
                    input = new URL(pdf_url).openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                binding.pdfView.fromStream(input)
                        .onRender(new OnRenderListener() {
                            @Override
                            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                binding.progressBar.setVisibility(View.GONE);
                            }
                        })
                        .load();
                return null;
            }
        }.execute();

    }


    private void showPdfOfflineMode(String offline_pdf) {
        binding.pdfView.fromFile(new File(offline_pdf))
//                .pages() // all pages are displayed by default
//                .enableSwipe(true) // allows to block changing pages using swipe
//                .swipeHorizontal(false)
//                .enableDoubletap(true)
//                .defaultPage(0)
//                // allows to draw something on the current page, usually visible in the middle of the screen
//                .onDraw(onDrawListener)
//                // allows to draw something on all pages, separately for every page. Called only for visible pages
//                .onDrawAll(onDrawListener)
//                .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
//                .onPageChange(onPageChangeListener)
//                .onPageScroll(onPageScrollListener)
//                .onError(onErrorListener)
//                .onPageError(onPageErrorListener)
//                .onRender(onRenderListener) // called after document is rendered for the first time
//                // called on single tap, return true if handled, false to toggle scroll handle visibility
//                .onTap(onTapListener)
//                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
//                .password(null)
//                .scrollHandle(null)
//                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
//                // spacing between pages in dp. To define spacing color, set view background
//                .spacing(0)
                .load();
                binding.progressBar.setVisibility(View.GONE);

    }


    // Some Listeners
    OnDrawListener onDrawListener = new OnDrawListener() {
        @Override
        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

        }
    };

    OnLoadCompleteListener onLoadCompleteListener = new OnLoadCompleteListener() {
        @Override
        public void loadComplete(int nbPages) {
            binding.progressBar.setVisibility(View.GONE);
        }
    };

    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageChanged(int page, int pageCount) {

        }
    };

    OnPageScrollListener onPageScrollListener = new OnPageScrollListener() {
        @Override
        public void onPageScrolled(int page, float positionOffset) {

        }
    };

    OnErrorListener onErrorListener = new OnErrorListener() {
        @Override
        public void onError(Throwable t) {
            Toast.makeText(activity, "Try Again ", Toast.LENGTH_SHORT).show();
        }
    };

    OnPageErrorListener onPageErrorListener = new OnPageErrorListener() {
        @Override
        public void onPageError(int page, Throwable t) {

        }
    };

    OnRenderListener onRenderListener = new OnRenderListener() {
        @Override
        public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
            Toast.makeText(activity, "Rendering Problem", Toast.LENGTH_SHORT).show();
        }
    };

    OnTapListener onTapListener = new OnTapListener() {
        @Override
        public boolean onTap(MotionEvent e) {
            return false;
        }
    };



}