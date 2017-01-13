package com.example.fragmentchange.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragmentchange.R;
import com.example.fragmentchange.shililei.Gift;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ToAdapter extends BaseAdapter {

    //资源数组
    private List<Gift.ListBean> mGift;
    //视图填充器
    private LayoutInflater mInflater;
    //上下文
    private Context mContext;
    //图片处理
    private ImageLoader mImageLoader;

    public ToAdapter(List<Gift.ListBean> mGift, Context mContext) {
        this.mGift = mGift;
//        Log.i("==vvvv=", mGift.toString());
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        mImageLoader = new ImageLoader();
    }

    @Override
    public int getCount() {
        return mGift == null ? 0 : mGift.size();
    }

    @Override
    public Object getItem(int position) {
        return mGift.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder mHolder;
        if (null == view) {
            view = mInflater.inflate(R.layout.libaotext, parent, false);
            mHolder = new ViewHolder(view);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        Gift.ListBean gift = mGift.get(position);
        mHolder.mLibao_giftname.setText(gift.getGiftname());
        mHolder.mLibao_gname.setText(gift.getGname());
        mHolder.mLibao_number.setText(""+gift.getNumber());

        //在图片加载之前放一张默认图片
        mHolder.mImageView.setImageResource(R.mipmap.ic_launcher);
        //把图片地址保存到ImageView的Tag中,后面做比较，如果地址一样就不加载
        mHolder.mImageView.setTag(Constants.BASE_URL+gift.getIconurl());

        mImageLoader.loadImage(Constants.BASE_URL+gift.getIconurl(), new ImageLoader.OnImageLoadListener() {
            @Override
            public void onImageLoadComplete(Bitmap bitmap, String urlStr) {
                if (bitmap != null) {
                    //把当前加载图片的地址和tag值进行比较
                    if (urlStr.equals(mHolder.mImageView.getTag())) {
                        mHolder.mImageView.setImageBitmap(bitmap);
                    }
                }
            }
        });
        return view;
    }

    class ViewHolder {
        ImageView mImageView;
        TextView mLibao_gname;
        TextView mLibao_giftname;
        TextView mLibao_number;

        public ViewHolder(View view) {
            mLibao_giftname = (TextView) view.findViewById(R.id.libao_gift_name);
            mLibao_gname = (TextView) view.findViewById(R.id.libao_gname);
            mLibao_number = (TextView) view.findViewById(R.id.libao_number);
            mImageView = (ImageView) view.findViewById(R.id.libao_imageview);
            view.setTag(this);
        }
    }
}
