package com.laboontech.scordemy.entity;

public class questionmodel {
   private String question,optionA,optionB,optionC,optionD,answer,answerSelected = "null";
    private int pointawarded,skipped=0;

    public questionmodel(String question, String optionA, String optionB, String optionC, String optionD, String answer) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getPointawarded() {
        return pointawarded;
    }

    public void setPointawarded(int pointawarded) {
        this.pointawarded = pointawarded;
    }

    public String getAnswerSelected() {
        return answerSelected;
    }

    public void setAnswerSelected(String answerSelected) {
        this.answerSelected = answerSelected;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getAnswer() {
        return answer;
    }
}
