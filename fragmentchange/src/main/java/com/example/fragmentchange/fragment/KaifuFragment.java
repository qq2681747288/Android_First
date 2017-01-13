package com.example.fragmentchange.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fragmentchange.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class KaifuFragment extends Fragment {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kaifu,container,false);
        initFragment();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mViewPager = (ViewPager)view.findViewById(R.id.kaifu_vp);
        mTabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        //初始化ViewPager, 在Fragment中要获得FragmentManager，要使用getChildFragmentManager
        mViewPager.setAdapter(new MyFragementsAdapter(getChildFragmentManager()));
//        //设置TabLayout和ViewPager的联动
//        mTabLayout.setupWithViewPager(mViewPager);
//        for (int i = 0; i <mTabLayout.getTabCount() ; i++) {
//            mTabLayout.getTabAt(i).setText("按钮-->"+(i+1));
//        }
        //手动添加按钮
        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setText("开服");
        mTabLayout.addTab(tab1);
        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setText("开测");
        mTabLayout.addTab(tab2);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new KaifuAFragment());
        mFragments.add(new KaifuBFragment());
    }

    class MyFragementsAdapter extends FragmentPagerAdapter{

        public MyFragementsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
