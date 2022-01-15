package com.laboontech.scordemy;

public class Item {
    private String Title;
    private String mimage;
    private String Date;

    public Item(String title, String image, String date){
        Title=title;
        mimage=image;
        Date=date;
    }
    public String getTitle(){
        return Title;
    }
    public String getImage(){
        return mimage;
    }
    public String getDate(){
        return Date;
    }
}
