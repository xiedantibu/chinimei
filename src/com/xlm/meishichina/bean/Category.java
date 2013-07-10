package com.xlm.meishichina.bean;

import com.xlm.meishichina.db.annotation.Table;

@Table(name = "category")
public class Category extends Entity
{
    private int id;
    private String name;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
