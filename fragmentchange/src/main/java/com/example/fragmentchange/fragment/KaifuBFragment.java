package com.example.fragmentchange.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fragmentchange.R;
import com.example.fragmentchange.shililei.KaiCe;
import com.example.fragmentchange.utils.Constants;
import com.example.fragmentchange.utils.JSONLoader;
import com.example.fragmentchange.utils.KaiCeAdapter;
import com.example.fragmentchange.utils.ToAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class KaifuBFragment extends Fragment {
    //实体类对象
    private KaiCe mKaiCe;
    //适配器
    private KaiCeAdapter mAdapter;
    //数据数组
    private List<KaiCe.InfoBean> mListKaiCe;
    //ListView视图
    private PullToRefreshListView mListView;
    //JsonLoader工具
    private JSONLoader mJSONLoader;
    //创建handler对象
    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bkaifu,container,false);
        initView(view);
        initData();
        return view;


    }

    private void initData() {
        mJSONLoader = new JSONLoader();
        mJSONLoader.loadJSON(Constants.KAICE_URL,
                new JSONLoader.OnJSONLoadListener() {
                    @Override
                    public void onJSONLoadComplete(String json) {
                        Gson gson = new Gson();
                        mKaiCe = gson.fromJson(json,KaiCe.class);

                        List<KaiCe.InfoBean> list = mKaiCe.getInfo();
                        mListKaiCe.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initView(View view) {
        mKaiCe = new KaiCe();
        mHandler = new Handler();
        mListKaiCe = new ArrayList<>();
        mAdapter = new KaiCeAdapter(mListKaiCe,getContext());
        mListView = (PullToRefreshListView) view.findViewById(R.id.b_list_view);
        mListView.setAdapter(mAdapter);
        //设置刷新模式
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                        Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                },1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                        Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                },1000);
            }
        });
    }
}
