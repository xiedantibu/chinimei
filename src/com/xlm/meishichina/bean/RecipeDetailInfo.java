package com.xlm.meishichina.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.xlm.meishichina.db.annotation.Id;

public class RecipeDetailInfo extends Entity
{

    /**
     * 菜谱详情中的菜谱id
     */
    @Id(column = "id")
    private String id;
    /**
     * 当前菜谱的作者
     */
    private String uId;

    /**
     * recipeTitle 菜谱标题
     */
    private String recipeTitle;

    /**
     * recipeAuthor 菜谱作者
     */
    private String recipeAuthor;

    /**
     * recipeAuthor 菜谱作者
     */
    private String recipeAuthorAvator;

    /**
     * recipeDescr 菜谱描述
     */
    private String recipeDescr;

    /**
     * recipeTips 菜谱提醒
     */
    private String recipeTips;

    /**
     * recipeCover 菜谱图片
     */
    private String recipeCover;

    /**
     * publishTime 发表时间
     */
    private String publishTime;

    private String viewNum;

    private String replyNum;

    private String favNum;

    /**
     * during 时间
     */
    private String during;

    /**
     * cuisine 口味
     */
    private String cuisine;

    /**
     * technics 工艺
     */
    private String technics;

    /**
     * level 难易程度
     */
    private String level;

    /**
     * baseFood 基本素材
     */
    private String baseFood;

    /**
     * mainIngredient 主要素材
     */
    private String mainIngredient;

    /**
     * mIngredientInfos1 主料
     */
    private List<IngredientInfo> mIngredientInfos1 = new ArrayList<IngredientInfo>();

    /**
     * mIngredientInfos2 配料
     */
    private List<IngredientInfo> mIngredientInfos2 = new ArrayList<IngredientInfo>();

    /**
     * mIngredientInfos3 辅料
     */
    private List<IngredientInfo> mIngredientInfos3 = new ArrayList<IngredientInfo>();

    /**
     * stepcount 步骤数
     */
    private String stepcount;

    /**
     * mStepInfos 步骤信息
     */
    private List<RecipeStepInfo> mStepInfos = new ArrayList<RecipeStepInfo>();

    /**
     * isFav 是否收藏
     */
    private String isFav;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUId()
    {
        return uId;
    }

    public void setUId(String uId)
    {
        this.uId = uId;
    }

