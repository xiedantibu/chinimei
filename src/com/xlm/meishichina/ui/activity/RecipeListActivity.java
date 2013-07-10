package com.xlm.meishichina.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RecipeCusineInfo;
import com.xlm.meishichina.bean.RecipeCusineInfo.RecipeCusineItem;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.assist.PauseOnScrollListener;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.adapter.RecipeListAdapter;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshListView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

/**
 * 
 * @ClassName: RecipeListActivity
 * @Description: 家常菜谱 环球美食 特殊人群 食疗菜谱 菜单专题 主要食材 菜品口味 公用fragment
 * @author xlm
 * @date 2013-3-11 上午10:25:33
 */
public class RecipeListActivity extends BaseActivity implements MeishiConfig,
        OnClickListener
{
    /**
     * mLinearLayoutProgress 进度条加载layout
     */
    public LinearLayout mLinearLayoutProgress;
    /**
     * mImageViewEmpty 加载失败的时候现在的empty图片
     */
    public ImageView mImageViewEmpty;

    /**
     * mViewStub 第一次加载临时布局
     */
    public ViewStub mViewStub;
    private PullToRefreshListView mPullToRefreshListView;
    private int mType;
    private String mSearchText = "";
    private String mTitle = "";
    private int currentIdx = 1;

    private int currentItemNum = 0;

    private int defaultItemNum = 10;

    private int totalItemNum = 0;

    private int requestItemNum = 0;

    private boolean isPullRefresh = false;

    private List<RecipeCusineItem> mList = new ArrayList<RecipeCusineItem>();

    private RecipeCusineInfo mRecipeCusineInfo = null;

    private RecipeListAdapter mAdapter;

    private ListView mListView;

    private GestureDetector gestureDetector;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case HANDLE_REQUEST_RECIPE_LIST_SUCCESS:

                    if (mPullToRefreshListView == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.activity_recipe_listview);
                        mPullToRefreshListView = (PullToRefreshListView) mViewStub
                                .inflate();

                        mListView = mPullToRefreshListView.getRefreshableView();
                        mListView
                                .setOnScrollListener(new PauseOnScrollListener(
                                        mImageLoader, true, true));

                        mAdapter = new RecipeListAdapter(
                                RecipeListActivity.this, mList);
                        mListView.setAdapter(mAdapter);

                        gestureDetector = new GestureDetector(
                                RecipeListActivity.this,
                                new MyOnGestureListener());
                        mListView
                                .setOnItemClickListener(new OnItemClickListener()
                                {

                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> paramAdapterView,
                                            View paramView, int paramInt,
                                            long paramLong)
                                    {
                                        if (mRecipeCusineInfo != null)
                                        {
                                            Intent intent = new Intent(
                                                    RecipeListActivity.this,
                                                    RecipeDetailActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("recipe_id", mList
                                                    .get(paramInt - 1).getId()
                                                    .trim());
                                            bundle.putString("recipe_name",
                                                    mList.get(paramInt - 1)
                                                            .getRecipetitle()
                                                            .trim());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }

                                    }
                                });

                        mListView.setOnTouchListener(new OnTouchListener()
                        {

                            @Override
                            public boolean onTouch(View paramView,
                                    MotionEvent paramMotionEvent)
                            {
                                return gestureDetector
                                        .onTouchEvent(paramMotionEvent);
                            }
                        });

                        mPullToRefreshListView
                                .setOnRefreshListener(new OnRefreshListener2()
                                {

                                    @Override
                                    public void onPullDownToRefresh(
                                            PullToRefreshBase refreshView)
                                    {
                                        HttpRequest
                                                .getInstance()
                                                .requestRecipeList(
                                                        RecipeListActivity.this,
                                                        mHandler, mType,
                                                        mSearchText, 1,
                                                        defaultItemNum);
                                        isPullRefresh = true;
                                    }

                                    @Override
                                    public void onPullUpToRefresh(
                                            PullToRefreshBase refreshView)
                                    {
                                        if (requestItemNum != 0)
                                        {
                                            HttpRequest
                                                    .getInstance()
                                                    .requestRecipeList(
                                                            RecipeListActivity.this,
                                                            mHandler, mType,
                                                            mSearchText,
                                                            currentIdx + 1,
                                                            defaultItemNum);
                                        }
                                        else
                                        {
                                            Toaster.showShort(
                                                    RecipeListActivity.this,
                                                    MeishiApplication
                                                            .getApplication()
                                                            .getString(
                                                                    R.string.fragment_personal_load));
                                            mPullToRefreshListView
                                                    .onRefreshComplete();

                                        }

                                    }
                                });

                    }

                    if (isPullRefresh)
                    {
                        mList.clear();
                    }

                    mImageViewEmpty.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                    mRecipeCusineInfo = (RecipeCusineInfo) msg.obj;

                    currentIdx = StringUtil.toInt(mRecipeCusineInfo.getIdx()
                            .trim());
                    currentItemNum = StringUtil.toInt(mRecipeCusineInfo
                            .getCount().trim());
                    totalItemNum = StringUtil.toInt(mRecipeCusineInfo
                            .getTotal().trim());

                    if (currentItemNum == defaultItemNum)
                    {

                        if (currentIdx * currentItemNum < totalItemNum)
                        {
                            int num = (totalItemNum - currentIdx
                                    * currentItemNum);
                            requestItemNum = ((num >= 10) ? 10 : num);
                        }
                        else
                        {
                            requestItemNum = 0;
                        }

                    }
                    else
                    {
                        requestItemNum = 0;
                    }

                    mList.addAll(mRecipeCusineInfo.getmRecipeCusineInfos());
                    mAdapter.notifyDataSetChanged();
                    mPullToRefreshListView.onRefreshComplete();
                    if (isPullRefresh)
                    {
                        isPullRefresh = false;
                        mListView.setSelection(1);
                    }
                    if (mList.size() == 0)
                    {
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }
                    break;

                case HANDLE_REQUEST_RECIPE_LIST_FAIL:
                    if (mPullToRefreshListView != null)
                    {

                        if (isPullRefresh)
                        {
                            isPullRefresh = false;
                        }
                        mPullToRefreshListView.onRefreshComplete();
                    }
                    else
                    {
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                    }
                    Toaster.showShort(RecipeListActivity.this,
                            msg.obj.toString());
                    break;
            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading_content);
        mLinearLayoutProgress = (LinearLayout) findViewById(R.id.progressContainer);
        mImageViewEmpty = (ImageView) findViewById(R.id.emptys);
        mViewStub = (ViewStub) findViewById(R.id.loader_content);
        mTitle = getString(R.string.app_name);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mType = bundle.getInt("type");
            mSearchText = bundle.getString("id");// 搜索的关键词也当作id
            if (mType == 4)
            {
                mTitle = mSearchText.trim();
            }
            else
            {
                mTitle = bundle.getString("name");
            }
            HttpRequest.getInstance().requestRecipeList(
                    RecipeListActivity.this, mHandler, mType, mSearchText, 1,
                    defaultItemNum);
            mLinearLayoutProgress.setVisibility(View.VISIBLE);
            mImageViewEmpty.setOnClickListener(this);
        }
        else
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            mImageViewEmpty.setVisibility(View.VISIBLE);
        }
        getSupportActionBar().setNavigationMode(
                ActionBar.NAVIGATION_MODE_STANDARD);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.emptys:
                Bundle bundle = getIntent().getExtras();
                if (bundle != null)
                {

                    HttpRequest.getInstance().requestRecipeList(
                            RecipeListActivity.this, mHandler, mType,
                            mSearchText, 1, defaultItemNum);
                    mImageViewEmpty.setVisibility(View.GONE);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener
    {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY)
        {

            if (e2.getX() - e1.getX() > 200 && Math.abs(velocityX) > 100)
            {
                RecipeListActivity.this.finish();
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
                RecipeListActivity.this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
