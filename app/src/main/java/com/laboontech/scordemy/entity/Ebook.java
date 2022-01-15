package com.laboontech.scordemy.entity;

import java.util.List;

public class Ebook extends Base_Response {
    public List<Ebook> res;

    //Ebooks
    public String e_tittle, ebook_id, sub_title, e_image, sub_table, content_id;

    //Ebook classes
    public String ec_title, ec_id, ec_content_id, ec_subtable, ec_image;

    //Ebook stream
    public String es_id, es_title, es_sub_table, es_content_id, es_image;

    //Ebook Pdf
    public String ep_id, ep_title, ep_pdf, ep_content_id;

    //Ebook Booksolution
    public String ebs_id, ebs_title, ebs_image, ebs_content_id,ebs_sub_table;

    //Ebook Year
    public String ey_id,   ey_title,  ey_subtable ,  ey_content_id ;

}
