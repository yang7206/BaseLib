package com.yxy.lib.base.http.filedownload;

import android.content.Context;

import com.yxy.lib.base.http.encrypt.Md5;

import java.util.HashMap;
import java.util.Map;

import com.yxy.lib.base.http.HttpEnvironment;

/**
 * Created by Administrator on 2016/10/20.
 */
public class FileDownloaderManager {

    private static FileDownloaderManager mInstance;
    private Context context;

    public void initContext(Context context) {
        this.context = context;
    }

    private FileDownloaderManager() {
    }

    public synchronized static FileDownloaderManager getInstance() {
        if (mInstance == null) {
            mInstance = new FileDownloaderManager();
        }
        return mInstance;
    }


    private Map<String, DownloadTask> callMaps = new HashMap<>();

    private String getDownloaddInfoMd5(String url, String storePath) {
        return Md5.getMD5CodeHex((url + storePath).getBytes());
    }

    public DownloadTask startDownload(String url, String storePath, OnDownloadListener listener) {
        String key = getDownloaddInfoMd5(url, storePath);
        if (callMaps.containsKey(key)) {
            DownloadTask task = callMaps.get(key);
            task.addListener(listener);
            return task;
        }
        DownloadTask task = new DownloadTask(this, context, url, storePath, listener);
        callMaps.put(key, task);
        HttpEnvironment.getInstance().submit(task);
        return task;
    }

    void removeTask(DownloadTask task) {
        String key = getDownloaddInfoMd5(task.getUrl(), task.getStorePath());
        callMaps.remove(key);
    }

    public void cancelTask(DownloadTask task) {
        if (task == null) return;
        task.cancelTask();
        String key = getDownloaddInfoMd5(task.getUrl(), task.getStorePath());
        callMaps.remove(key);
    }


}

