package com.yxy.lib.base.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenShotUtil {

    public static Bitmap takeScreenShot(Activity activity) {
        // View是你�?��截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状�?栏高�?
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int bottomBarHeight = frame.bottom;


        // 获取屏幕长和�?
        int width = DeviceUtils.getScreenWidth(activity);
        // 去掉标题�?
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, bottomBarHeight - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static boolean savePic(Bitmap b, File filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void shoot(Activity activity, File filePath, OnScreenShotCompleteListener l) {
        if (filePath == null) {
            return;
        }
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        try {
            Bitmap bitmap = takeScreenShot(activity);
            if (bitmap == null && l != null) {
                l.onShotComplete(false, null);
                return;
            }
            savePic(bitmap, filePath);
            if (l != null) {
                l.onShotComplete(true, bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (l != null) {
                l.onShotComplete(false, null);
            }
        }
    }

    public interface OnScreenShotCompleteListener {
        void onShotComplete(boolean isSuccess, Bitmap b);
    }
}
