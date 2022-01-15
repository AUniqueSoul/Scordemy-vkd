package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.activities.VideoActivity;
import com.laboontech.scordemy.entity.Notes;
import com.laboontech.scordemy.interfaces.IPdfDownloadListener;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.DataViewHolder> {
    private static final String TAG = "NotesAdapterTest";
    private Context context;
    private List<Notes> list;
    private IPdfDownloadListener listener;

    public NotesAdapter(Context context, List<Notes> list, IPdfDownloadListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notes, parent, false);
        return new DataViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Notes data = list.get(position);

        holder.title.setText(data.title);
        Animations.bounce(context, holder.itemView);
        Log.d(TAG, "onBindViewHolder: "+MainContentActivity.user_subscription_status);
        Log.d(TAG, "onBindViewHolder: "+data.paid_type);

        if (data.paid_type.equals(Constants.PAID)) {
            if (MainContentActivity.user_subscription_status.equals(Constants.ACTIVE)){
                holder.lock.setVisibility(View.GONE);
                holder.download.setVisibility(View.VISIBLE);
            }else{
                holder.lock.setVisibility(View.VISIBLE);
            }
        } else {
            holder.lock.setVisibility(View.GONE);
            holder.download.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            if (data.paid_type.equals(Constants.PAID)){
                if (MainContentActivity.user_subscription_status.equals(Constants.ACTIVE)){
                    Animations.blink(context, holder.download);
                    listener.onPdfDownload(data, holder.number_progress_bar, holder.download);
                }else{
                    Functions.showBottomSheet(context);
                }
            }else{
                Animations.blink(context, holder.download);
                listener.onPdfDownload(data, holder.number_progress_bar, holder.download);
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private NumberProgressBar number_progress_bar;
        private ImageView download, lock;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            number_progress_bar = itemView.findViewById(R.id.number_progress_bar);
            download = itemView.findViewById(R.id.download);
            lock = itemView.findViewById(R.id.lock);
        }
    }

}
