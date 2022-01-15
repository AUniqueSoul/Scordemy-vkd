package com.laboontech.scordemy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laboontech.scordemy.R;
import com.laboontech.scordemy.activities.MainContentActivity;
import com.laboontech.scordemy.activities.PracticeQuizActivity;
import com.laboontech.scordemy.entity.Quiz;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Constants;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.List;


public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.DataViewHolder> {

    private Context context;
    private List<Quiz> list;


    public QuizListAdapter(Context context, List<Quiz> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_list, parent, false);
        return new DataViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Quiz data = list.get(position);

        holder.title.setText(data.title);
        holder.questions_num.setText(data.question_count + " " + context.getString(R.string.questions));


        if (data.paid_type.equals(Constants.PAID)) {
            if (MainContentActivity.user_subscription_status.equals(Constants.ACTIVE)){
                holder.lock.setVisibility(View.GONE);
            }else{
                holder.lock.setVisibility(View.VISIBLE);
            }
        } else {
            holder.lock.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.paid_type.equals(Constants.PAID)) {
                    if (MainContentActivity.user_subscription_status.equals(Constants.ACTIVE)){
                        Intent intent = new Intent(context, PracticeQuizActivity.class);
                        Variables.quizQuestionsList = data.quiz_details;
                        intent.putExtra("title", data.title);
                        context.startActivity(intent);
                    }else{
                        Functions.showBottomSheet(context);
                    }
                } else {
                    Intent intent = new Intent(context, PracticeQuizActivity.class);
                    Variables.quizQuestionsList = data.quiz_details;
                    intent.putExtra("title", data.title);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView title, questions_num;
        private ImageView lock;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            questions_num = itemView.findViewById(R.id.questions_num);
            lock = itemView.findViewById(R.id.lock);
        }
    }

}
