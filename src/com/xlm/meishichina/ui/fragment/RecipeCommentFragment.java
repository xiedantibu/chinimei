package com.xlm.meishichina.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.CommentInfo;
import com.xlm.meishichina.bean.CommentInfo.CommentItem;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.assist.PauseOnScrollListener;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.LoginActivity;
import com.xlm.meishichina.ui.adapter.CommentAdapter;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.StringUtil;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshListView;

public class RecipeCommentFragment extends Fragment implements MeishiConfig,
        OnClickListener
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

    private RelativeLayout mRelativeLayoutViewStub;

    private TextView mTextViewMockReplyEdit;

    private EditText mEditTextReplyEdit;

    private ImageView mImageViewReply;

    // private LinearLayout mLinearLayoutCommentReply;

    private PullToRefreshListView mPullToRefreshListView;

    private ListView mListView;

    private RelativeLayout mRelativeLayoutLoadMore;

    private Button mButtonMore;

    private LinearLayout mLinearLayoutLoading;

    private TextView mTextViewLoadingText;

    private TextView mTextViewErrorText;

    private CommentAdapter mAdapter;

    private CommentInfo mCommentInfo;

    private String id = "";

    private int currentIdx = 1;

    private int currentItemNum = 0;

    private int defaultItemNum = 10;

    private int totalItemNum = 0;

    private int requestItemNum = 0;

    private boolean isPullRefresh = false;

    private boolean from = true;

    private List<CommentItem> mList = new ArrayList<CommentInfo.CommentItem>();

    private InputMethodManager mInputMethodManager = null;

    private DialogsProgressDialogIndeterminateFragment dialogIndeterminateFragment = null;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            if (dialogIndeterminateFragment != null)
            {

                dialogIndeterminateFragment.dismiss();
            }
            switch (msg.what)
            {
                case HANDLE_REQUEST_RECIPECOMMENTLIST_SUCCESS:

                    if (mRelativeLayoutViewStub == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.fragment_recipe_comment_base);
                        mRelativeLayoutViewStub = (RelativeLayout) mViewStub
                                .inflate();

                        mTextViewMockReplyEdit = (TextView) mRelativeLayoutViewStub
                                .findViewById(R.id.mock_reply_edit);
                        mEditTextReplyEdit = (EditText) mRelativeLayoutViewStub
                                .findViewById(R.id.reply_edit);
                        mImageViewReply = (ImageView) mRelativeLayoutViewStub
                                .findViewById(R.id.reply);

                        mPullToRefreshListView = (PullToRefreshListView) mRelativeLayoutViewStub
                                .findViewById(R.id.fragment_recipe_comment_reply_list);

                        mListView = mPullToRefreshListView.getRefreshableView();
                        mListView
                                .setOnScrollListener(new PauseOnScrollListener(
                                        mImageLoader, true, true));
                        mRelativeLayoutLoadMore = (RelativeLayout) LayoutInflater
                                .inflate(getSupportActivity(),
                                        R.layout.layout_loading_more);

                        mButtonMore = (Button) mRelativeLayoutLoadMore
                                .findViewById(R.id.layout_more_button);

                        mLinearLayoutLoading = (LinearLayout) mRelativeLayoutLoadMore
                                .findViewById(R.id.layout_more_loading);
                        mTextViewLoadingText = (TextView) mRelativeLayoutLoadMore
                                .findViewById(R.id.layout_more_loading_text);
                        mTextViewErrorText = (TextView) mRelativeLayoutLoadMore
                                .findViewById(R.id.layout_more_err_text);

                        mListView.addFooterView(mRelativeLayoutLoadMore);

                        mAdapter = new CommentAdapter(getSupportActivity(),
                                mList);
                        mListView.setAdapter(mAdapter);
                        mButtonMore
                                .setOnClickListener(RecipeCommentFragment.this);
                        mTextViewErrorText
                                .setOnClickListener(RecipeCommentFragment.this);

                        mImageViewReply
                                .setOnClickListener(RecipeCommentFragment.this);

                        mTextViewMockReplyEdit
                                .setOnClickListener(RecipeCommentFragment.this);

                        mListView.setOnTouchListener(new OnTouchListener()
                        {

                            @Override
                            public boolean onTouch(View paramView,
                                    MotionEvent paramMotionEvent)
                            {

                                if (mEditTextReplyEdit.getVisibility() == View.VISIBLE)
                                {
                                    mInputMethodManager.hideSoftInputFromWindow(
                                            mEditTextReplyEdit
                                                    .getApplicationWindowToken(),
                                            0);
                                    mTextViewMockReplyEdit
                                            .setVisibility(View.VISIBLE);
                                    mEditTextReplyEdit.setVisibility(View.GONE);
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
                                        isPullRefresh = true;

                                        HttpRequest.getInstance()
                                                .recipeCommentListRequest(
                                                        getSupportActivity(),
                                                        mHandler, id, 1,
                                                        defaultItemNum, from);
                                    }
                                });
                    }

                    if (isPullRefresh)
                    {
                        mPullToRefreshListView.onRefreshComplete();
                        mList.clear();
                    }

                    mImageViewEmpty.setVisibility(View.GONE);
                    mRelativeLayoutViewStub.setVisibility(View.VISIBLE);

                    mCommentInfo = (CommentInfo) msg.obj;
                    currentIdx = StringUtil.toInt(mCommentInfo.getIdx().trim());
                    currentItemNum = StringUtil.toInt(mCommentInfo.getCount()
                            .trim());
                    totalItemNum = StringUtil.toInt(mCommentInfo.getTotal()
                            .trim());

                    if (currentItemNum == defaultItemNum)
                    {

                        if (currentIdx * currentItemNum < totalItemNum)
                        {
                            int num = (totalItemNum - currentIdx
                                    * currentItemNum);
                            requestItemNum = ((num >= 10) ? 10 : num);
                            if (requestItemNum == 0)
                            {
                                mButtonMore
                                        .setText(MeishiApplication
                                                .getApplication()
                                                .getString(
                                                        R.string.fragment_recipe_comment_load_more_no));
                            }
                            else
                            {

                                mButtonMore
                                        .setText(String
                                                .format(MeishiApplication
                                                        .getApplication()
                                                        .getString(
                                                                R.string.fragment_recipe_comment_load_more),
                                                        requestItemNum));
                            }
                        }
                        else
                        {
                            mButtonMore
                                    .setText(MeishiApplication
                                            .getApplication()
                                            .getString(
                                                    R.string.fragment_recipe_comment_load_more_no));
                            requestItemNum = 0;
                        }

                    }
                    else
                    {
                        mButtonMore
                                .setText(MeishiApplication
                                        .getApplication()
                                        .getString(
                                                R.string.fragment_recipe_comment_load_more_no));
                        requestItemNum = 0;
                    }

                    mButtonMore.setVisibility(View.VISIBLE);
                    mLinearLayoutLoading.setVisibility(View.GONE);
                    mTextViewErrorText.setVisibility(View.GONE);
                    mList.addAll(mCommentInfo.getmCommentItems());
                    mAdapter.notifyDataSetChanged();
                    if (isPullRefresh)
                    {
                        isPullRefresh = false;
                        mListView.setSelection(1);
                    }
                    break;

                case HANDLE_REQUEST_RECIPECOMMENTLIST_FAIL:
                    if (mRelativeLayoutViewStub != null)
                    {
                        mButtonMore.setVisibility(View.GONE);
                        mLinearLayoutLoading.setVisibility(View.GONE);
                        mTextViewErrorText.setVisibility(View.VISIBLE);

                        if (isPullRefresh)
                        {
                            mPullToRefreshListView.onRefreshComplete();
                            isPullRefresh = false;
                        }
                    }
                    else
                    {
                        mImageViewEmpty.setVisibility(View.VISIBLE);
                    }
                    Toaster.showShort(getSupportActivity(), msg.obj.toString());
                    break;

                case HANDLE_REQUEST_RECIPECOMMENTREPLY_SUCCESS:
                    mTextViewMockReplyEdit.setVisibility(View.VISIBLE);
                    mEditTextReplyEdit.setVisibility(View.GONE);
                    mEditTextReplyEdit.setText("");
                    isPullRefresh = true;
                    HttpRequest.getInstance().recipeCommentListRequest(
                            getSupportActivity(), mHandler, id, 1,
                            defaultItemNum, from);
                    break;
                case HANDLE_REQUEST_RECIPECOMMENTREPLY_FAIL:
                    if (msg.obj.toString().equals(
                            MeishiApplication.getApplication().getResources()
                                    .getString(R.string.login_no_user)))
                    {
                        Toaster.showShort(getSupportActivity(),
                                getString(R.string.login_please_first_logon));
                        Intent intent = new Intent(getSupportActivity(),
                                LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {

                        Toaster.showShort(getSupportActivity(),
                                msg.obj.toString());
                    }
                    break;
            }

        };
    };

    public static RecipeCommentFragment getInstance(Bundle bundle)
    {
        RecipeCommentFragment fragment = new RecipeCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
            from = (bundle.getBoolean("from"));
            if (from)
            {

                id = (bundle.getString("recipe_id")).trim();
            }
            else
            {
                id = (bundle.getString("id")).trim();
            }

            HttpRequest.getInstance().recipeCommentListRequest(
                    getSupportActivity(), mHandler, id, currentIdx,
                    defaultItemNum, from);

            mLinearLayoutProgress.setVisibility(View.VISIBLE);
            mImageViewEmpty.setOnClickListener(this);
        }
        else
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            mImageViewEmpty.setVisibility(View.VISIBLE);
        }
        mInputMethodManager = ((InputMethodManager) MeishiApplication
                .getApplication().getSystemService("input_method"));
        return view;
    }

    @Override
    public void onClick(View paramView)
    {

        switch (paramView.getId())
        {
            case R.id.layout_more_button:
                if (requestItemNum != 0)
                {

                    mButtonMore.setVisibility(View.GONE);
                    mLinearLayoutLoading.setVisibility(View.VISIBLE);
                    mTextViewLoadingText
                            .setText(String
                                    .format(MeishiApplication
                                            .getApplication()
                                            .getString(
                                                    R.string.fragment_recipe_comment_loading_more),
                                            requestItemNum));
                    HttpRequest.getInstance().recipeCommentListRequest(
                            getSupportActivity(), mHandler, id, currentIdx + 1,
                            defaultItemNum, from);
                }
                break;

            case R.id.emptys:
                if (!"".equals(id))
                {
                    HttpRequest.getInstance().recipeCommentListRequest(
                            getSupportActivity(), mHandler, id, currentIdx,
                            defaultItemNum, from);
                    mImageViewEmpty.setVisibility(View.GONE);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.layout_more_err_text:
                if (requestItemNum != 0)
                {

                    HttpRequest.getInstance().recipeCommentListRequest(
                            getSupportActivity(), mHandler, id, currentIdx + 1,
                            defaultItemNum, from);
                    mButtonMore.setVisibility(View.GONE);
                    mTextViewErrorText.setVisibility(View.GONE);
                    mLinearLayoutLoading.setVisibility(View.VISIBLE);
                    mTextViewLoadingText
                            .setText(String
                                    .format(MeishiApplication
                                            .getApplication()
                                            .getString(
                                                    R.string.fragment_recipe_comment_loading_more),
                                            requestItemNum));
                }
                break;
            case R.id.mock_reply_edit:
                mTextViewMockReplyEdit.setVisibility(View.GONE);
                mEditTextReplyEdit.setVisibility(View.VISIBLE);
                mEditTextReplyEdit.requestFocus();
                mInputMethodManager.showSoftInput(
                        mEditTextReplyEdit.findFocus(), 0);
                break;
            case R.id.reply:
                if (!"".equals(mEditTextReplyEdit.getText().toString().trim())
                        && mEditTextReplyEdit.getVisibility() == View.VISIBLE)
                {

                    mInputMethodManager.hideSoftInputFromWindow(
                            mEditTextReplyEdit.getApplicationWindowToken(), 0);

                    if (MeishiApplication.getApplication().getUserInfo() != null
                            && MeishiApplication.getApplication().getUserInfo()
                                    .getSid() != null)
                    {

                        dialogIndeterminateFragment = DialogsProgressDialogIndeterminateFragment
                                .getInstance(MeishiApplication
                                        .getApplication()
                                        .getString(
                                                R.string.yaoyiyao_fav_collect_deal_data));
                        dialogIndeterminateFragment
                                .show(getSupportActivity());
                        HttpRequest.getInstance().recipeCommentReplyRequest(
                                getSupportActivity(), mHandler, id,
                                mEditTextReplyEdit.getText().toString().trim(),
                                from);
                    }
                    else
                    {
                        Toaster.showShort(getSupportActivity(),
                                getString(R.string.login_please_first_logon));
                        Intent intent = new Intent(getSupportActivity(),
                                LoginActivity.class);
                        startActivity(intent);
                    }
                }
                else
                {
                    Toaster.showShort(
                            getSupportActivity(),
                            getString(R.string.fragment_recipe_comment_repley_none));
                }
                break;
        }
    }

}
