package com.xlm.meishichina.ui.fragment;

import java.io.Serializable;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.LinearLayout;
import org.holoeverywhere.widget.TextView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.IngredientInfo;
import com.xlm.meishichina.bean.RecipeDetailInfo;
import com.xlm.meishichina.bean.RecipeStepInfo;
import com.xlm.meishichina.http.HttpRequest;
import com.xlm.meishichina.image.core.DisplayImageOptions;
import com.xlm.meishichina.ui.MeishiApplication;
import com.xlm.meishichina.ui.activity.LoginActivity;
import com.xlm.meishichina.ui.activity.PersonalActivity;
import com.xlm.meishichina.ui.activity.PictureShowActivity;
import com.xlm.meishichina.util.MeishiConfig;
import com.xlm.meishichina.util.Toaster;
import com.xlm.meishichina.widget.pulltorefresh.PullToRefreshScrollView;

public class RecipeDetailFragment extends BaseFragment implements MeishiConfig,
        OnClickListener
{

    /**
     * mOptions
     */
    private DisplayImageOptions mOptions;

    /**
     * mOptions
     */
    private DisplayImageOptions mOptions2;
    private PullToRefreshScrollView mPullToRefreshScrollView;

    public RecipeDetailInfo mRecipeDetailInfo = null;

    /**
     * mImageViewTop 最大图片
     */
    private ImageView mImageViewTop;

    /**
     * mImageViewUser 用户头像
     */
    private ImageView mImageViewUser;

    /**
     * mTextViewUserName 用户名字
     */
    private TextView mTextViewUserName;

    private TextView mTextViewRecipeName;

    private TextView mTextViewDescr;

    private TextView mTextViewKouwei;

    private TextView mTextViewGongyi;

    private TextView mTextViewTime;

    private TextView mTextViewDifficulty;

    private LinearLayout mLinearLayoutIngredients;

    private LinearLayout mLinearLayoutSteps;

    private TextView mTextViewTips;

    private TextView mTextViewTipsText;

    private List<RecipeStepInfo> mStepInfos;

    private String id = "";
    private String recipeName = "";
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            switch (msg.what)
            {

                case HANDLE_REQUEST_RECIPEDETAIL_SUCCESS:
                    mRecipeDetailInfo = (RecipeDetailInfo) msg.obj;
                    if (mPullToRefreshScrollView == null)
                    {
                        mViewStub
                                .setLayoutResource(R.layout.fragment_recipe_detail_base);
                        mPullToRefreshScrollView = (PullToRefreshScrollView) mViewStub
                                .inflate();
                        initViewStubComponent(mPullToRefreshScrollView);
                        mOptions = new DisplayImageOptions.Builder()
                                .showStubImage(
                                        R.drawable.yaoyiyao_pic_background)
                                .showImageForEmptyUri(
                                        R.drawable.yaoyiyao_pic_background)
                                .cacheInMemory().cacheOnDisc()
                                .bitmapConfig(Bitmap.Config.RGB_565).build();
                        mOptions2 = new DisplayImageOptions.Builder()
                                .showStubImage(
                                        R.drawable.yaoyiyao_pic_background)
                                .showImageForEmptyUri(
                                        R.drawable.yaoyiyao_pic_background)
                                .cacheInMemory()
                                .bitmapConfig(Bitmap.Config.RGB_565).build();
                    }
                    mPullToRefreshScrollView.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                    fillComponent();
                    break;
                case HANDLE_REQUEST_RECIPEDETAIL_FAIL:
                    mRecipeDetailInfo = null;
                    if (mPullToRefreshScrollView != null)
                    {

                        mPullToRefreshScrollView.setVisibility(View.GONE);
                    }
                    mImageViewEmpty.setVisibility(View.VISIBLE);
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

    public static RecipeDetailFragment getInstance(Bundle bundle)
    {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
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
        mImageViewEmpty.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            id = (bundle.getString("recipe_id")).trim();
            recipeName = (bundle.getString("recipe_name")).trim();
            HttpRequest.getInstance().recipeDetailRequest(getSupportActivity(),
                    mHandler, id);
            mLinearLayoutProgress.setVisibility(View.VISIBLE);
        }
        else
        {
            mLinearLayoutProgress.setVisibility(View.GONE);
            mImageViewEmpty.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void fillComponent()
    {
        if (mRecipeDetailInfo != null)
        {
            mImageLoader.displayImage(MeishiApplication.getApplication()
                    .isShowPic() ? mRecipeDetailInfo.getRecipeCover().trim()
                    : null, mImageViewTop, mOptions);
            mImageLoader.displayImage(MeishiApplication.getApplication()
                    .isShowPic() ? mRecipeDetailInfo.getRecipeAuthorAvator()
                    .trim() : null, mImageViewUser, mOptions2);

            mTextViewUserName.setText(mRecipeDetailInfo.getRecipeAuthor()
                    .trim());
            mTextViewRecipeName.setText(mRecipeDetailInfo.getRecipeTitle()
                    .trim());
            mTextViewDescr.setText(mRecipeDetailInfo.getRecipeDescr()
                    .replace("<br />", "").replace("&nbsp;", ""));
            mTextViewKouwei.setText(mRecipeDetailInfo.getCuisine().trim());
            mTextViewGongyi.setText(mRecipeDetailInfo.getTechnics().trim());
            mTextViewTime.setText(mRecipeDetailInfo.getDuring().trim());
            mTextViewDifficulty.setText(mRecipeDetailInfo.getLevel().trim());
            mImageViewUser.setOnClickListener(RecipeDetailFragment.this);

            List<IngredientInfo> mIngredientInfos01 = mRecipeDetailInfo
                    .getmIngredientInfos1();
            if (mIngredientInfos01 != null && mIngredientInfos01.size() > 0)
            {
                RelativeLayout localRelativeLayout = (RelativeLayout) LayoutInflater
                        .inflate(
                                getSupportActivity(),
                                R.layout.recipe_detail_ingredients_category_name);
                TextView mTextView = (TextView) localRelativeLayout
                        .findViewById(R.id.ingredientsCategoryName);
                mTextView.setText(MeishiApplication.getApplication().getString(
                        R.string.fragment_recipe_detail_zhuliao));
                mLinearLayoutIngredients.addView(localRelativeLayout);

                for (int i = 0; i < mIngredientInfos01.size(); i = i + 2)
                {

                    View view = LayoutInflater.inflate(getSupportActivity(),
                            R.layout.recipe_detail_ingredients_item);

                    TextView textView01 = (TextView) view
                            .findViewById(R.id.text11);
                    TextView textView02 = (TextView) view
                            .findViewById(R.id.text12);

                    textView01.setText(mIngredientInfos01.get(i)
                            .getIngredientName().trim());
                    textView02.setText(mIngredientInfos01.get(i)
                            .getIngredientScale().trim());

                    if (i + 1 < mIngredientInfos01.size())
                    {
                        TextView textView03 = (TextView) view
                                .findViewById(R.id.text21);
                        TextView textView04 = (TextView) view
                                .findViewById(R.id.text22);

                        textView03.setText(mIngredientInfos01.get(i + 1)
                                .getIngredientName().trim());
                        textView04.setText(mIngredientInfos01.get(i + 1)
                                .getIngredientScale().trim());
                    }
                    this.mLinearLayoutIngredients.addView(view);
                }
            }

            List<IngredientInfo> mIngredientInfos02 = mRecipeDetailInfo
                    .getmIngredientInfos2();
            if (mIngredientInfos02 != null && mIngredientInfos02.size() > 0)
            {
                RelativeLayout localRelativeLayout = (RelativeLayout) LayoutInflater
                        .inflate(
                                getSupportActivity(),
                                R.layout.recipe_detail_ingredients_category_name);
                TextView mTextView = (TextView) localRelativeLayout
                        .findViewById(R.id.ingredientsCategoryName);
                mTextView.setText(MeishiApplication.getApplication().getString(
                        R.string.fragment_recipe_detail_fuliao));
                mLinearLayoutIngredients.addView(localRelativeLayout);

                for (int i = 0; i < mIngredientInfos02.size(); i = i + 2)
                {

                    View view = LayoutInflater.inflate(getSupportActivity(),
                            R.layout.recipe_detail_ingredients_item);

                    TextView textView01 = (TextView) view
                            .findViewById(R.id.text11);
                    TextView textView02 = (TextView) view
                            .findViewById(R.id.text12);

                    textView01.setText(mIngredientInfos02.get(i)
                            .getIngredientName().trim());
                    textView02.setText(mIngredientInfos02.get(i)
                            .getIngredientScale().trim());

                    if (i + 1 < mIngredientInfos02.size())
                    {
                        TextView textView03 = (TextView) view
                                .findViewById(R.id.text21);
                        TextView textView04 = (TextView) view
                                .findViewById(R.id.text22);

                        textView03.setText(mIngredientInfos02.get(i + 1)
                                .getIngredientName().trim());
                        textView04.setText(mIngredientInfos02.get(i + 1)
                                .getIngredientScale().trim());
                    }
                    this.mLinearLayoutIngredients.addView(view);
                }
            }

            List<IngredientInfo> mIngredientInfos03 = mRecipeDetailInfo
                    .getmIngredientInfos3();
            if (mIngredientInfos03 != null && mIngredientInfos03.size() > 0)
            {
                RelativeLayout localRelativeLayout = (RelativeLayout) LayoutInflater
                        .inflate(
                                getSupportActivity(),
                                R.layout.recipe_detail_ingredients_category_name);
                TextView mTextView = (TextView) localRelativeLayout
                        .findViewById(R.id.ingredientsCategoryName);
                mTextView.setText(MeishiApplication.getApplication().getString(
                        R.string.fragment_recipe_detail_peiliao));
                mLinearLayoutIngredients.addView(localRelativeLayout);

                for (int i = 0; i < mIngredientInfos03.size(); i = i + 2)
                {

                    View view = LayoutInflater.inflate(getSupportActivity(),
                            R.layout.recipe_detail_ingredients_item);

                    TextView textView01 = (TextView) view
                            .findViewById(R.id.text11);
                    TextView textView02 = (TextView) view
                            .findViewById(R.id.text12);

                    textView01.setText(mIngredientInfos03.get(i)
                            .getIngredientName().trim());
                    textView02.setText(mIngredientInfos03.get(i)
                            .getIngredientScale().trim());

                    if (i + 1 < mIngredientInfos03.size())
                    {
                        TextView textView03 = (TextView) view
                                .findViewById(R.id.text21);
                        TextView textView04 = (TextView) view
                                .findViewById(R.id.text22);

                        textView03.setText(mIngredientInfos03.get(i + 1)
                                .getIngredientName().trim());
                        textView04.setText(mIngredientInfos03.get(i + 1)
                                .getIngredientScale().trim());
                    }
                    this.mLinearLayoutIngredients.addView(view);
                }
            }
            if ((mIngredientInfos01 == null || mIngredientInfos01.size() == 0)
                    && (mIngredientInfos02 == null || mIngredientInfos02.size() == 0)
                    && (mIngredientInfos03 == null || mIngredientInfos03.size() == 0))
            {
                TextView mTextView = (TextView) this.mLinearLayoutIngredients
                        .findViewById(R.id.fragment_recipe_detail_main_ingredients_text);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText(mRecipeDetailInfo.getMainIngredient()
                        .replace("<br />", "").replace("&nbsp;", ""));
            }

            if (!"".equals(mRecipeDetailInfo.getRecipeTips().trim()))
            {

                mTextViewTips.setText(mRecipeDetailInfo.getRecipeTips()
                        .replace("<br />", "").replace("&nbsp;", ""));
            }
            else
            {
                mTextViewTips.setVisibility(View.GONE);
                mTextViewTipsText.setVisibility(View.GONE);
            }

            mStepInfos = mRecipeDetailInfo.getmStepInfos();
            if (mStepInfos != null && mStepInfos.size() > 0)
            {

                for (int i = 0; i < mStepInfos.size(); i++)
                {
                    View view = LayoutInflater.inflate(getSupportActivity(),
                            R.layout.recipe_detail_step_item);
                    ImageView mImageView = (ImageView) view
                            .findViewById(R.id.fragment_recipe_detail_step_image);
                    TextView mTextViewIndex = (TextView) view
                            .findViewById(R.id.fragment_recipe_detail_step_index);
                    TextView mTextViewDesc = (TextView) view
                            .findViewById(R.id.fragment_recipe_detail_step_description);

                    String idx = mStepInfos.get(i).getIdx();
                    String pic = mStepInfos.get(i).getPic();
                    String note = mStepInfos.get(i).getNote();

                    if (!"".equals(pic.trim()))
                    {
                        mImageLoader.displayImage(MeishiApplication
                                .getApplication().isShowPic() ? pic.trim()
                                : null, mImageView, mOptions);
                    }
                    SpannableString spannableString = new SpannableString(idx
                            + ".");
                    spannableString.setSpan(
                            new StyleSpan(Typeface.BOLD_ITALIC), 0,
                            idx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    mTextViewIndex.setText(spannableString);

                    mTextViewDesc.setText(note.replace("<br />", "").replace(
                            "&nbsp;", ""));

                    this.mLinearLayoutSteps.addView(view);

                    view.setTag(Integer.valueOf(i));
                    StepInfoClick stepInfoClick = new StepInfoClick();
                    view.setOnClickListener(stepInfoClick);
                }
            }

        }
    }

    private void initViewStubComponent(View view)
    {
        mImageViewTop = (ImageView) view
                .findViewById(R.id.fragment_recipe_detail_item_imageview);
        mImageViewUser = (ImageView) view
                .findViewById(R.id.frament_recipe_detail_item_userCover);

        mTextViewUserName = (TextView) view
                .findViewById(R.id.frament_recipe_detail_item_userName);

        mTextViewRecipeName = (TextView) view
                .findViewById(R.id.frament_recipe_detail_item_name);

        mTextViewDescr = (TextView) view
                .findViewById(R.id.frament_recipe_detail_item_synopsis);

        mTextViewKouwei = (TextView) view
                .findViewById(R.id.fragment_recipe_detail_kouwei_id);

        mTextViewGongyi = (TextView) view
                .findViewById(R.id.fragment_recipe_detail_gongyi_id);

        mTextViewTime = (TextView) view
                .findViewById(R.id.fragment_recipe_detail_time_id);

        mTextViewDifficulty = (TextView) view
                .findViewById(R.id.fragment_recipe_detail_difficulty_id);

        mLinearLayoutIngredients = (LinearLayout) view
                .findViewById(R.id.fragment_recipe_detail_ingredients);

        mLinearLayoutSteps = (LinearLayout) view
                .findViewById(R.id.fragment_recipe_detail_steps);

        mTextViewTips = (TextView) view
                .findViewById(R.id.fragment_recipe_detail_tips);

        mTextViewTipsText = (TextView) view
                .findViewById(R.id.fragment_recipe_detail_tips_text);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.emptys:
                if (!"".equals(id))
                {
                    HttpRequest.getInstance().recipeDetailRequest(
                            getSupportActivity(), mHandler, id);
                    mLinearLayoutProgress.setVisibility(View.VISIBLE);
                    mImageViewEmpty.setVisibility(View.GONE);
                }
                break;
            case R.id.frament_recipe_detail_item_userCover:
                if (mRecipeDetailInfo != null)
                {
                    Intent intent = new Intent(getSupportActivity(),
                            PersonalActivity.class);
                    intent.putExtra("uid", mRecipeDetailInfo.getUId().trim());
                    intent.putExtra("name", mTextViewUserName.getText()
                            .toString());
                    if (MeishiApplication.getApplication().getUserInfo() != null
                            && MeishiApplication.getApplication().getUserInfo()
                                    .getSid() != null
                            && MeishiApplication.getApplication().getUserInfo()
                                    .getuId().trim()
                                    .equals(mRecipeDetailInfo.getUId().trim()))
                    {
                        intent.putExtra("ismyself", true);
                    }
                    else
                    {
                        intent.putExtra("ismyself", false);
                    }
                    startActivity(intent);
                }
                break;

        }
    }

    public class StepInfoClick implements OnClickListener
    {

        @Override
        public void onClick(View paramView)
        {

            Intent intent = new Intent(getSupportActivity(),
                    PictureShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("StepInfos", (Serializable) mStepInfos);
            bundle.putInt("position", (Integer) paramView.getTag());
            bundle.putString("recipeName", recipeName);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}