    public String getRecipeTitle()
    {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle)
    {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeAuthor()
    {
        return recipeAuthor;
    }

    public void setRecipeAuthor(String recipeAuthor)
    {
        this.recipeAuthor = recipeAuthor;
    }

    public String getRecipeAuthorAvator()
    {
        return recipeAuthorAvator;
    }

    public void setRecipeAuthorAvator(String recipeAuthorAvator)
    {
        this.recipeAuthorAvator = recipeAuthorAvator;
    }

    public String getRecipeDescr()
    {
        return recipeDescr;
    }

    public void setRecipeDescr(String recipeDescr)
    {
        this.recipeDescr = recipeDescr;
    }

    public String getRecipeTips()
    {
        return recipeTips;
    }

    public void setRecipeTips(String recipeTips)
    {
        this.recipeTips = recipeTips;
    }

    public String getRecipeCover()
    {
        return recipeCover;
    }

    public void setRecipeCover(String recipeCover)
    {
        this.recipeCover = recipeCover;
    }

    public String getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(String publishTime)
    {
        this.publishTime = publishTime;
    }

    public String getViewNum()
    {
        return viewNum;
    }

    public void setViewNum(String viewNum)
    {
        this.viewNum = viewNum;
    }

    public String getReplyNum()
    {
        return replyNum;
    }

    public void setReplyNum(String replyNum)
    {
        this.replyNum = replyNum;
    }

    public String getFavNum()
    {
        return favNum;
    }

    public void setFavNum(String favNum)
    {
        this.favNum = favNum;
    }

    public String getDuring()
    {
        return during;
    }

    public void setDuring(String during)
    {
        this.during = during;
    }

    public String getCuisine()
    {
        return cuisine;
    }

    public void setCuisine(String cuisine)
    {
        this.cuisine = cuisine;
    }

    public String getTechnics()
    {
        return technics;
    }

    public void setTechnics(String technics)
    {
        this.technics = technics;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getBaseFood()
    {
        return baseFood;
    }

    public void setBaseFood(String baseFood)
    {
        this.baseFood = baseFood;
    }

    public String getMainIngredient()
    {
        return mainIngredient;
    }

    public void setMainIngredient(String mainIngredient)
    {
        this.mainIngredient = mainIngredient;
    }

    public List<IngredientInfo> getmIngredientInfos1()
    {
        return mIngredientInfos1;
    }

    public void setmIngredientInfos1(List<IngredientInfo> mIngredientInfos1)
    {
        this.mIngredientInfos1 = mIngredientInfos1;
    }

    public List<IngredientInfo> getmIngredientInfos2()
    {
        return mIngredientInfos2;
    }

    public void setmIngredientInfos2(List<IngredientInfo> mIngredientInfos2)
    {
        this.mIngredientInfos2 = mIngredientInfos2;
    }

    public List<IngredientInfo> getmIngredientInfos3()
    {
        return mIngredientInfos3;
    }

    public void setmIngredientInfos3(List<IngredientInfo> mIngredientInfos3)
    {
        this.mIngredientInfos3 = mIngredientInfos3;
    }

    public List<RecipeStepInfo> getmStepInfos()
    {
        return mStepInfos;
    }

    public void setmStepInfos(List<RecipeStepInfo> mStepInfos)
    {
        this.mStepInfos = mStepInfos;
    }

    public String getIsFav()
    {
        return isFav;
    }

    public void setIsFav(String isFav)
    {
        this.isFav = isFav;
    }

    public String getStepcount()
    {
        return stepcount;
    }

    public void setStepcount(String stepcount)
    {
        this.stepcount = stepcount;
    }

    public static RecipeDetailInfo prase(InputStream inputStream)
            throws IOException
    {
        RecipeDetailInfo recipeDetailInfo = null;
        IngredientInfo ingredientInfo = null;
        RecipeStepInfo recipeStepInfo = null;
        XmlPullParser xmlParser = Xml.newPullParser();
        String flagTag = null;
        try
        {
            xmlParser.setInput(inputStream, UTF_8);

            int evtType = xmlParser.getEventType();

            while (evtType != xmlParser.END_DOCUMENT)
            {
                String tag = xmlParser.getName();
                switch (evtType)
                {
                    case XmlPullParser.START_TAG:
                        if (tag.equalsIgnoreCase("infos"))
                        {
                            recipeDetailInfo = new RecipeDetailInfo();
                        }
                        else if (null != recipeDetailInfo)
                        {
                            if (tag.equalsIgnoreCase("error_code"))
                            {
                                recipeDetailInfo.setError_code(xmlParser
                                        .nextText());
                            }
                            else if (tag.equalsIgnoreCase("error_descr"))
                            {

                                recipeDetailInfo.setError_descr(xmlParser
                                        .nextText());
                            }
                            else if (recipeDetailInfo.getError_code()
                                    .equalsIgnoreCase("1"))
                            {

                                if (tag.equalsIgnoreCase("id"))
                                {
                                    recipeDetailInfo
                                            .setId(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("uid"))
                                {
                                    recipeDetailInfo.setUId(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("title"))
                                {
                                    recipeDetailInfo.setRecipeTitle(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("author"))
                                {
                                    recipeDetailInfo.setRecipeAuthor(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("avatar"))
                                {
                                    recipeDetailInfo
                                            .setRecipeAuthorAvator(xmlParser
                                                    .nextText());
                                }
                                else if (tag.equalsIgnoreCase("descr"))
                                {
                                    recipeDetailInfo.setRecipeDescr(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("tips"))
                                {
                                    recipeDetailInfo.setRecipeTips(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("cover"))
                                {
                                    recipeDetailInfo.setRecipeCover(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("time"))
                                {
                                    recipeDetailInfo.setPublishTime(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("viewnum"))
                                {
                                    recipeDetailInfo.setViewNum(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("replynum"))
                                {
                                    recipeDetailInfo.setReplyNum(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("favnum"))
                                {
                                    recipeDetailInfo.setFavNum(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("level"))
                                {
                                    recipeDetailInfo.setLevel(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("during"))
                                {
                                    recipeDetailInfo.setDuring(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("cuisine"))
                                {
                                    recipeDetailInfo.setCuisine(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("technics"))
                                {
                                    recipeDetailInfo.setTechnics(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("basefood"))
                                {
                                    recipeDetailInfo.setBaseFood(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("mainingredient"))
                                {
                                    recipeDetailInfo
                                            .setMainIngredient(xmlParser
                                                    .nextText());
                                }
                                else if (tag.equalsIgnoreCase("ingredient1"))
                                {
                                    flagTag = tag;
                                }
                                else if (tag.equalsIgnoreCase("item"))
                                {
                                    ingredientInfo = new IngredientInfo();
                                }
                                else if (ingredientInfo != null
                                        && tag.equalsIgnoreCase("name"))
                                {
                                    ingredientInfo.setIngredientName(xmlParser
                                            .nextText());
                                }
                                else if (ingredientInfo != null
                                        && tag.equalsIgnoreCase("scale"))
                                {
                                    ingredientInfo.setIngredientScale(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("ingredient2"))
                                {
                                    flagTag = tag;
                                }
                                else if (tag.equalsIgnoreCase("ingredient3"))
                                {
                                    flagTag = tag;
                                }
                                else if (tag.equalsIgnoreCase("stepcount"))
                                {
                                    recipeDetailInfo.setStepcount(xmlParser
                                            .nextText());
                                }
                                else if (tag.equalsIgnoreCase("step"))
                                {
                                    recipeStepInfo = new RecipeStepInfo();
                                }
                                else if (recipeStepInfo != null
                                        && tag.equalsIgnoreCase("idx"))
                                {
                                    recipeStepInfo.setIdx(xmlParser.nextText());
                                }
                                else if (recipeStepInfo != null
                                        && tag.equalsIgnoreCase("note"))
                                {
                                    recipeStepInfo
                                            .setNote(xmlParser.nextText());
                                }
                                else if (recipeStepInfo != null
                                        && tag.equalsIgnoreCase("pic"))
                                {
                                    recipeStepInfo.setPic(xmlParser.nextText());
                                }
                                else if (tag.equalsIgnoreCase("isfav"))
                                {
                                    recipeDetailInfo.setIsFav(xmlParser
                                            .nextText());
                                }
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (flagTag != null
                                && flagTag.equalsIgnoreCase("ingredient1")
                                && tag.equalsIgnoreCase("item")
                                && recipeDetailInfo != null
                                && ingredientInfo != null)
                        {
                            recipeDetailInfo.getmIngredientInfos1().add(
                                    ingredientInfo);
                            ingredientInfo = null;
                        }
                        if (flagTag != null
                                && flagTag.equalsIgnoreCase("ingredient2")
                                && tag.equalsIgnoreCase("item")
                                && recipeDetailInfo != null
                                && ingredientInfo != null)
                        {
                            recipeDetailInfo.getmIngredientInfos2().add(
                                    ingredientInfo);
                            ingredientInfo = null;
                        }
                        if (flagTag != null
                                && flagTag.equalsIgnoreCase("ingredient3")
                                && tag.equalsIgnoreCase("item")
                                && recipeDetailInfo != null
                                && ingredientInfo != null)
                        {
                            recipeDetailInfo.getmIngredientInfos3().add(
                                    ingredientInfo);
                            ingredientInfo = null;
                        }
                        if (tag.equalsIgnoreCase("step")
                                && recipeDetailInfo != null
                                && recipeStepInfo != null)
                        {
                            recipeDetailInfo.getmStepInfos()
                                    .add(recipeStepInfo);
                            recipeStepInfo = null;
                        }
                        if (tag.equalsIgnoreCase("ingredient1")
                                || tag.equalsIgnoreCase("ingredient2")
                                || tag.equalsIgnoreCase("ingredient3"))
                        {
                            flagTag = null;
                        }
                        break;
                }
                evtType = xmlParser.next();
            }
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        finally
        {
            inputStream.close();
        }
        return recipeDetailInfo;

    }
}
