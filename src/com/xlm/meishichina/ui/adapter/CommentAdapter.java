package com.xlm.meishichina.ui.adapter;

import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.CommentInfo.CommentItem;
import com.xlm.meishichina.image.core.DisplayImageOptions;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.PersonalActivity;
import com.xlm.meishichina.util.MeishiConfig;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CommentAdapter extends BaseAdapter implements MeishiConfig
{
    /**
     * mOptions
     */
    private DisplayImageOptions mOptions;
    private Context mContext;
    private List<CommentItem> mListCommentItems;

    public CommentAdapter(Context mContext, List<CommentItem> mCommentItems)
    {

        this.mContext = mContext;
        this.mListCommentItems = mCommentItems;
        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.biz_tie_user_avater_bg)
                .showImageForEmptyUri(R.drawable.biz_tie_user_avater_bg)
                .cacheInMemory().bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount()
    {
        return mListCommentItems.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return mListCommentItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2)
    {
        HolderView mHolderView = null;
        if (arg1 == null)
        {

            arg1 = LayoutInflater.inflate(mContext,
                    R.layout.fragment_recipe_comment_listview_item);
            mHolderView = new HolderView(arg1);
            arg1.setTag(mHolderView);
        }
        else
        {
            mHolderView = (HolderView) arg1.getTag();
        }

        if (mListCommentItems != null && mListCommentItems.size() > 0)
        {
            mHolderView.mTextViewTime.setText(mListCommentItems.get(arg0)
                    .getTime().trim());
            mHolderView.mTextViewName.setText(mListCommentItems.get(arg0)
                    .getAuthor().trim());

            mHolderView.mTextViewContent.setText(mListCommentItems.get(arg0)
                    .getMessage().replace("<br />", "").replace("&nbsp;", ""));

            mImageLoader.displayImage(MeishiApplication.getApplication().isShowPic()?mListCommentItems.get(arg0).getUcover()
                    .trim():null, mHolderView.mImageViewAvater, mOptions);
            mHolderView.mImageViewAvater
                    .setOnClickListener(new OnClickListener()
                    {

                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(mContext,
                                    PersonalActivity.class);
                            intent.putExtra("uid", mListCommentItems.get(arg0)
                                    .getUid().trim());
                            intent.putExtra("name", mListCommentItems.get(arg0)
                                    .getAuthor().trim());
                            if (MeishiApplication.getApplication()
                                    .getUserInfo() != null
                                    && MeishiApplication.getApplication()
                                            .getUserInfo().getSid() != null
                                    && MeishiApplication
                                            .getApplication()
                                            .getUserInfo()
                                            .getuId()
                                            .trim()
                                            .equals(mListCommentItems.get(arg0)
                                                    .getUid().trim()))
                            {
                                intent.putExtra("ismyself", true);
                            }
                            else
                            {
                                intent.putExtra("ismyself", false);
                            }
                            mContext.startActivity(intent);
                        }
                    });
        }
        return arg1;
    }

    class HolderView
    {

        private ImageView mImageViewAvater;
        private TextView mTextViewTime;
        private TextView mTextViewName;
        private TextView mTextViewContent;

        public HolderView(View view)
        {
            mImageViewAvater = (ImageView) view
                    .findViewById(R.id.comment_listview_item_avater);
            mTextViewTime = (TextView) view
                    .findViewById(R.id.comment_listview_item_date);
            mTextViewName = (TextView) view
                    .findViewById(R.id.comment_listview_item_username);
            mTextViewContent = (TextView) view
                    .findViewById(R.id.comment_listview_item_content);

        }
    }
}
