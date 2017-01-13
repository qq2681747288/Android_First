package com.example.fragmentchange.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/10.
 */

public class KaiFuAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<String,List<String>> mData;

    private KaiFuAdapter(Context mContext,Map<String,List<String>> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    //获得组数量,也就是键的数量
    @Override
    public int getGroupCount() {
        return mData.keySet().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        String key = (String) mData.keySet().toArray()[groupPosition];
        List<String> list = mData.get(key);
        return list == null? 0:list.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //获得组视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String group = (String) mData.keySet().toArray()[groupPosition];
        TextView textView = new TextView(mContext);
        textView.setText(group);
        textView.setTextColor(Color.GREEN);
        textView.setTextSize(23);
        return textView;
    }

    //获得组中的子视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String group = (String) mData.keySet().toArray()[groupPosition];
        List<String> list = mData.get(group);
        String child = list.get(childPosition);
        TextView textView = new TextView(mContext);
        textView.setText(child);
        textView.setTextSize(15);

        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
