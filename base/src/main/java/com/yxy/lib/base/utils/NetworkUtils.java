package com.yxy.lib.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * 缃戠粶宸ュ叿绫�
 * 
 * @author yxy
 *
 */
public class NetworkUtils {
	
	private static boolean isProxyNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
        NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
        if (mobNetInfo == null || mobNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return false;
        }
        
        if (mobNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
        	return hasProxySetting();
        }
        
        return false;
	}
	
	/**
	 * 缃戠粶鍙敤
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isNetActivie(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
		NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
		return mobNetInfo != null && mobNetInfo.isConnected();
	}
	
	/**
	 * WIFI鍙敤
	 * @return
	 */
	public static boolean isWifiActive(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
        NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
		return mobNetInfo != null && mobNetInfo.getType() == ConnectivityManager.TYPE_WIFI;

	}
	
	/**
	 * 绉诲姩鍗″彲鐢�
	 * @param context
	 * @return
	 */
	public static boolean isMobileActive(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
        NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
		return mobNetInfo != null && mobNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;

	}
	
	/**
	 * 鏄惁鏈変唬鐞嗚缃�
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static boolean hasProxySetting() {
		return !TextUtils.isEmpty(android.net.Proxy.getDefaultHost()) &&
				android.net.Proxy.getDefaultPort() != -1;
	}
	
	public static boolean ifNetworkEnable(Context context) {
		return NetworkUtils.isNetActivie(context);
	}
}
