package com.example.fragmentchange.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.example.fragmentchange.R;
import com.example.fragmentchange.shililei.Gift;
import com.example.fragmentchange.utils.Constants;
import com.example.fragmentchange.utils.ImageLoader;
import com.example.fragmentchange.utils.JSONLoader;
import com.example.fragmentchange.utils.ToAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/27.
 */

public class LibaoFragment extends Fragment {
    private List<Gift.ListBean> listBeen;
    private ViewPager mViewPager;
    //存放视图的集合
    private List<View> mView;
    //定义圆点集合--因为圆点是一个个图片，所以用imageview
    private List<ImageView> mDots;
    //显示圆点的布局
    private LinearLayout mDotLayout;
    private Gift mGift;
    private JSONLoader mJSONLoader;
    //设置原来选中圆点的索引
    private int mCurrentDot = 0;
    //设置选中视图的索引
    private int mCurrentPager = 0;
    private Handler mHandler;

    private PullToRefreshListView mListView;

    private ToAdapter mToAdapter;
    //上拉下拉刷新-分页加载
    private int mPullCurrentPage = 0 ;
    //侧滑菜单
    private SlidingPaneLayout mSlidingPane;
    //侧滑视图。1：
    private View mSPview;
    //定义ActionBar---标题按钮
    private ActionBar mActionBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.libao, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        mJSONLoader = new JSONLoader();

        mHandler = new Handler();
        mJSONLoader.loadJSON(Constants.GIFT_URL + 1,
                new JSONLoader.OnJSONLoadListener() {
                    @Override
                    public void onJSONLoadComplete(String json) {
                        Gson gson = new Gson();
                        mGift = gson.fromJson(json, Gift.class);
//                        Log.i("xxx", "onJSONLoadComplete: "+mGift.getAd());
                        List<Gift.ListBean> list = mGift.getList();
                        listBeen.addAll(list);
                        mToAdapter.notifyDataSetChanged();
//                        Log.i("xxx", "onJSONLoadComplete: " + mGift.getList());


                        initPagerView(mGift.getAd());
                        mViewPager.setAdapter(new BannerAdapter());


                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mCurrentPager == mGift.getAd().size()) {
                                    mCurrentPager = 0;
                                } else {
                                    mCurrentPager++;
                                }
                                mViewPager.setCurrentItem(mCurrentPager);
                                mHandler.postDelayed(this, 3000);
                            }
                        }, 2000);
                        initDots(mGift.getAd().size());


                    }
                });

    }

    private void initView(View view) {
        mGift = new Gift();
        listBeen = new ArrayList<>();
        //初始化侧滑
        mSlidingPane = (SlidingPaneLayout) view.findViewById(R.id.sliding_pane);
        //侧滑视图1：
        mSPview = view.findViewById(R.id.ce_hua);
        mListView = (PullToRefreshListView) view.findViewById(R.id.libao_list_view);
        mToAdapter = new ToAdapter(listBeen, getContext());
//        Log.i("===", mGift.getList().toString());
        mListView.setAdapter(mToAdapter);
        //设置刷新模式  Mode.BOTH :上拉下拉都有效果
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        //设置刷新监听
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //PULLDown---下拉刷新
                mPullCurrentPage = 0;
                //网络加载和UI更新
                //............
                //停止刷新
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                    }
                },1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //PULLUP---上拉加载更多
                mPullCurrentPage++;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                    }
                },1000);
            }
        });

        mSlidingPane.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //slideOffset ---偏移量
                mSPview.setScaleX(1- slideOffset * 0.6f);
                mSPview.setScaleY(1- slideOffset * 0.6f);
            }

            @Override
            public void onPanelOpened(View panel) {

            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mDotLayout = (LinearLayout) view.findViewById(R.id.dot_layout);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mDots.get(mCurrentDot).setEnabled(true);
                mDots.get(position).setEnabled(false);
                mCurrentDot = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //适配器
    class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mView == null ? 0 : mView.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mView.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mView.get(position));
            return mView.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    //初始化ViewPager的所有子控件
    private void initPagerView(List<Gift.AdBean> ads) {
        mView = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (Gift.AdBean ad : ads) {
            //加载视图
            View view = inflater.inflate(R.layout.banner_image, null);
            //加载imageview控件
            final ImageView imageview = (ImageView) view.findViewById(R.id.banner_image_view);

            //进行图片的异步加载
            ImageLoader.loadImage(Constants.BASE_URL + ad.getIconurl(),
                    new ImageLoader.OnImageLoadListener() {
                        @Override
                        public void onImageLoadComplete(Bitmap bitmap, String urlStr) {
                            Log.i("xxx", "onImageLoadComplete: " + bitmap);
                            if (bitmap != null) {
                                imageview.setImageBitmap(bitmap);
                            }
                        }
                    });
            mView.add(view);
        }
    }

    //初始化圆点的集合
    private void initDots(int count) {
        mDots = new ArrayList<>();
        //创建布局参数
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
        //设置左右间距（左，上，右，下）
        lp.setMargins(5, 0, 5, 0);
        for (int i = 0; i < count; i++) {
            //创建imageview
            ImageView imageview = new ImageView(getContext());
            //将选择器作为图片
            imageview.setImageResource(R.drawable.dots);
            //添加到集合
            mDots.add(imageview);
            //添加到视图
            mDotLayout.addView(imageview, lp);
        }
        //设置默认选中第一个圆点
        mDots.get(0).setEnabled(false);
    }

    //设置搜索按键

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }
}
