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

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MainContentActivity;

import com.laboontech.scordemy.activities.ebooks.EbookActivity;
import com.laboontech.scordemy.entity.HomeData;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.HomeDataViewHolder> {
    private static final String TAG = "HomeDataAdapterTest";
    private Context context;
    private List<HomeData> list;

    public HomeDataAdapter(Context context, List<HomeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_data, parent, false);
        return new HomeDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDataViewHolder holder, int position) {
        HomeData data = list.get(position);
        Glide.with(context).load(Variables.IMAGE_PATH + data.image).into(holder.imageView);
        Animations.bottomToTop(context, holder.imageView);
        Log.d(TAG, "onBindViewHolder:home id "+data.home_data_id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View view) {


                if (data.home_data_id.equals(Constants.JEE) || data.home_data_id.equals(Constants.TET)
                || data.home_data_id.equals(Constants.APSC) || data.home_data_id.equals(Constants.NET)){
                  Functions.ShowToast(context,"Coming soon");
                }else
                if (data.home_data_id.equals(Constants.EBOOKS)){
                    Intent intent = new Intent(context, EbookActivity.class);
                    context.startActivity(intent);
                }else{
                    Variables.sharedPreferences = context.getSharedPreferences(Variables.my_shared_pref,MODE_PRIVATE);
                    Variables.editor = Variables.sharedPreferences.edit();
                    Variables.editor.putString(Variables.image,data.image);
                    Variables.editor.putString("home_id",data.home_data_id);
                    Variables.editor.putString(Constants.BACK_TITLE,data.title);
                    Variables.editor.apply();

                    Intent intent = new Intent(context, MainContentActivity.class);
                    context.startActivity(intent);
                }





            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HomeDataViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;

        public HomeDataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
        }
    }

}
