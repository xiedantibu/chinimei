package com.xlm.meishichina.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RecipesListInfo;
import com.xlm.meishichina.bean.RecipesListInfo.RecipeItem;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.assist.PauseOnScrollListener;
import com.xlm.meishichina.ui.activity.RecipeDetailActivity;
import com.xlm.meishichina.ui.adapter.PersonalFavRecipeListAdapter;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshListView;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.util.Toaster;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class PersonalOrginalFragment extends BaseFragment implements
        MeishiConfig, OnClickListener
{
    public static PersonalOrginalFragment getInstance(Bundle bundle)
    {
        PersonalOrginalFragment fragment = new PersonalOrginalFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private String id = "";

    private int currentIdx = 1;

    private int currentItemNum = 0;

    private int defaultItemNum = 10;

    private int totalItemNum = 0;

    private int requestItemNum = 0;

    private List<RecipeItem> mRecipeItems = new ArrayList<RecipeItem>();

    private RecipesListInfo mFavRecipesList = null;

    private PullToRefreshListView mPullToRefreshListView = null;

    public ListView mListView = null;

    private PersonalFavRecipeListAdapter mFavRecipeListAdapter = null;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {
                case HANDLE_REQUEST_FAV_RECIPES_LIST_SUCCESS:
                    if (mPullToRefreshListView == null)
                    {

                        mViewStub
                                .setLayoutResource(R.layout.fragment_personal_listview);
                        mPullToRefreshListView = (PullToRefreshListView) mViewStub
                                .inflate();
                        mListView = mPullToRefreshListView.getRefreshableView();

                        mFavRecipeListAdapter = new PersonalFavRecipeListAdapter(
                                getSupportActivity(), mRecipeItems);

                        mListView.setAdapter(mFavRecipeListAdapter);
                        mListView
                                .setOnScrollListener(new PauseOnScrollListener(
                                        mImageLoader, true, true));
                        mListView
                                .setOnItemClickListener(new OnItemClickListener()
                                {
                                    public void onItemClick(
                                            android.widget.AdapterView<?> paramAdapterView,
                                            View paramView, int paramInt,
                                            long paramLong)
                                    {

                                        if (mRecipeItems != null
                                                && mRecipeItems.size() > 0)
                                        {
                                            Intent intent = new Intent(
                                                    getSupportActivity(),
                                                    RecipeDetailActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(
                                                    "recipe_id",
                                                    mRecipeItems
                                                            .get(paramInt - 1)
                                                            .getId().trim());
                                            bundle.putString(
                                                    "recipe_name",
                                                    mRecipeItems
                                                            .get(paramInt - 1)
                                                            .getRecipetitle()
                                                            .trim());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    };
                                });

                        mPullToRefreshListView
                                .setOnRefreshListener(new OnRefreshListener<ListView>()
                                {

                                    @Override
                                    public void onRefresh(
                                            PullToRefreshBase<ListView> refreshView)
                                    {
                                        if (requestItemNum != 0)
                                        {
                                            HttpRequest
                                                    .getInstance()
                                                    .requestFavRecipesList(
                                                            getSupportActivity(),
                                                            mHandler, id,
                                                            currentIdx + 1,
                                                            defaultItemNum,
                                                            false);
                                        }
                                        else
                                        {
                                            Toaster.showShort(
                                                    getSupportActivity(),
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

                    mPullToRefreshListView.onRefreshComplete();
                    mImageViewEmpty.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                    mFavRecipesList = (RecipesListInfo) msg.obj;

                    currentIdx = StringUtil.toInt(mFavRecipesList.getIdx()
                            .trim());
                    currentItemNum = StringUtil.toInt(mFavRecipesList
                            .getCount().trim());
                    totalItemNum = StringUtil.toInt(mFavRecipesList.getTotal()
                            .trim());

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

                    mRecipeItems
                            .addAll(mFavRecipesList.getmRecipeItems());
                    mFavRecipeListAdapter.notifyDataSetChanged();
                    if (mRecipeItems.size() == 0)
                    {
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }
                    break;
                case HANDLE_REQUEST_FAV_RECIPES_LIST_FAIL:
                    if (mPullToRefreshListView != null)
                    {
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                    String fav_msg_fail = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), fav_msg_fail);
                    break;
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_loading_content, null);
        mLinearLayoutProgress = (LinearLayout) view
                .findViewById(R.id.progressContainer);
        mImageViewEmpty = (ImageView) view.findViewById(R.id.emptys);
        mViewStub = (ViewStub) view.findViewById(R.id.loader_content);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            id = (bundle.getString("uid")).trim();
            if (!"".equals(id))
            {
                HttpRequest.getInstance().requestFavRecipesList(
                        getSupportActivity(), mHandler, id, 1, 10, false);
                mLinearLayoutProgress.setVisibility(View.VISIBLE);
                mImageViewEmpty.setOnClickListener(this);
            }
            else
            {
                mLinearLayoutProgress.setVisibility(View.GONE);
                mImageViewEmpty.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            mImageViewEmpty.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.emptys:
                if (!"".equals(id))
                {
                    HttpRequest.getInstance().requestFavRecipesList(
                            getSupportActivity(), mHandler, id, 1, 10, false);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                    if (mPullToRefreshListView != null)
                        mPullToRefreshListView.setVisibility(View.GONE);
                }
                else
                {
                    mLinearLayoutProgress.setVisibility(View.GONE);
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

}
