package com.yxy.lib.base.location;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yxy.lib.base.app.LibApplication;

/**
 * Created by Administrator on 2015/11/20.
 */
public class AMapLocationManager implements AMapLocationListener {

    private AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;

    private static AMapLocationManager mInstance;

    private AMapLocationManager() {
    }

    public synchronized static AMapLocationManager getInstance() {
        if (mInstance == null) {
            mInstance = new AMapLocationManager();
        }
        return mInstance;
    }

    public void startLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(LibApplication.getApplication());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);

        //声明mLocationOption对象
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        Log.d("AMapLocationManager", "startLocation");
    }


    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }


    public void destory() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }

    private AMapLocation amapLocation;

    public AMapLocation getLastAMapLocation() {
        return amapLocation;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            this.amapLocation = amapLocation;
        }
        if (mAMapLocationListener != null) {
            mAMapLocationListener.onLocationChanged(amapLocation);
        }
    }

    private AMapLocationListener mAMapLocationListener;

    public void setAMapLocationListener(AMapLocationListener l) {
        this.mAMapLocationListener = l;
    }

}
