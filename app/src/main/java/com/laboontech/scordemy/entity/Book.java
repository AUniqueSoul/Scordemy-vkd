package com.laboontech.scordemy.entity;

import java.util.List;

public class Book extends Base_Response {

    private String book_id, book_name, book_active, book_createdat;
    private List<Book> result;


    public Book(String book_id, String book_name) {
        this.book_id = book_id;
        this.book_name = book_name;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_active() {
        return book_active;
    }

    public void setBook_active(String book_active) {
        this.book_active = book_active;
    }

    public String getBook_createdat() {
        return book_createdat;
    }

    public void setBook_createdat(String book_createdat) {
        this.book_createdat = book_createdat;
    }

    public List<Book> getResult() {
        return result;
    }

    public void setResult(List<Book> result) {
        this.result = result;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return book_name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Book){
            Book c = (Book )obj;
            if(c.getBook_name().equals(book_name) && c.getBook_id()==book_id )return true;
        }

        return false;
    }
}
