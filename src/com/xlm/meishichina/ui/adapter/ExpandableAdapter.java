package com.xlm.meishichina.ui.adapter;

import java.util.List;
import java.util.Map;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.BaseExpandableListAdapter;
import org.holoeverywhere.widget.TextView;

import com.xlm.meishichina.R;
import com.xlm.meishichina.bean.CateInfo;
import com.xlm.meishichina.bean.Category;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ExpandableAdapter extends BaseExpandableListAdapter
{

    private List<Category> mCategories;
    private Map<Integer, List<CateInfo>> map;
    private Context context;

    public ExpandableAdapter(Context context, List<Category> mCategories,
            Map<Integer, List<CateInfo>> map)
    {
        this.context = context;
        this.mCategories = mCategories;
        this.map = map;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return map.get(mCategories.get(groupPosition).getId())
                .get(childPosition).getDisplay_name();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent)
    {
        View view = LayoutInflater.inflate(context,
                R.layout.fragment_category_row_child);
        TextView textView = (TextView) view
                .findViewById(R.id.fragment_category_text);
        textView.setText(getChild(groupPosition, childPosition).toString());
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return map.get(mCategories.get(groupPosition).getId()).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return mCategories.get(groupPosition).getName();
    }

    @Override
    public int getGroupCount()
    {
        return mCategories.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent)
    {
        View view = LayoutInflater.inflate(context,
                R.layout.fragment_category_row_group);
        TextView textView = (TextView) view
                .findViewById(R.id.fragment_category_text);
        textView.setText(getGroup(groupPosition).toString());
        return view;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

}
