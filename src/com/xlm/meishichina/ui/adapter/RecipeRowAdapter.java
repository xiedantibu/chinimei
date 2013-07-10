package com.xlm.meishichina.ui.adapter;

import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RecipeRowInfo.RecipeRowItem;
import com.xlm.meishichina.image.core.DisplayImageOptions;
import com.xlm.meishichina.image.core.display.RoundedBitmapDisplayer;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.util.MeishiConfig;

public class RecipeRowAdapter extends BaseAdapter implements MeishiConfig
{

    /**
     * mOptions
     */
    private DisplayImageOptions mOptions;
    private Context mContext;
    private List<RecipeRowItem> mRowItems;

    public RecipeRowAdapter(Context mContext, List<RecipeRowItem> mRowItems)
    {

        this.mContext = mContext;
        this.mRowItems = mRowItems;
        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.recipe_detail)
                .showImageForEmptyUri(R.drawable.recipe_detail)
                .cacheInMemory().displayer(new RoundedBitmapDisplayer(5))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount()
    {
        return mRowItems.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return mRowItems.get(arg0);
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
                    R.layout.activity_recipe_listview_item);
            mHolderView = new HolderView(arg1);
            arg1.setTag(mHolderView);
        }
        else
        {
            mHolderView = (HolderView) arg1.getTag();
        }
        if (mRowItems != null && mRowItems.size() > 0)
        {
            mHolderView.mTextViewName.setText(mRowItems.get(arg0).getSubject()
                    .trim());
            mHolderView.mTextViewComment.setText(String.format(
                    MeishiApplication.getApplication().getResources()
                            .getString(R.string.fragment_personal_comment_num),
                    mRowItems.get(arg0).getReplynum().trim()));
            mHolderView.mTextViewRead.setText(String.format(
                    MeishiApplication.getApplication().getResources()
                            .getString(R.string.fragment_personal_read_num),
                    mRowItems.get(arg0).getViewnum().trim()));
            mHolderView.mTextViewFav.setText(String.format(
                    MeishiApplication.getApplication().getResources()
                            .getString(R.string.fragment_personal_fav_num),
                    mRowItems.get(arg0).getCollnum().trim()));
            mHolderView.mTextViewContent
                    .setText(String
                            .format(MeishiApplication
                                    .getApplication()
                                    .getResources()
                                    .getString(
                                            R.string.fragment_personal_zhuliao_text),
                                    mRowItems.get(arg0).getMainingredient()
                                            .replace("<br />", "")
                                            .replace("&nbsp;", "")));
            mImageLoader.displayImage(MeishiApplication.getApplication().isShowPic()?mRowItems.get(arg0).getCover().trim():null,
                    mHolderView.mImageViewContent, mOptions);
        }
        return arg1;
    }

    class HolderView
    {

        private ImageView mImageViewContent;
        private TextView mTextViewRead;
        private TextView mTextViewName;
        private TextView mTextViewComment;
        private TextView mTextViewFav;
        private TextView mTextViewContent;

        public HolderView(View view)
        {
            mImageViewContent = (ImageView) view
                    .findViewById(R.id.recipe_listview_item_cover);
            mTextViewRead = (TextView) view
                    .findViewById(R.id.recipe_listview_item_read);
            mTextViewComment = (TextView) view
                    .findViewById(R.id.recipe_listview_item_comment);
            mTextViewFav = (TextView) view
                    .findViewById(R.id.recipe_listview_item_fav);
            mTextViewName = (TextView) view
                    .findViewById(R.id.recipe_listview_item_name);
            mTextViewContent = (TextView) view
                    .findViewById(R.id.recipe_listview_item_content);

        }
    }

}
