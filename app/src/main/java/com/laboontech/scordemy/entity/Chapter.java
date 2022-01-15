package com.laboontech.scordemy.entity;

import java.util.List;

public class Chapter extends Base_Response{

    private String chapter_id;
    private String chap_name;
    private String videos_count;
    private String notes_count;
    private String quiz_title_count;

    public String getHome_id() {
        return home_id;
    }

    public void setHome_id(String home_id) {
        this.home_id = home_id;
    }

    private String home_id;
    private List<Chapter> result;

    public Chapter(String chapter_id, String chap_name) {
        this.chapter_id = chapter_id;
        this.chap_name = chap_name;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChap_name() {
        return chap_name;
    }

    public void setChap_name(String chap_name) {
        this.chap_name = chap_name;
    }

    public List<Chapter> getResult() {
        return result;
    }

    public void setResult(List<Chapter> result) {
        this.result = result;
    }


    public String getVideos_count() {
        return videos_count;
    }

    public void setVideos_count(String videos_count) {
        this.videos_count = videos_count;
    }

    public String getNotes_count() {
        return notes_count;
    }

    public void setNotes_count(String notes_count) {
        this.notes_count = notes_count;
    }

    public String getQuiz_title_count() {
        return quiz_title_count;
    }

    public void setQuiz_title_count(String quiz_title_count) {
        this.quiz_title_count = quiz_title_count;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return chap_name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Chapter){
            Chapter c = (Chapter)obj;
            if(c.getChap_name().equals(chap_name) && c.getChapter_id()==chapter_id )return true;
        }

        return false;
    }
}
