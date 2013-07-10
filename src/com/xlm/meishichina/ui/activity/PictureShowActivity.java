package com.xlm.meishichina.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.FrameLayout;
import org.holoeverywhere.widget.TextView;
import android.graphics.Bitmap;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RecipeStepInfo;
import com.xlm.meishichina.image.core.DisplayImageOptions;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.widget.imageviewtouch.ImageViewTouch;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PictureShowActivity extends BaseActivity implements MeishiConfig
{
    private ViewPager mViewPager;
    private TextView mTextViewName, mTextViewSum, mTextViewInfo;
    private List<FrameLayout> mList;
    private int currentItem = 0;
    private List<RecipeStepInfo> mStepInfos;
    private String recipeName = "";
    private GestureDetector gestureDetector;
    private DisplayImageOptions mOptions;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        init();
    }

    private void init()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setBackgroundDrawable(
                getResources().getDrawable(R.drawable.actionbar_bg));

        mOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.yaoyiyao_pic_background)
                .cacheInMemory().bitmapConfig(Bitmap.Config.RGB_565).build();

        mViewPager = (ViewPager) findViewById(R.id.show_pic_pager);

        mTextViewName = (TextView) findViewById(R.id.show_pic_text_setname);
        mTextViewSum = (TextView) findViewById(R.id.show_pic_imgsum);
        mTextViewInfo = (TextView) findViewById(R.id.show_pic_imginfo);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentItem = bundle.getInt("position");
        mStepInfos = (List<RecipeStepInfo>) bundle.getSerializable("StepInfos");
        recipeName = bundle.getString("recipeName");

        gestureDetector = new GestureDetector(PictureShowActivity.this,
                new MyOnGestureListener());

        mList = new ArrayList<FrameLayout>();

        for (int i = 0; i < mStepInfos.size(); i++)
        {

            final FrameLayout layout = (FrameLayout) LayoutInflater.inflate(
                    PictureShowActivity.this, R.layout.activity_show_pic_item);
            ImageViewTouch mImageView = (ImageViewTouch) layout
                    .findViewById(R.id.show_pic_item_picture);
            mImageView.setFitToScreen(true);
            // http://i3.meishichina.com/attachment/recipe/201012/m_201012041224261.JPG
            String url = mStepInfos.get(i).getPic().replace("m_", "").trim();
            Logmeishi.d("uri=========" + url);
            mImageLoader.displayImage(url, mImageView, mOptions);
            mList.add(layout);
        }

        String sum = (currentItem + 1) + "/" + mStepInfos.size();
        mTextViewName.setText(recipeName.trim());
        mTextViewInfo.setText(mStepInfos.get(currentItem).getNote()
                .replace("<br />", "").replace("&nbsp;", ""));
        mTextViewSum.setText(sum);

        mViewPager.setAdapter(new ImagePageAdapter());
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        mViewPager.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    public class ImagePageAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return mList.size();
        }

        @Override
        public Object instantiateItem(View container, int position)
        {

            ((ViewPager) container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(View container, int position, Object object)
        {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View paramView, Object paramObject)
        {
            return paramView == paramObject;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1)
        {

        }

        @Override
        public Parcelable saveState()
        {
            return null;
        }

        @Override
        public void startUpdate(View arg0)
        {

        }

        @Override
        public void finishUpdate(View arg0)
        {

        }
    }

    private class MyPageChangeListener implements OnPageChangeListener
    {

        @Override
        public void onPageScrolled(int paramInt1, float paramFloat,
                int paramInt2)
        {

        }

        @Override
        public void onPageSelected(int paramInt)
        {
            currentItem = paramInt;
            String sum = (currentItem + 1) + "/" + mStepInfos.size();
            mTextViewInfo.setText(mStepInfos.get(currentItem).getNote()
                    .replace("<br />", "").replace("&nbsp;", ""));
            mTextViewSum.setText(sum);

        }

        @Override
        public void onPageScrollStateChanged(int paramInt)
        {

        }

    }

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener
    {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY)
        {

            if (velocityX > 100 && mViewPager.getCurrentItem() == 0)
            {
                PictureShowActivity.this.finish();
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {

        if (mList != null && !mList.isEmpty())
        {
            mList.clear();
            mList = null;
        }
        super.onDestroy();
    }
}
