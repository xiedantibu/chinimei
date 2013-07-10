package com.xlm.meishichina.bean;

import com.xlm.meishichina.db.annotation.Table;

@Table(name = "cate_info")
public class CateInfo extends Entity
{

    private int id;
    private int cate_id;
    private int req_id;
    private String display_name;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getCate_id()
    {
        return cate_id;
    }

    public void setCate_id(int cate_id)
    {
        this.cate_id = cate_id;
    }

    public int getReq_id()
    {
        return req_id;
    }

    public void setReq_id(int req_id)
    {
        this.req_id = req_id;
    }

    public String getDisplay_name()
    {
        return display_name;
    }

    public void setDisplay_name(String display_name)
    {
        this.display_name = display_name;
    }

}
