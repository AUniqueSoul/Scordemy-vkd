package com.laboontech.scordemy.activities.newTest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.databinding.OnlineItemQuizBinding;
import com.laboontech.scordemy.model.ResItem;
import com.laboontech.scordemy.utils.Animations;
import com.laboontech.scordemy.utils.Functions;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class OnlineQuizAdapter extends RecyclerView.Adapter<OnlineQuizAdapter.QuizViewHolder> {
    private List<ResItem> resItems = new ArrayList<>();
    private Context context;
    private boolean isSubmited = false;
    private boolean equationFound = false;
    private int i = 0;
    private NoQuestionSelected noQuestionSelected;
    private int pos = 0;

    public OnlineQuizAdapter(Context context, NoQuestionSelected noQuestionSelected) {
        this.context = context;
        this.noQuestionSelected = noQuestionSelected;
    }

    public void UpdateList(List<ResItem> resItems) {
        this.resItems = resItems;
        notifyDataSetChanged();
    }

    public void UpdateSubmit(boolean isSubmited) {
        this.isSubmited = isSubmited;
        for (i = 0; i < resItems.size(); i++) {
            if (resItems.get(i).getSelectQuestion() != null) {
                equationFound = true;
                break;
            }
        }
        if (isSubmited && !equationFound) {
            noQuestionSelected.NoQuestionSelected();
        } else {
            noQuestionSelected.getResult(resItems);

        }
    }

    public int getCurrenQuiz() {
        return pos;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OnlineItemQuizBinding binding = OnlineItemQuizBinding.inflate((LayoutInflater.from(context)),parent,false);
        return new QuizViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, final int position) {
        holder.onBindidView(position, resItems.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return resItems.size();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        private OnlineItemQuizBinding binding;
        private Disposable disposable;

        public QuizViewHolder(@NonNull OnlineItemQuizBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBindidView(final int position, final ResItem resItem) {
            if(position<10){
                binding.questionnumber.setText("0"+String.valueOf(position + 1) + " of " + resItems.size());
            }else{
                binding.questionnumber.setText(String.valueOf(position+1)+ " of " + resItems.size());
            }

            if (resItem.getImage() != null && !resItem.getImage().isEmpty()) {
                binding.quizImg.setVisibility(View.VISIBLE);
                Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImage()).into(binding.quizImg);
            } else {
                binding.quizImg.setVisibility(View.GONE);
            }
            binding.questionText.setText(resItem.getQuestion());
            binding.answer1.setText("A) " + resItem.getOption1());
            binding.answer2.setText("B) " + resItem.getOption2());
            binding.answer3.setText("C) " + resItem.getOption3());
            binding.answer4.setText("D) " + resItem.getOption4());

            Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImg_option1()).into(binding.imgOption1);
            Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImg_option2()).into(binding.imgOption2);
            Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImg_option3()).into(binding.imgOption3);
            Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImg_option4()).into(binding.imgOption4);


            binding.answerLay.setOnClickListener(v -> {
                resItem.setSelectQuestion("a");
                resItem.setSkipQuestion(false);
                binding.answerLay.setBackgroundResource(R.drawable.question_select_blue);
                Animations.fade_in(context, binding.answerLay);
                binding.answerLay2.setBackgroundResource(R.drawable.bg_white);
                binding.answer3lay.setBackgroundResource(R.drawable.bg_white);
                binding.answer4lay.setBackgroundResource(R.drawable.bg_white);
                binding.answer1.setTextColor(Color.WHITE);
                binding.answer2.setTextColor(Color.BLACK);
                binding.answer3.setTextColor(Color.BLACK);
                binding.answer4.setTextColor(Color.BLACK);
                if ("a".equalsIgnoreCase(resItem.getAnswer().toLowerCase())) {
                    resItem.setCorrectAnswerSelected(true);
                } else {
                    resItem.setCorrectAnswerSelected(false);
                }
            });
            binding.answerLay2.setOnClickListener(v -> {
                resItem.setSelectQuestion("b");
                resItem.setSkipQuestion(false);
                binding.answerLay2.setBackgroundResource(R.drawable.question_select_blue);
                Animations.fade_in(context, binding.answerLay2);
                binding.answerLay.setBackgroundResource(R.drawable.bg_white);
                binding.answer3lay.setBackgroundResource(R.drawable.bg_white);
                binding.answer4lay.setBackgroundResource(R.drawable.bg_white);
                binding.answer1.setTextColor(Color.BLACK);
                binding.answer2.setTextColor(Color.WHITE);
                binding.answer3.setTextColor(Color.BLACK);
                binding.answer4.setTextColor(Color.BLACK);
                if ("b".equalsIgnoreCase(resItem.getAnswer().toLowerCase())) {
                    resItem.setCorrectAnswerSelected(true);
                } else {
                    resItem.setCorrectAnswerSelected(false);
                }
            });
            binding.answer3lay.setOnClickListener(v -> {
                resItem.setSelectQuestion("c");
                resItem.setSkipQuestion(false);
                binding.answer3lay.setBackgroundResource(R.drawable.question_select_blue);
                Animations.fade_in(context, binding.answer3lay);
                binding.answerLay.setBackgroundResource(R.drawable.bg_white);
                binding.answerLay2.setBackgroundResource(R.drawable.bg_white);
                binding.answer4lay.setBackgroundResource(R.drawable.bg_white);
                binding.answer1.setTextColor(Color.BLACK);
                binding.answer2.setTextColor(Color.BLACK);
                binding.answer3.setTextColor(Color.WHITE);
                binding.answer4.setTextColor(Color.BLACK);
                if ("c".equalsIgnoreCase(resItem.getAnswer().toLowerCase())) {
                    resItem.setCorrectAnswerSelected(true);
                } else {
                    resItem.setCorrectAnswerSelected(false);
                }
            });
            binding.answer4lay.setOnClickListener(v -> {
                resItem.setSelectQuestion("d");
                resItem.setSkipQuestion(false);
                binding.answer4lay.setBackgroundResource(R.drawable.question_select_blue);
                Animations.fade_in(context, binding.answer4lay);
                binding.answerLay.setBackgroundResource(R.drawable.bg_white);
                binding.answerLay2.setBackgroundResource(R.drawable.bg_white);
                binding.answer3lay.setBackgroundResource(R.drawable.bg_white);
                binding.answer1.setTextColor(Color.BLACK);
                binding.answer2.setTextColor(Color.BLACK);
                binding.answer3.setTextColor(Color.BLACK);
                binding.answer4.setTextColor(Color.WHITE);
                if ("d".equalsIgnoreCase(resItem.getAnswer().toLowerCase())) {
                    resItem.setCorrectAnswerSelected(true);
                } else {
                    resItem.setCorrectAnswerSelected(false);
                }
            });


        }
    }

    public interface NoQuestionSelected {
        void NoQuestionSelected();

        void getResult(List<ResItem> resItems);

    }
}
