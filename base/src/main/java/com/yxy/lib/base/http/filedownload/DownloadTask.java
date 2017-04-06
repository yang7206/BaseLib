package com.yxy.lib.base.http.filedownload;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yxy.lib.base.http.call.OKHttpManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/10/20.
 */
public class DownloadTask implements Runnable {
    private String mUrl;
    private String storePath;
    private List<OnDownloadListener> listeners = new ArrayList<>();
    private int status = STATUS_INIT;
    private static final int STATUS_INIT = 0;
    private static final int STATUS_START = 1;
    private static final int STATUS_DOWNLOADING = 2;
    private static final int STATUS_DONE = 3;
    private static final int STATUS_FAIL = 4;
    private static final int STATUS_START_SINGLE = 5;
    private static final int STATUS_DOWNLOADING_SINGLE = 6;
    private static final int STATUS_DONE_SINGLE = 7;
    private static final int STATUS_FAIL_SINGLE = 8;
    private Handler mHandler;

    private boolean isCancel = false;
    private String errorMsg = null;
    private long curr;
    private long total;
    private FileDownloaderManager manager;

    DownloadTask(FileDownloaderManager manager, Context context, String url, String storePath, OnDownloadListener listener) {
        this.mUrl = url;
        this.manager = manager;
        this.storePath = storePath;
        listeners.add(listener);
        mHandler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case STATUS_START:
                        callbackStart();
                        break;
                    case STATUS_DOWNLOADING:
                        callbackProgressUpdate(msg.arg1);
                        break;
                    case STATUS_DONE:
                        callbackDownloadComplate();
                        break;
                    case STATUS_FAIL:
                        Log.d("DownloadTask", "callbackFail");
                        callbackFail();
                        break;
                    case STATUS_START_SINGLE:
                        callbackStartSingle((OnDownloadListener) msg.obj);
                        break;
                    case STATUS_DOWNLOADING_SINGLE:
                        callbackDownloadingSingle((OnDownloadListener) msg.obj);
                        break;
                    case STATUS_DONE_SINGLE:
                        callbackDoneSingle((OnDownloadListener) msg.obj);
                        break;
                    case STATUS_FAIL_SINGLE:
                        callbackFailSingle((OnDownloadListener) msg.obj);
                        break;
                }
            }
        };
    }

    protected void addListener(OnDownloadListener listener) {
        listeners.add(listener);
        switch (status) {
            case STATUS_START:
                mHandler.sendEmptyMessage(STATUS_START_SINGLE);
                break;
            case STATUS_DOWNLOADING:
                mHandler.sendEmptyMessage(STATUS_DOWNLOADING_SINGLE);
                break;
            case STATUS_DONE:
                mHandler.sendEmptyMessage(STATUS_DONE_SINGLE);
                break;
            case STATUS_FAIL:
                mHandler.sendEmptyMessage(STATUS_FAIL_SINGLE);
                break;
        }
    }

    private void callbackStartSingle(OnDownloadListener listener) {
        if (listener == null) return;
        listener.onStartDownload(mUrl);
    }

    private void callbackDownloadingSingle(OnDownloadListener listener) {
        if (listener == null) return;
        listener.onProgressUpdate(mUrl, curr, total);
    }

    private void callbackDoneSingle(OnDownloadListener listener) {
        if (listener == null) return;
        listener.onDownloadComplate(mUrl, storePath);
    }

    private void callbackFailSingle(OnDownloadListener listener) {
        if (listener == null) return;
        listener.onDownloadFail(mUrl, errorMsg, isCancel);
    }

    private boolean isCancel() {
        if (isCancel) {
            errorMsg = "已取消";
            sendHandlerMessage(STATUS_FAIL);
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        Call call = OKHttpManager.getInstance().buildGetFileCall(mUrl);
        try {
            sendHandlerMessage(STATUS_START);
            if (isCancel()) {
                call.cancel();
                return;
            }
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                total = body.contentLength();
                if (isCancel()) {
                    call.cancel();
                    return;
                }
                InputStream inputStream = body.byteStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                File file = new File(storePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                byte[] _8K = new byte[8192];
                int read;
                if (isCancel()) {
                    call.cancel();
                    return;
                }
                int allread = 0 ;
                while ((read = bis.read(_8K)) != -1 && !isCancel) {
                    allread += read;
                    bos.write(_8K, 0, read);
                    sendProgressHandlerMessage(STATUS_DOWNLOADING,allread);
                }
                bos.flush();
                bos.close();
                bis.close();
                if (isCancel()) {
                    call.cancel();
                    return;
                }
                if (!isCancel) {
                    sendHandlerMessage(STATUS_DONE);
                    return;
                } else {
                    errorMsg = "已取消";
                }
            } else {
                errorMsg = response.message();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendHandlerMessage(STATUS_FAIL);
    }

    private void sendHandlerMessage(int status) {
        this.status = status;
        mHandler.sendEmptyMessage(status);
    }

    private void sendProgressHandlerMessage(int status,long curr) {
        this.status = status;
        mHandler.obtainMessage(status,(int)curr,0).sendToTarget();
    }

    private void callbackStart() {
        for (int i = 0; i < listeners.size(); i++) {
            OnDownloadListener listener = listeners.get(i);
            listener.onStartDownload(mUrl);
        }
    }

    private void callbackFail() {
        Log.d("DownloadTask", "listeners size :" + listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            OnDownloadListener listener = listeners.get(i);
            listener.onDownloadFail(mUrl, errorMsg, isCancel);
        }
        manager.removeTask(this);
    }

    private void callbackProgressUpdate(int read) {
        Log.d("DownloadTask", "listeners size :" + listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            OnDownloadListener listener = listeners.get(i);
            listener.onProgressUpdate(mUrl, read, total);
        }
    }

    private void callbackDownloadComplate() {
        for (int i = 0; i < listeners.size(); i++) {
            OnDownloadListener listener = listeners.get(i);
            listener.onDownloadComplate(mUrl, storePath);
        }
        manager.removeTask(this);
    }

    protected void cancelTask() {
        isCancel = true;
    }

    protected String getUrl() {
        return mUrl;
    }

    protected String getStorePath() {
        return storePath;
    }


}
