package com.yxy.lib.base.app;

import android.app.Application;

/**
 * Created by Administrator on 2016/11/9.
 */
public class LibApplication {

    private static Application mApplication;

    public static void setApplication(Application application){
        mApplication = application;
    }

    public static Application getApplication(){
        return mApplication;
    }


}
