package com.laboontech.scordemy.activities.report.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laboontech.scordemy.R;
import com.laboontech.scordemy.databinding.ViewSolutionItemBinding;
import com.laboontech.scordemy.model.ResItem;
import com.laboontech.scordemy.utils.Variables;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;

public class ViewSolutionAdapter extends RecyclerView.Adapter<ViewSolutionAdapter.ViewSolutionViewHolder> {
    private List<ResItem> resItems = new ArrayList<>();
    private Context context;
    private boolean isSubmited = true;
    private boolean equationFound = false;


    public ViewSolutionAdapter(Context context, List<ResItem> resItems) {
        this.context = context;
        this.resItems = resItems;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewSolutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewSolutionItemBinding binding = ViewSolutionItemBinding.inflate((LayoutInflater.from(context)),parent,false);
        return new ViewSolutionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewSolutionViewHolder holder, final int position) {
        holder.onBindidView(position, resItems.get(position));
    }

    @Override
    public int getItemCount() {
        return resItems.size();
    }


    class ViewSolutionViewHolder extends RecyclerView.ViewHolder {
        private ViewSolutionItemBinding binding;
        private Disposable disposable;

        public ViewSolutionViewHolder(@NonNull ViewSolutionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void onBindidView(final int position, final ResItem resItem) {


            if (TextUtils.isEmpty(resItem.getSolutionText()) || TextUtils.isEmpty(resItem.getSolutionImage())){
                binding.rlSolution.setVisibility(View.VISIBLE);
           binding.solutionTxt.setText(resItem.getSolutionText());
                binding.solutionTxt.setText(resItem.getSolutionText());
                Glide.with(context).load(Variables.IMAGE_PATH + resItem.getSolutionImage()).into(binding.solutionImage);
            }else{
                binding.rlSolution.setVisibility(View.GONE);
            }

            binding.questionOrder.setText(String.valueOf((position+1)+"/"+resItems.size()));
            binding.questionText.setText(resItem.getQuestion());
            binding.answer1.setText("A) "+resItem.getOption1());
            binding.answer2.setText("B) "+resItem.getOption2());
            binding.answer3.setText("C) "+resItem.getOption3());
            binding.answer4.setText("D) "+resItem.getOption4());
            Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImg_option1()).into(binding.imgOption1);
            Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImg_option2()).into(binding.imgOption2);
            Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImg_option3()).into(binding.imgOption3);
            Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImg_option4()).into(binding.imgOption4);
            if (resItem.getImage() != null && !resItem.getImage().isEmpty()) {
                binding.quizImg.setVisibility(View.VISIBLE);
                Glide.with(context).load(Variables.IMAGE_PATH+resItem.getImage()).into(binding.quizImg);
            } else {
                binding.quizImg.setVisibility(View.GONE);
            }
            if (resItem.getSelectQuestion() != null && !resItem.getSelectQuestion().isEmpty() && !resItem.isSkipQuestion()&& isSubmited) {
                switch (resItem.getAnswer().toLowerCase()){
                        case "a":{
                            binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                                if ("a".equalsIgnoreCase(resItem.getSelectQuestion().trim())) {
                                    binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                                    binding.answer1txt.setVisibility(View.VISIBLE);
                                    binding.answer1txt.setText("Well Done");
                                    binding.answer2txt.setVisibility(View.GONE);
                                    binding.answer3txt.setVisibility(View.GONE);
                                    binding.answer4txt.setVisibility(View.GONE);
                                    binding.answer1.setTextColor(Color.BLACK);
                                    binding.answer2.setTextColor(Color.BLACK);
                                    binding.answer3.setTextColor(Color.BLACK);
                                    binding.answer4.setTextColor(Color.BLACK);
                                }else  if ("b".equalsIgnoreCase(resItem.getSelectQuestion().trim())) {
                                    binding.answerLay2.setBackgroundResource(R.drawable.question_select_red);
                                    binding.answer1txt.setVisibility(View.VISIBLE);
                                    binding.answer1txt.setText("You Missed");
                                    binding.answer2txt.setVisibility(View.VISIBLE);
                                    binding.answer2txt.setText("You Marked");
                                    binding.answer3txt.setVisibility(View.GONE);
                                    binding.answer4txt.setVisibility(View.GONE);
                                    binding.answer1.setTextColor(Color.BLACK);
                                    binding.answer2.setTextColor(Color.WHITE);
                                    binding.answer3.setTextColor(Color.BLACK);
                                    binding.answer4.setTextColor(Color.BLACK);
                                }else if ("c".equalsIgnoreCase(resItem.getSelectQuestion().trim())) {
                                    binding.answer3lay.setBackgroundResource(R.drawable.question_select_red);
                                    binding.answer1txt.setVisibility(View.VISIBLE);
                                    binding.answer1txt.setText("You Missed");
                                    binding.answer2txt.setVisibility(View.GONE);
                                    binding.answer3txt.setVisibility(View.VISIBLE);
                                    binding.answer3txt.setText("You Marked");
                                    binding.answer4txt.setVisibility(View.GONE);
                                    binding.answer1.setTextColor(Color.BLACK);
                                    binding.answer2.setTextColor(Color.BLACK);
                                    binding.answer3.setTextColor(Color.WHITE);
                                    binding.answer4.setTextColor(Color.BLACK);
                                }else if ("d".equalsIgnoreCase(resItem.getSelectQuestion().trim())) {
                                    binding.answer4lay.setBackgroundResource(R.drawable.question_select_red);
                                    binding.answer1txt.setVisibility(View.VISIBLE);
                                    binding.answer1txt.setText("You Missed");
                                    binding.answer2txt.setVisibility(View.GONE);
                                    binding.answer3txt.setVisibility(View.GONE);
                                    binding.answer4txt.setVisibility(View.VISIBLE);
                                    binding.answer4txt.setText("You Marked");
                                    binding.answer1.setTextColor(Color.BLACK);
                                    binding.answer2.setTextColor(Color.BLACK);
                                    binding.answer3.setTextColor(Color.BLACK);
                                    binding.answer4.setTextColor(Color.WHITE);
                                }
                            break;
                        }
                        case "b":{
                            binding.answerLay2.setBackgroundResource(R.drawable.question_select_green);
                            if ("a".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1txt.setVisibility(View.VISIBLE);
                                binding.answer1txt.setText("You Marked");
                                binding.answer2txt.setText("You Missed");
                                binding.answer2txt.setVisibility(View.VISIBLE);
                                binding.answer3txt.setVisibility(View.GONE);
                                binding.answer4txt.setVisibility(View.GONE);
                                binding.answer1.setTextColor(Color.WHITE);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else  if ("b".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay2.setBackgroundResource(R.drawable.question_select_green);
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer2txt.setText("Well Done");
                                binding.answer2txt.setVisibility(View.VISIBLE);
                                binding.answer3txt.setVisibility(View.GONE);
                                binding.answer4txt.setVisibility(View.GONE);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("c".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer3lay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer2txt.setText("You Missed");
                                binding.answer2txt.setVisibility(View.VISIBLE);
                                binding.answer3txt.setVisibility(View.VISIBLE);
                                binding.answer3txt.setText("You Marked");
                                binding.answer4txt.setVisibility(View.GONE);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.WHITE);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("d".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer4lay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer2txt.setText("You Missed");
                                binding.answer2txt.setVisibility(View.VISIBLE);
                                binding.answer3txt.setVisibility(View.GONE);
                                binding.answer4txt.setVisibility(View.VISIBLE);
                                binding.answer4txt.setText("You Marked");
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.WHITE);
                            }
                            break;
                        }
                        case "c":{
                            binding.answer3lay.setBackgroundResource(R.drawable.question_select_green);
                            if ("a".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1txt.setVisibility(View.VISIBLE);
                                binding.answer1txt.setText("You Marked");
                                binding.answer3txt.setText("you Missed ");
                                binding.answer3txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setVisibility(View.GONE);
                                binding.answer4txt.setVisibility(View.GONE);
                                binding.answer1.setTextColor(Color.WHITE);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else  if ("b".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay2.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer3txt.setText("you Missed ");
                                binding.answer3txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setText("You Marked");
                                binding.answer4txt.setVisibility(View.GONE);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.WHITE);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("c".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer3lay.setBackgroundResource(R.drawable.question_select_green);
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer3txt.setText("Well Done");
                                binding.answer3txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setVisibility(View.GONE);
                                binding.answer4txt.setVisibility(View.GONE);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("d".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer4lay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer3txt.setText("you Missed ");
                                binding.answer3txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setVisibility(View.GONE);
                                binding.answer4txt.setVisibility(View.VISIBLE);
                                binding.answer4txt.setText("You Marked");
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.WHITE);
                            }
                            break;
                        }
                        case "d":{
                            binding.answer4lay.setBackgroundResource(R.drawable.question_select_green);
                            if ("a".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answerLay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1txt.setVisibility(View.VISIBLE);
                                binding.answer1txt.setText("You Marked");
                                binding.answer4txt.setText("You Missed");
                                binding.answer4txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setVisibility(View.GONE);
                                binding.answer3txt.setVisibility(View.GONE);
                                binding.answer1.setTextColor(Color.WHITE);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else  if ("b".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer4txt.setText("You Missed");
                                binding.answer4txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setText("You Marked");
                                binding.answer3txt.setVisibility(View.GONE);
                                binding.answerLay2.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.WHITE);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("c".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer3lay.setBackgroundResource(R.drawable.question_select_red);
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer4txt.setText("You Missed");
                                binding.answer4txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setVisibility(View.GONE);
                                binding.answer3txt.setVisibility(View.VISIBLE);
                                binding.answer3txt.setText("You Marked");
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.WHITE);
                                binding.answer4.setTextColor(Color.BLACK);
                            }else if ("d".equalsIgnoreCase(resItem.getSelectQuestion())) {
                                binding.answer4lay.setBackgroundResource(R.drawable.question_select_green);
                                binding.answer1txt.setVisibility(View.GONE);
                                binding.answer4txt.setText("Well Done");
                                binding.answer4txt.setVisibility(View.VISIBLE);
                                binding.answer2txt.setVisibility(View.GONE);
                                binding.answer3txt.setVisibility(View.GONE);
                                binding.answer1.setTextColor(Color.BLACK);
                                binding.answer2.setTextColor(Color.BLACK);
                                binding.answer3.setTextColor(Color.BLACK);
                                binding.answer4.setTextColor(Color.BLACK);
                            }
                            break;
                        }
                }
            }
            else {
                switch (resItem.getAnswer().toLowerCase()){
                    case "a":{
                        binding.answerLay.setBackgroundResource(R.drawable.question_select_green);
                        binding.answerLay2.setBackgroundResource(R.drawable.bg_white);
                        binding.answer3lay.setBackgroundResource(R.drawable.bg_white);
                        binding.answer4lay.setBackgroundResource(R.drawable.bg_white);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        binding.answer4txt.setVisibility(View.GONE);
                        binding.answer1txt.setText("You Missed");
                        binding.answer1txt.setVisibility(View.VISIBLE);
                        binding.answer2txt.setVisibility(View.GONE);
                        binding.answer3txt.setVisibility(View.GONE);
                        break;
                    }
                    case "b":{
                        binding.answerLay2.setBackgroundResource(R.drawable.question_select_green);
                        binding.answerLay.setBackgroundResource(R.drawable.bg_white);
                        binding.answer3lay.setBackgroundResource(R.drawable.bg_white);
                        binding.answer4lay.setBackgroundResource(R.drawable.bg_white);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        binding.answer4txt.setVisibility(View.GONE);
                        binding.answer2txt.setText("You Missed");
                        binding.answer2txt.setVisibility(View.VISIBLE);
                        binding.answer1txt.setVisibility(View.GONE);
                        binding.answer3txt.setVisibility(View.GONE);
                        break;
                    }
                    case "c":{
                        binding.answer3lay.setBackgroundResource(R.drawable.question_select_green);
                        binding.answerLay.setBackgroundResource(R.drawable.bg_white);
                        binding.answerLay2.setBackgroundResource(R.drawable.bg_white);
                        binding.answer4lay.setBackgroundResource(R.drawable.bg_white);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        binding.answer4txt.setVisibility(View.GONE);
                        binding.answer3txt.setText("You Missed");
                        binding.answer3txt.setVisibility(View.VISIBLE);
                        binding.answer2txt.setVisibility(View.GONE);
                        binding.answer1txt.setVisibility(View.GONE);
                        break;
                    }
                    case "d":{
                        binding.answer4lay.setBackgroundResource(R.drawable.question_select_green);
                        binding.answerLay.setBackgroundResource(R.drawable.bg_white);
                        binding.answerLay2.setBackgroundResource(R.drawable.bg_white);
                        binding.answer3lay.setBackgroundResource(R.drawable.bg_white);
                        binding.answer1.setTextColor(Color.BLACK);
                        binding.answer2.setTextColor(Color.BLACK);
                        binding.answer3.setTextColor(Color.BLACK);
                        binding.answer4.setTextColor(Color.BLACK);
                        binding.answer1txt.setVisibility(View.GONE);
                        binding.answer4txt.setText("You Missed");
                        binding.answer4txt.setVisibility(View.VISIBLE);
                        binding.answer2txt.setVisibility(View.GONE);
                        binding.answer3txt.setVisibility(View.GONE);
                        break;
                    }

                }

            }
        }
    }


}
