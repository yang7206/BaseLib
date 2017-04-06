package com.yxy.lib.base.app;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/9/6.
 */
public class CacheConfig {

    public final static String DIR = Environment.getExternalStorageDirectory() + File.separator + "com.huaxiang.health";
    public final static String DIR_BITMAP_CACHES = DIR + File.separator + "bitmap_caches";
    public final static String DIR_INFO_CACHES = DIR + File.separator + "info";
    public final static String DIR_APK = DIR + File.separator + "apk";
    public final static String DIR_SCREEN_BITMAP_CACHES = DIR + File.separator + "screen_bitmap_caches";
    public final static String DIR_GROUP_BITMAP_CACHES = DIR + File.separator + "bitmap_caches" + File.separator + "group";

    public final static String DIR_BG_CACHES = DIR + File.separator + "background_caches";
    public final static String DIR_EXCEPTION_CACHES = DIR + File.separator + "exception_caches";

}
