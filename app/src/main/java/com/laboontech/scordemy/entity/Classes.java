package com.laboontech.scordemy.entity;

import java.util.List;

public class Classes extends Base_Response{

    private String class_id,c_name;
    private List<Classes> result;

    public Classes(String class_id, String c_name) {
        this.class_id = class_id;
        this.c_name = c_name;
    }

    public List<Classes> getResult() {
        return result;
    }

    public void setResult(List<Classes> result) {
        this.result = result;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return c_name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Classes){
            Classes c = (Classes)obj;
            if(c.getC_name().equals(c_name) && c.getClass_id()==class_id )return true;
        }

        return false;
    }
}
