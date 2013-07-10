package com.xlm.meishichina.ui.fragment;

import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;
import android.os.Bundle;
import android.view.ViewStub;
import android.widget.ImageView;

public class BaseFragment extends Fragment
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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }
}
