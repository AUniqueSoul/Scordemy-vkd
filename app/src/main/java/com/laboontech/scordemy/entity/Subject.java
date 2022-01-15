package com.laboontech.scordemy.entity;

import java.util.List;

public class Subject extends Base_Response{

    private String subject_id,s_name,videos_count,notes_count,quiz_title_count,home_id;
    private List<Subject> result;

    public Subject(String subject_id, String s_name) {
        this.subject_id = subject_id;
        this.s_name = s_name;
    }

    public String getHome_id() {
        return home_id;
    }

    public void setHome_id(String home_id) {
        this.home_id = home_id;
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

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public List<Subject> getResult() {
        return result;
    }

    public void setResult(List<Subject> result) {
        this.result = result;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return s_name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Subject){
            Subject c = (Subject)obj;
            if(c.getS_name().equals(s_name) && c.getSubject_id()==subject_id )return true;
        }

        return false;
    }
}
