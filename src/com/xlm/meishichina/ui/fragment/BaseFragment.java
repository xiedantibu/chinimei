package com.xlm.meishichina.ui.fragment;

import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.LinearLayout;
import android.os.Bundle;
import android.view.ViewStub;
import android.widget.ImageView;

public class BaseFragment extends Fragment
{
    /**
     * mLinearLayoutProgress ����������layout
     */
    public LinearLayout mLinearLayoutProgress;
    /**
     * mImageViewEmpty ����ʧ�ܵ�ʱ�����ڵ�emptyͼƬ
     */
    public ImageView mImageViewEmpty;

    /**
     * mViewStub ��һ�μ�����ʱ����
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
