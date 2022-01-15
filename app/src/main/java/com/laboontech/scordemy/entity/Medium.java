package com.laboontech.scordemy.entity;

import java.util.List;

public class Medium extends Base_Response{
    public String  medium_id, medium ;
    public List<Medium> res;

    public String getMedium_id() {
        return medium_id;
    }

    public String getMedium() {
        return medium;
    }

    public Medium(String medium_id, String medium) {
        this.medium_id = medium_id;
        this.medium = medium;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return medium;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Medium){
            Medium c = (Medium )obj;
            if(c.getMedium().equals(medium) && c.getMedium_id()==medium_id ) return true;
        }

        return false;
    }


}
