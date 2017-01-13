package com.example.fragmentchange.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragmentchange.R;
import com.example.fragmentchange.shililei.Gift;
import com.example.fragmentchange.shililei.KaiCe;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */

public class KaiCeAdapter extends BaseAdapter {

    //资源数组
    private List<KaiCe.InfoBean> mKaiCe;
    //视图填充器
    private LayoutInflater mInflater;
    //上下文
    private Context mContext;
    //图片处理
    private ImageLoader mImageLoader;

    public KaiCeAdapter(List<KaiCe.InfoBean> mKaiCe, Context mContext) {
        this.mKaiCe = mKaiCe;
//        Log.i("==vvvv=", mGift.toString());
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        mImageLoader = new ImageLoader();
    }

    @Override
    public int getCount() {
        return mKaiCe == null ? 0 : mKaiCe.size();
    }

    @Override
    public Object getItem(int position) {
        return mKaiCe.get(position);
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
            view = mInflater.inflate(R.layout.b_kaifu_text, parent, false);
            mHolder = new ViewHolder(view);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        KaiCe.InfoBean kaice = mKaiCe.get(position);
        mHolder.mKaice_gname.setText(kaice.getGname());
        mHolder.mKaice_operators.setText("运营商:"+kaice.getOperators());
        mHolder.mKaice_addtime.setText(kaice.getAddtime());

        //在图片加载之前放一张默认图片
        mHolder.mImageView.setImageResource(R.mipmap.ic_launcher);
        //把图片地址保存到ImageView的Tag中,后面做比较，如果地址一样就不加载
        mHolder.mImageView.setTag(Constants.BASE_URL+kaice.getIconurl());

        mImageLoader.loadImage(Constants.BASE_URL+kaice.getIconurl(), new ImageLoader.OnImageLoadListener() {
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
        TextView mKaice_gname;
        TextView mKaice_operators;
        TextView mKaice_addtime;

        public ViewHolder(View view) {
            mImageView = (ImageView) view.findViewById(R.id.kaice_image_view);
            mKaice_gname = (TextView) view.findViewById(R.id.kaice_gname);
            mKaice_operators = (TextView) view.findViewById(R.id.kaice_operators);
            mKaice_addtime = (TextView) view.findViewById(R.id.kaice_addtime);
            view.setTag(this);
        }
    }
}
