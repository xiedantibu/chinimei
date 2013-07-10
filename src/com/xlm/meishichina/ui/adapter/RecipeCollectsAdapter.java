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
import com.xlm.meishichina.bean.RecipesCollectListInfo.RecipesCollectItem;
import com.xlm.meishichina.image.core.DisplayImageOptions;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.util.MeishiConfig;

public class RecipeCollectsAdapter extends BaseAdapter implements MeishiConfig
{
    private List<RecipesCollectItem> mlist;
    private Context mContext;
    private DisplayImageOptions mOptions;

    public RecipeCollectsAdapter(Context mContext,
            List<RecipesCollectItem> mlist)
    {

        this.mContext = mContext;
        this.mlist = mlist;
        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.yaoyiyao_pic_background)
                .showImageForEmptyUri(R.drawable.yaoyiyao_pic_background)
                .cacheInMemory().cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount()
    {
        return mlist.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        HolderView mHolderView = null;
        if (null == convertView)
        {
            convertView = LayoutInflater.inflate(mContext,
                    R.layout.fragment_recipe_collects_item);
            mHolderView = new HolderView(convertView);
            convertView.setTag(mHolderView);
        }
        else
        {
            mHolderView = (HolderView) convertView.getTag();
        }
        if (mlist != null && mlist.size() > 0 && mlist.size() > position)
        {
            mImageLoader.displayImage(MeishiApplication.getApplication().isShowPic()?mlist.get(position).getCover().trim():null,
                    mHolderView.mImageView, mOptions);
            mHolderView.mTextView
                    .setText(mlist.get(position).getTitle().trim());
        }
        return convertView;
    }

    static class HolderView
    {
        private ImageView mImageView;
        private TextView mTextView;

        public HolderView(View v)
        {
            mImageView = (ImageView) v
                    .findViewById(R.id.recipe_collects_item_image);
            mTextView = (TextView) v
                    .findViewById(R.id.recipe_collects_item_text);
        }
    }

}
