package com.laboontech.scordemy.entity;

import java.util.List;

public class HomeData extends Base_Response {
    public List<HomeData> res;
    public List<HomeData> result;
    public String title, image, sub_table_name, class_name, subject , medium , chapter , videos_count, quiz_count, notes_count
            ,medium_id, m_name, class_id, c_name;
    public String home_data_id, subject_id, chapter_id,id,name;


    public HomeData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof HomeData){
            HomeData c = (HomeData)obj;
            if(c.title.equals(title) && c.home_data_id==home_data_id )return true;
        }

        return false;
    }

}
