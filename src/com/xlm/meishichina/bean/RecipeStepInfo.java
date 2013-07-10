package com.xlm.meishichina.bean;

import java.io.Serializable;

public class RecipeStepInfo implements Serializable
{

    private String idx;
    private String note;
    private String pic;

    public String getIdx()
    {
        return idx;
    }

    public void setIdx(String idx)
    {
        this.idx = idx;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getPic()
    {
        return pic;
    }

    public void setPic(String pic)
    {
        this.pic = pic;
    }

}
