package com.yxy.lib.base.http.call;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/9/5.
 */
public class OKHttpManager {

    private static OKHttpManager mInstance;
    private OkHttpClient client;
    private final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final MediaType MEDIA_STREM = MediaType.parse("application/octet-stream");

    private OKHttpManager() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public synchronized static OKHttpManager getInstance() {
        if (mInstance == null) {
            mInstance = new OKHttpManager();
        }
        return mInstance;
    }


    public Call buildPostCall(String url, String json) {
        Log.d("OKHttpManager", "url :" + url + ",json :" + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        return call;
    }

    public Call buildPostCall(String url, byte[] content) {
        Log.d("OKHttpManager", "url :" + url + " \ncontent len :" + content.length);
        RequestBody body = RequestBody.create(MEDIA_STREM, content);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        return call;
    }

    public Call buildPostFileCall(String url, JSONObject object) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            File file = new File(object.optString(key));
            RequestBody fileBody = RequestBody.create(MEDIA_STREM, file);
            builder.addFormDataPart(file.getName(), file.getName(), fileBody);
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        return call;
    }

    public Call buildGetFileCall(String url){
        Log.d("OKHttpManager", "buildGetFileCall url :" + url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        return call;
    }

}
