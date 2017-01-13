package com.example.fragmentchange.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.fragmentchange.R;
import com.example.fragmentchange.shililei.KaiFu;
import com.example.fragmentchange.utils.Constants;
import com.example.fragmentchange.utils.JSONLoader;
import com.example.fragmentchange.utils.KaiFuAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/27.
 */

public class KaifuAFragment extends Fragment {

    private ExpandableListView mExLv = null;
    private Map<String,List<String>> mData;
    private JSONLoader mJSONLoader;
    //实体类对象
    private KaiFu mKaiFu;
    private KaiFuAdapter mKaiFuAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.akaifu,container,false);
//        initview(view);
//        initData();
        return view;
    }

    private void initData() {
        mData = new LinkedHashMap<>();
        mJSONLoader.loadJSON(Constants.KAIFU_URL,
                new JSONLoader.OnJSONLoadListener() {
            @Override
            public void onJSONLoadComplete(String json) {
                Gson gson = new Gson();
                mKaiFu = gson.fromJson(json,KaiFu.class);
                List<String> group1 = new ArrayList<>();
                group1.add(mKaiFu.getInfo().toString());
            }
        });

    }

    private void initview(View view) {
        mExLv = (ExpandableListView)view.findViewById(R.id.kaifu_Ex_listview);
//        mKaiFuAdapter = new KaiFuAdapter(getActivity(),);
    }

}
