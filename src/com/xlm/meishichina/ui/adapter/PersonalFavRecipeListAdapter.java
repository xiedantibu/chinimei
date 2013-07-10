package com.xlm.meishichina.ui.adapter;

import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RecipesListInfo.RecipeItem;
import com.xlm.meishichina.image.core.DisplayImageOptions;
import com.xlm.meishichina.image.core.display.RoundedBitmapDisplayer;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.util.MeishiConfig;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PersonalFavRecipeListAdapter extends BaseAdapter implements
        MeishiConfig
{
    /**
     * mOptions
     */
    private DisplayImageOptions mOptions;
    private Context mContext;
    private List<RecipeItem> mRecipeItems;

    public PersonalFavRecipeListAdapter(Context mContext,
            List<RecipeItem> mRecipeItems)
    {

        this.mContext = mContext;
        this.mRecipeItems = mRecipeItems;
        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.recipe_detail)
                .showImageForEmptyUri(R.drawable.recipe_detail)
                .cacheInMemory().displayer(new RoundedBitmapDisplayer(5))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount()
    {
        return mRecipeItems.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return mRecipeItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2)
    {
        HolderView mHolderView = null;
        if (arg1 == null)
        {

            arg1 = LayoutInflater.inflate(mContext,
                    R.layout.fragment_fav_recipe_listview_item);
            mHolderView = new HolderView(arg1);
            arg1.setTag(mHolderView);
        }
        else
        {
            mHolderView = (HolderView) arg1.getTag();
        }
        if (mRecipeItems != null && mRecipeItems.size() > 0)
        {
            mHolderView.mTextViewName.setText(mRecipeItems.get(arg0)
                    .getRecipetitle().trim());
            mHolderView.mTextViewingredients
                    .setText(String.format(
                            mContext.getString(R.string.fragment_personal_zhuliao_text),
                            mRecipeItems.get(arg0).getMainingredient()
                                    .trim()));
            mImageLoader.displayImage(MeishiApplication.getApplication().isShowPic()?mRecipeItems.get(arg0).getCover()
                    .trim():null, mHolderView.mImageViewCover, mOptions);
        }
        return arg1;
    }

    class HolderView
    {

        private ImageView mImageViewCover;
        private TextView mTextViewName;
        private TextView mTextViewingredients;

        public HolderView(View view)
        {
            mImageViewCover = (ImageView) view
                    .findViewById(R.id.fav_recipe_listview_item_cover);
            mTextViewName = (TextView) view
                    .findViewById(R.id.fav_recipe_listview_item_name);
            mTextViewingredients = (TextView) view
                    .findViewById(R.id.fav_recipe_listview_item_ingredients);

        }
    }
}
