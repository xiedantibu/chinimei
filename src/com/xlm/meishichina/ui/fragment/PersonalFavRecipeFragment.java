package com.xlm.meishichina.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.RecipesListInfo;
import com.xlm.meishichina.bean.RecipesListInfo.RecipeItem;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.assist.PauseOnScrollListener;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.LoginActivity;
import com.xlm.meishichina.ui.activity.RecipeDetailActivity;
import com.xlm.meishichina.ui.adapter.PersonalFavRecipeListAdapter;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshListView;

public class PersonalFavRecipeFragment extends Fragment implements
        MeishiConfig, OnClickListener
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

    private String id = "";

    private int currentIdx = 1;

    private int currentItemNum = 0;

    private int defaultItemNum = 10;

    private int totalItemNum = 0;

    private int requestItemNum = 0;

    private int currentCheckItem = 0;

    private List<RecipeItem> mRecipeItems = new ArrayList<RecipeItem>();

    private RecipesListInfo mFavRecipesList = null;

    private PullToRefreshListView mPullToRefreshListView = null;

    public ListView mListView = null;

    private PersonalFavRecipeListAdapter mFavRecipeListAdapter = null;

    public ActionMode mActionMode;

    private boolean isMySelf = false;

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
                        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
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
                                        if (mActionMode != null)
                                        {
                                            mListView.clearChoices();
                                            mActionMode.finish();
                                            mActionMode = null;
                                            return;
                                        }
                                        mListView.clearChoices();

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
                        mListView
                                .setOnItemLongClickListener(new OnItemLongClickListener()
                                {

                                    @Override
                                    public boolean onItemLongClick(
                                            AdapterView<?> parent, View view,
                                            int position, long id)
                                    {

                                        if (position - 1 < mRecipeItems.size()
                                                && position - 1 >= 0)
                                        {
                                            if (mActionMode != null)
                                            {
                                                mActionMode.finish();
                                                mActionMode = null;
                                            }
                                            mListView.setItemChecked(position,
                                                    true);
                                            mFavRecipeListAdapter
                                                    .notifyDataSetChanged();
                                            mActionMode = getSupportActivity()
                                                    .startActionMode(
                                                            new PersonalFavRecipeActionModes());
                                            return true;
                                        }
                                        return false;
                                    }

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
                                                            true);
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

                    mRecipeItems.addAll(mFavRecipesList.getmRecipeItems());
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
                        if (mActionMode != null)
                        {
                            mListView.clearChoices();
                            mActionMode.finish();
                            mActionMode = null;
                            return;
                        }
                    }
                    mImageViewEmpty.setVisibility(View.VISIBLE);
                    String fav_msg_fail = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), fav_msg_fail);
                    break;
                case HANDLE_REQUEST_FAVCOLLECT_SUCCESS:
                    String fav_msg_success = (String) msg.obj;
                    Toaster.showShort(getSupportActivity(), fav_msg_success);
                    mRecipeItems.remove(currentCheckItem);
                    mFavRecipeListAdapter.notifyDataSetChanged();
                    if (mRecipeItems.size() == 0)
                    {
                        if (mPullToRefreshListView != null)
                        {
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                    }
                    break;
                case HANDLE_REQUEST_FAVCOLLECT_FAIL:
                    String fav_msg_fail2 = (String) msg.obj;
                    if (fav_msg_fail2.equals(MeishiApplication.getApplication()
                            .getResources().getString(R.string.login_no_user)))
                    {
                        Toaster.showShort(getSupportActivity(),
                                getString(R.string.login_please_first_logon));
                        Intent intent = new Intent(getSupportActivity(),
                                LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {

                        Toaster.showShort(getSupportActivity(), fav_msg_fail2);
                    }
                    break;
            }
        };
    };

    public static PersonalFavRecipeFragment getInstance(Bundle bundle)
    {
        PersonalFavRecipeFragment fragment = new PersonalFavRecipeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

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
            isMySelf = bundle.getBoolean("ismyself");
            if (!"".equals(id))
            {
                HttpRequest.getInstance().requestFavRecipesList(
                        getSupportActivity(), mHandler, id, 1, 10, true);
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
                            getSupportActivity(), mHandler, id, 1, 10, true);
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

    private class PersonalFavRecipeActionModes implements ActionMode.Callback
    {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            if (mActionMode == null)
                mActionMode = mode;

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            buildMenu(mode, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.menu_remove:
                    if (isMySelf)
                    {
                        if (MeishiApplication.getApplication().getUserInfo() != null
                                && MeishiApplication.getApplication()
                                        .getUserInfo().getSid() != null)
                        {
                            if (mRecipeItems != null && mRecipeItems.size() > 0)
                            {
                                HttpRequest
                                        .getInstance()
                                        .favOrDelCollectRequest(
                                                getSupportActivity(),
                                                mHandler,
                                                mRecipeItems
                                                        .get(mListView
                                                                .getCheckedItemPosition() - 1)
                                                        .getId().trim(), true);
                                currentCheckItem = mListView
                                        .getCheckedItemPosition() - 1;
                                mode.finish();
                            }

                        }
                        else
                        {
                            Toaster.showShort(
                                    getSupportActivity(),
                                    getString(R.string.login_please_first_logon));
                            Intent intent = new Intent(getSupportActivity(),
                                    LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        mode.finish();
                    }

                    break;
                case R.id.menu_share:
                    if (mRecipeItems != null && mRecipeItems.size() > 0)
                    {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(
                                Intent.EXTRA_TEXT,
                                mRecipeItems.get(
                                        mListView.getCheckedItemPosition() - 1)
                                        .getRecipetitle()
                                        + "\n 主料："
                                        + mRecipeItems
                                                .get(mListView
                                                        .getCheckedItemPosition() - 1)
                                                .getMainingredient().trim()
                                        + "\n 简介："
                                        + mRecipeItems
                                                .get(mListView
                                                        .getCheckedItemPosition() - 1)
                                                .getDescr()
                                                .replace("<br />", "")
                                                .replace("&nbsp;", "") + "\n");
                        startActivity(Intent.createChooser(intent,
                                getString(R.string.app_name)));
                    }
                    mode.finish();
                    break;

            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            mActionMode = null;
            mListView.clearChoices();
            mFavRecipeListAdapter.notifyDataSetChanged();
        }

        protected void buildMenu(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            menu.clear();
            if (isMySelf)
            {
                inflater.inflate(R.menu.menu_delete_and_share, menu);
            }
            else
            {
                inflater.inflate(R.menu.menu_share, menu);
            }
            mode.setTitle(mRecipeItems
                    .get(mListView.getCheckedItemPosition() - 1)
                    .getRecipetitle().trim());

        }
    }

}
