package com.laboontech.scordemy.model;

import com.google.gson.annotations.SerializedName;

public class ResItem{

	@SerializedName("image")
	private String image;

	@SerializedName("question")
	private String question;

	@SerializedName("answer")
	private String answer;

	@SerializedName("solution_text")
	private String solutionText;

	@SerializedName("option3")
	private String option3;

	@SerializedName("option4")
	private String option4;

	@SerializedName("option1")
	private String option1;

	@SerializedName("id")
	private String id;

	@SerializedName("option2")
	private String option2;


	@SerializedName("img_option1")
	private String img_option1;

	@SerializedName("img_option2")
	private String img_option2;

	@SerializedName("img_option3")
	private String img_option3;

	@SerializedName("img_option4")
	private String img_option4;


	public String getImg_option1() {
		return img_option1;
	}

	public String getImg_option2() {
		return img_option2;
	}

	public String getImg_option3() {
		return img_option3;
	}

	public String getImg_option4() {
		return img_option4;
	}

	@SerializedName("solution_image")
	private String solutionImage;


	private boolean isCorrectAnswerSelected;

	private String selectQuestion;

	private boolean isSkipQuestion=true;

	private boolean isSelectQuestion=false;

	public boolean isCorrectAnswerSelected() {
		return isCorrectAnswerSelected;
	}

	public void setCorrectAnswerSelected(boolean correctAnswerSelected) {
		isCorrectAnswerSelected = correctAnswerSelected;
	}

	public String getSelectQuestion() {
		return selectQuestion;
	}

	public void setSelectQuestion(String selectQuestion) {
		this.selectQuestion = selectQuestion;
	}

	public boolean isSkipQuestion() {
		return isSkipQuestion;
	}

	public void setSkipQuestion(boolean skipQuestion) {
		isSkipQuestion = skipQuestion;
	}

	public boolean isSelectQuestion() {
		return isSelectQuestion;
	}

	public void setSelectQuestion(boolean selectQuestion) {
		isSelectQuestion = selectQuestion;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return question;
	}

	public void setAnswer(String answer){
		this.answer = answer;
	}

	public String getAnswer(){
		return answer;
	}

	public void setSolutionText(String solutionText){
		this.solutionText = solutionText;
	}

	public String getSolutionText(){
		return solutionText;
	}

	public void setOption3(String option3){
		this.option3 = option3;
	}

	public String getOption3(){
		return option3;
	}

	public void setOption4(String option4){
		this.option4 = option4;
	}

	public String getOption4(){
		return option4;
	}

	public void setOption1(String option1){
		this.option1 = option1;
	}

	public String getOption1(){
		return option1;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setOption2(String option2){
		this.option2 = option2;
	}

	public String getOption2(){
		return option2;
	}

	public void setSolutionImage(String solutionImage){
		this.solutionImage = solutionImage;
	}

	public String getSolutionImage(){
		return solutionImage;
	}

	@Override
 	public String toString(){
		return 
			"ResItem{" + 
			"image = '" + image + '\'' + 
			",question = '" + question + '\'' + 
			",answer = '" + answer + '\'' + 
			",solution_text = '" + solutionText + '\'' + 
			",option3 = '" + option3 + '\'' + 
			",option4 = '" + option4 + '\'' + 
			",option1 = '" + option1 + '\'' + 
			",id = '" + id + '\'' + 
			",option2 = '" + option2 + '\'' + 
			",solution_image = '" + solutionImage + '\'' + 
			"}";
		}
}