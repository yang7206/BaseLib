package com.yxy.lib.base.http.filedownload;

/**
 * Created by Administrator on 2016/10/20.
 */
public interface OnDownloadListener {

    void onStartDownload(String url);

    void onProgressUpdate(String url, long curr, long total);

    void onDownloadComplate(String url, String storePath);

    void onDownloadFail(String url, String msg, boolean isCancel);
}