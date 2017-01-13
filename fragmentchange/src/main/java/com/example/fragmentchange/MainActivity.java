package com.example.fragmentchange;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.example.fragmentchange.fragment.KaifuFragment;
import com.example.fragmentchange.fragment.LibaoFragment;
import com.example.fragmentchange.fragment.RemenFragment;
import com.example.fragmentchange.fragment.TeseFragment;

public class MainActivity extends AppCompatActivity  {

    private Fragment mKaifuFrag;
    private Fragment mLibaoFrag;
    private Fragment mRemenFrag;
    private Fragment mTeseFrag;
    private FragmentManager mFragmentMar;
    //当前显示页面
    private Fragment mCurrentFragment;
    //按键初始化
    private RadioButton mLiBaoButton;
    private RadioButton mReMenButton;
    private RadioButton mTeSeButton;
    private RadioButton mKaiFuButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();
    }

    private void initFragment() {
        mKaifuFrag = new KaifuFragment();
        mLibaoFrag = new LibaoFragment();
        mRemenFrag = new RemenFragment();
        mTeseFrag = new TeseFragment();
        mFragmentMar = getSupportFragmentManager();
        mFragmentMar.beginTransaction().add(R.id.frame_layout,mLibaoFrag).commit();
        //当前显示的是
        mCurrentFragment = mLibaoFrag;
    }

    //使用show和hide来优化显示
    public void switchFragment(Fragment newFrag){
        //如果新的视图已经添加，隐藏原来的视图，显示新视图
        if(newFrag.isAdded()){
            mFragmentMar.beginTransaction().hide(mCurrentFragment).show(newFrag).commit();
        }else {
            //没有添加的话，隐藏原来视图，添加新视图
            mFragmentMar.beginTransaction().hide(mCurrentFragment).
                    add(R.id.frame_layout,newFrag).commit();
        }
        //把新碎片设为当前碎片
        mCurrentFragment = newFrag;
    }

    private void initView() {
            mLiBaoButton = (RadioButton) findViewById(R.id.libao);
            mTeSeButton = (RadioButton) findViewById(R.id.tese);
            mReMenButton = (RadioButton) findViewById(R.id.remen);
            mKaiFuButton = (RadioButton) findViewById(R.id.kaifu);

        mLiBaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(mLibaoFrag);
            }
        });
        mTeSeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(mTeseFrag);
            }
        });
        mKaiFuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(mKaifuFrag);
            }
        });
        mReMenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(mRemenFrag);
            }
        });
    }



}
