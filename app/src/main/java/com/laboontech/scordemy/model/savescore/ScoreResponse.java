package com.laboontech.scordemy.model.savescore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScoreResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private String code;
	private String error_msg;

	public String getError_msg() {
		return error_msg;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	@Override
 	public String toString(){
		return 
			"ScoreResponse{" + 
			"msg = '" + msg + '\'' + 
			",code = '" + code + '\'' + 
			"}";
		}



		private String score_id,user_id , subtest_id,completed , score, accuracy,correct , wrong, skipped;
private List<ScoreResponse> res;

	public List<ScoreResponse> getRes() {
		return res;
	}

	public String getScore_id() {
		return score_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getSubtest_id() {
		return subtest_id;
	}

	public String getCompleted() {
		return completed;
	}

	public String getScore() {
		return score;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public String getCorrect() {
		return correct;
	}

	public String getWrong() {
		return wrong;
	}

	public String getSkipped() {
		return skipped;
	}
}