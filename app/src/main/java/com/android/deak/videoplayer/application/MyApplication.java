package com.android.deak.videoplayer.application;

import android.app.Application;

import com.android.deak.videoplayer.BuildConfig;
import com.android.deak.videoplayer.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.DbManager;
import org.xutils.x;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * 自定义application
 */
public class MyApplication extends Application {
    public static DisplayImageOptions sImageOptions;
    public static DisplayImageOptions sImageNoRoundOptions;
    public static DbManager.DaoConfig sDaoConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)//构建者，链式条用
                .memoryCacheSizePercentage(10)
                .memoryCacheExtraOptions(400, 800)
                .diskCacheExtraOptions(400, 800, null)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(200)
                .build();
        ImageLoader.getInstance().init(config);
        sImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true)//储存在缓存
                .cacheOnDisk(true)//储存在SD卡
                .resetViewBeforeLoading(true)//在加载图片之前把IMAGE复位
                .showImageForEmptyUri(R.mipmap.zwtp)//如果没有图片应该显示的图片
                .showImageOnFail(R.mipmap.zwtp)//如果加载失败显示的图片
                .showImageOnLoading(R.mipmap.zwtp)//正在加载显示的图片
                .build();
        sImageNoRoundOptions = new DisplayImageOptions.Builder().cacheInMemory(true)//储存在缓存
                .cacheOnDisk(true)//储存在SD卡
                .resetViewBeforeLoading(true)//在加载图片之前把IMAGE复位
                .showImageForEmptyUri(R.mipmap.zwtp)//如果没有图片应该显示的图片
                .showImageOnFail(R.mipmap.zwtp)//如果加载失败显示的图片
                .showImageOnLoading(R.mipmap.zwtp)//正在加载显示的图片
                .build();
        initBmob();
        initDb();
    }

    private void initDb() {
        sDaoConfig = new DbManager.DaoConfig()
                .setDbName("test.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbVersion(2)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                });
    }

    private void initBmob() {
        //第一：默认初始化
        Bmob.initialize(this, "Your Application ID");

//        第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        //设置appkey
        .setApplicationId("Your Application ID")
        //请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        //文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        //文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
    }
}
