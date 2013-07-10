package com.xlm.meishichina.ui.adapter;

import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.TextView;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.ReportListInfo.ReportItem;
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

public class ReportListAdapter extends BaseAdapter implements MeishiConfig
{
    /**
     * mOptions
     */
    private DisplayImageOptions mOptions;
    private Context mContext;
    private List<ReportItem> mReportItems;

    public ReportListAdapter(Context mContext, List<ReportItem> mReportItems)
    {

        this.mContext = mContext;
        this.mReportItems = mReportItems;
        mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.recipe_detail)
                .showImageForEmptyUri(R.drawable.recipe_detail)
                .cacheInMemory().displayer(new RoundedBitmapDisplayer(2))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount()
    {
        return mReportItems.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        return mReportItems.get(arg0);
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
                    R.layout.fragment_recipe_report_listview_item);
            mHolderView = new HolderView(arg1);
            arg1.setTag(mHolderView);
        }
        else
        {
            mHolderView = (HolderView) arg1.getTag();
        }
        if (mReportItems != null && mReportItems.size() > 0)
        {
            mHolderView.mTextViewName.setText(mReportItems.get(arg0)
                    .getAuthor().trim());
            mHolderView.mTextViewTime.setText(mReportItems.get(arg0).getTime()
                    .trim());
            mHolderView.mTextViewContent.setText(mReportItems.get(arg0)
                    .getMessage().replace("<br />", "").replace("&nbsp;", ""));
            mImageLoader.displayImage(MeishiApplication.getApplication().isShowPic()?mReportItems.get(arg0).getCover().trim():null,
                    mHolderView.mImageViewContent, mOptions);
        }
        return arg1;
    }

    class HolderView
    {

        private ImageView mImageViewContent;
        private TextView mTextViewTime;
        private TextView mTextViewName;
        private TextView mTextViewContent;

        public HolderView(View view)
        {
            mImageViewContent = (ImageView) view
                    .findViewById(R.id.report_listview_item_imageview);
            mTextViewTime = (TextView) view
                    .findViewById(R.id.report_listview_item_date);
            mTextViewName = (TextView) view
                    .findViewById(R.id.report_listview_item_username);
            mTextViewContent = (TextView) view
                    .findViewById(R.id.report_listview_item_content);

        }
    }
}
