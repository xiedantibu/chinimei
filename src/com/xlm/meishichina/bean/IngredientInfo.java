package com.xlm.meishichina.bean;

import java.io.Serializable;

public class IngredientInfo implements Serializable
{

    private String ingredientName;
    private String ingredientScale;

    public String getIngredientName()
    {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName)
    {
        this.ingredientName = ingredientName;
    }

    public String getIngredientScale()
    {
        return ingredientScale;
    }

    public void setIngredientScale(String ingredientScale)
    {
        this.ingredientScale = ingredientScale;
    }

}
