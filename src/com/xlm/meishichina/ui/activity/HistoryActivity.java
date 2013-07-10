package com.xlm.meishichina.ui.activity;

import java.util.List;

import org.holoeverywhere.widget.GridView;
import org.holoeverywhere.widget.LinearLayout;

import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RandomRecipeInfo;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.adapter.HistoryAdapter;
import com.xlm.meishichina.util.Logmeishi;
import com.xlm.meishichina.util.MeishiConfig;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class HistoryActivity extends BaseActivity implements MeishiConfig
{
    /**
     * mLinearLayoutProgress 进度条加载layout
     */
    private LinearLayout mLinearLayoutProgress;
    /**
     * mImageViewEmpty 加载失败的时候现在的empty图片
     */
    private ImageView mImageViewEmpty;

    /**
     * mViewStub 第一次加载临时布局
     */
    private ViewStub mViewStub;

    private GridView mGridView;

    private List<RandomRecipeInfo> mRandomRecipeInfos = null;

    private HistoryAdapter mAdapter;

    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading_content);
        init();
    }

    private void init()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(
                getResources().getString(R.string.yaoyiyao_history_gridview));

        mLinearLayoutProgress = (LinearLayout) this
                .findViewById(R.id.progressContainer);
        mImageViewEmpty = (ImageView) this.findViewById(R.id.emptys);
        mViewStub = (ViewStub) this.findViewById(R.id.loader_content);
        mLinearLayoutProgress.setVisibility(View.VISIBLE);
        mImageViewEmpty.setVisibility(View.GONE);

        mRandomRecipeInfos = MeishiApplication.getApplication().getFinalDb()
                .findAll(RandomRecipeInfo.class);
        if (mRandomRecipeInfos != null && mRandomRecipeInfos.size() > 0)
        {
            mViewStub
                    .setLayoutResource(R.layout.activity_yaoyiyao_history_layout);
            mGridView = (GridView) mViewStub.inflate();
            mAdapter = new HistoryAdapter(HistoryActivity.this,
                    mRandomRecipeInfos);
            mGridView.setAdapter(mAdapter);
            mLinearLayoutProgress.setVisibility(View.GONE);
//            mGridView
//                    .setOnScrollListener(new PauseOnScrollListener(false, true));
            gestureDetector = new GestureDetector(HistoryActivity.this,
                    new MyOnGestureListener());

            mGridView.setOnTouchListener(new OnTouchListener()
            {

                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            mGridView.setOnItemClickListener(new OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id)
                {
                    if (mRandomRecipeInfos != null
                            && mRandomRecipeInfos.size() > 0
                            && mRandomRecipeInfos.size() > position)
                    {

                        Intent intent = new Intent(HistoryActivity.this,
                                RecipeDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("recipe_id",
                                mRandomRecipeInfos.get(position).getId());
                        bundle.putString("recipe_name",
                                mRandomRecipeInfos.get(position)
                                        .getRecipeTitle());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }
        else
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            mImageViewEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if (mRandomRecipeInfos != null)
                {
                    mImageLoader.clearMemoryCache();
                    mRandomRecipeInfos.clear();
                    mRandomRecipeInfos = null;
                }
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener
    {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY)
        {

            if (e2.getX() - e1.getX() > 200 && Math.abs(velocityX) > 100)
            {
                if (mRandomRecipeInfos != null)
                {
                    mRandomRecipeInfos.clear();
                    mRandomRecipeInfos = null;
                }
                HistoryActivity.this.finish();
                return true;
            }
            return false;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {

        if (mRandomRecipeInfos != null)
        {
            Logmeishi.d("-------onBackPressed--------");
            mRandomRecipeInfos.clear();
            mRandomRecipeInfos = null;
        }
        this.finish();
    }
}
