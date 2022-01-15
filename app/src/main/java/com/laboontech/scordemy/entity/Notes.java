package com.laboontech.scordemy.entity;

import java.util.List;

public class Notes extends Base_Response {
    public int id, chapter_id;
    public String title, pdf,paid_type;
    public List<Notes> res;
}
