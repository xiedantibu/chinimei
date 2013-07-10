package com.xlm.meishichina.widget.slidmenu;

import org.holoeverywhere.app.Activity;

import com.xlm.meishichina.util.Logmeishi;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class SlidingActivity extends Activity implements SlidingActivityBase
{

    private SlidingActivityHelper mHelper;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHelper = new SlidingActivityHelper(this);
        mHelper.onCreate(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    @Override
    public void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        Logmeishi.d("onPostCreate");
        mHelper.onPostCreate(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#findViewById(int)
     */
    @Override
    public View findViewById(int id)
    {
        View v = super.findViewById(id);
        if (v != null)
            return v;
        return mHelper.findViewById(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Logmeishi.d("onSaveInstanceState");
        mHelper.onSaveInstanceState(outState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#setContentView(int)
     */
    @Override
    public void setContentView(int id)
    {
        Logmeishi.d("setContentView(int id)");
        setContentView(getLayoutInflater().inflate(id, null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#setContentView(android.view.View)
     */
    @Override
    public void setContentView(View v)
    {
        Logmeishi.d("setContentView(View v)");
        setContentView(v, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#setContentView(android.view.View,
     * android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View v, LayoutParams params)
    {
        super.setContentView(v, params);
        mHelper.registerAboveContentView(v, params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(int)
     */
    public void setBehindContentView(int id)
    {
        Logmeishi.d("setBehindContentView(int id)");
        setBehindContentView(getLayoutInflater().inflate(id, null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android
     * .view.View)
     */
    public void setBehindContentView(View v)
    {
        setBehindContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android
     * .view.View, android.view.ViewGroup.LayoutParams)
     */
    public void setBehindContentView(View v, LayoutParams params)
    {
        Logmeishi.d("setBehindContentView(View v, LayoutParams params)");
        mHelper.setBehindContentView(v, params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.slidingmenu.lib.app.SlidingActivityBase#getSlidingMenu()
     */
    public SlidingMenu getSlidingMenu()
    {
        Logmeishi.d("getSlidingMenu");
        return mHelper.getSlidingMenu();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.slidingmenu.lib.app.SlidingActivityBase#toggle()
     */
    public void toggle()
    {
        Logmeishi.d("toggle");
        mHelper.toggle();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.slidingmenu.lib.app.SlidingActivityBase#showAbove()
     */
    public void showContent()
    {
        mHelper.showContent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.slidingmenu.lib.app.SlidingActivityBase#showBehind()
     */
    public void showMenu()
    {
        mHelper.showMenu();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.slidingmenu.lib.app.SlidingActivityBase#showSecondaryMenu()
     */
    public void showSecondaryMenu()
    {
        mHelper.showSecondaryMenu();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.slidingmenu.lib.app.SlidingActivityBase#setSlidingActionBarEnabled
     * (boolean)
     */
    public void setSlidingActionBarEnabled(boolean b)
    {
        mHelper.setSlidingActionBarEnabled(b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        boolean b = mHelper.onKeyUp(keyCode, event);
        if (b)
            return b;
        return super.onKeyUp(keyCode, event);
    }

}
