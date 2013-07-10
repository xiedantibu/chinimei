package com.xlm.meishichina.ui.adapter;

import org.holoeverywhere.app.Fragment;

import com.xlm.meishichina.util.MeishiConfig;

import android.annotation.SuppressLint;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @ClassName: AppFragmentPagerAdapter
 * @Description: TODO
 * @author xlm
 * @date 2012-11-26 ÏÂÎç7:15:30
 */
@SuppressLint("NewApi")
public abstract class AppFragmentPagerAdapter extends PagerAdapter
{
    private static final String TAG = "FragmentPagerAdapter";
    private static final boolean DEBUG = MeishiConfig.IS_DEBUG;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    public AppFragmentPagerAdapter(FragmentManager fm)
    {
        mFragmentManager = fm;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);

    @Override
    public void startUpdate(ViewGroup container)
    {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        if (mCurTransaction == null)
        {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        // String name = makeFragmentName(container.getId(), itemId);
        String name = getTag(position);
        Fragment fragment = (Fragment) mFragmentManager.findFragmentByTag(name);
        if (fragment != null)
        {
            if (DEBUG)
                Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        }
        else
        {
            fragment = getItem(position);
            if (DEBUG)
                Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment
                        + "container.getId():" + container.getId());
            // mCurTransaction.add(container.getId(), fragment,
            // makeFragmentName(container.getId(), itemId));
            mCurTransaction.add(container.getId(), fragment, getTag(position));
        }
        if (fragment != mCurrentPrimaryItem)
        {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    @SuppressLint("NewApi")
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        if (mCurTransaction == null)
        {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG)
            Log.v(TAG, "Detaching item #" + getItemId(position) + ": f="
                    + object + " v=" + ((Fragment) object).getView());
        mCurTransaction.detach((Fragment) object);
    }

    @SuppressLint("NewApi")
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem)
        {
            if (mCurrentPrimaryItem != null)
            {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null)
            {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container)
    {
        if (mCurTransaction != null)
        {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return ((Fragment) object).getView() == view;
    }


    @Override
    public Parcelable saveState()
    {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader)
    {
    }

    /**
     * Return a unique identifier for the item at the given position.
     * <p/>
     * <p>
     * The default implementation returns the given position. Subclasses should
     * override this method if the positions of items can change.
     * </p>
     * 
     * @param position
     *            Position within this adapter
     * @return Unique identifier for the item at position
     */
    public long getItemId(int position)
    {
        return position;
    }

    @SuppressWarnings("unused")
    private static String makeFragmentName(int viewId, long id)
    {
        return "android:switcher:" + viewId + ":" + id;
    }

    protected abstract String getTag(int position);
}
