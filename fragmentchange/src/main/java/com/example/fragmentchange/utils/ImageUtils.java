package com.example.fragmentchange.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;
import android.support.v4.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/1/3.
 */

public class ImageUtils {
    //内存缓存大小
    public static final int MEMORY_CACHE_SIZE = 1024*1024*4;
    //磁盘缓存大小
    public static final int DISK_CACHE_SIZE = 1024*1024*16;
    //内存缓存
    private static LruCache<String,Bitmap> sMemoryCache;
    //磁盘缓存
    private static DiskLruCache sDisCache;

    //初始化缓存，在使用缓存前，必须调用该方法，至少调用一次
    public static void initCache(Context context){
        if(sMemoryCache ==null){
            sMemoryCache = new LruCache<>(MEMORY_CACHE_SIZE);
        }
        if(sDisCache ==null){
            try {
                sDisCache = DiskLruCache.open(getCacheFile(context),getVersionCode(context),
                        1,DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 获得缓存目录
     *
     */

    public static File getCacheFile(Context context){
        //判断sd卡有没有加上去
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //SD卡如果存在，就使用SD卡的路径
            return context.getExternalCacheDir();
        }
        //获得程序内部缓存目录
        return context.getCacheDir();
    }

    /**
     * 获得程序的版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  0 ;
    }

    /**
     * 将url地址转化为md5加密后的字符串
     * @param urlStr
     * @return
     * md5加密不可逆
     */
    public static String md5(String urlStr){
        try {
            //得到md5加密对象
            MessageDigest digest = MessageDigest.getInstance("md5");
            //调用update方法，对字节码进行加密
            digest.update(urlStr.getBytes());
            //获得加密后的结果
            byte[] digest1 = digest.digest();
            StringBuilder builder = new StringBuilder();
            //把加密后的字节码转换成文16进制的字符
            for (int i = 0; i <digest1.length ; i++) {
                builder.append(Integer.toHexString(Math.abs(digest1[i])));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存图片到一级缓存中
     * @param urlStr
     * @param bitmap
     */
    public static void saveToMemoryCache(String urlStr,Bitmap bitmap){
            sMemoryCache.put(md5(urlStr),bitmap);
    }

    /**
     * 从一级缓存中读取图片
     * @param urlStr
     * @return
     */
    public static Bitmap readFromMemoryCache(String urlStr){
        return sMemoryCache.get(md5(urlStr));
    }

    /**
     * 将图片保存到磁盘
     * @param urlStr
     * @param bitmap
     */
    public static void saveToDiskCache(String urlStr,Bitmap bitmap){
        try {
            DiskLruCache.Editor edit = sDisCache.edit(md5(urlStr));
            if(edit!=null){
                OutputStream outputStream = edit.newOutputStream(0);
                boolean compress =
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                if(compress){
                    edit.commit();
                }else{
                    edit.abort();;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从磁盘缓存中读取图片
     * @param urlStr
     * @return
     */
    public static Bitmap readFromDiskCache(String urlStr){
        try {
            DiskLruCache.Snapshot snapshot = sDisCache.get(md5(urlStr));
            if (snapshot !=null){
                InputStream inputStream = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                return bitmap;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 图片压缩方法
     * @param bytes  字节数组
     * @param destWidth     目标图片的宽
     * @param destHeight    目标图片的高
     * @return
     */
    public static Bitmap zipImage(byte[] bytes,int destWidth,int destHeight){
        //1.获得原始图片的宽和高
        //获取图片参数
        BitmapFactory.Options options= new BitmapFactory.Options();
        //设置只加载边框
        options.inJustDecodeBounds = true;
        //进行第一次加载
        BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        //获得图片的原始宽和高
        int Width= options.outWidth;
        int Height = options.outHeight;
        //2.计算压缩比例
        int simpleSize = 1;
        while ((Width/simpleSize >=destWidth) || (Height/simpleSize>=destHeight)){
            simpleSize *= 2;
        }
        //3.实际加载图片
        //设置不止加载边框
        options.inJustDecodeBounds = false;
        //设置压缩比例
        options.inSampleSize = simpleSize;
        //设置图片格式
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
        return bitmap;
    }

    /**
     *将输入流转为字节数组。
     * @param inputStream
     * @return
     */
    public static byte[] streamToByteArray(InputStream inputStream){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length = 0;
        try {
            while ((length = inputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,length);
            }
            outputStream.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}