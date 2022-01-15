package com.laboontech.scordemy.interfaces;

import android.widget.ImageView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.laboontech.scordemy.entity.Ebook;
import com.laboontech.scordemy.entity.Notes;

public interface IPdfDownloadListener {
    void onPdfDownload(Notes response, NumberProgressBar progressBar, ImageView downloadImg);
    void onEbookPdfDownload(Ebook response, NumberProgressBar progressBar);
}
