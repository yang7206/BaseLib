package com.yxy.lib.base.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.yxy.lib.base.utils.JsonUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Response;

public abstract class BaseProtocolCoder<T> implements Runnable {
    private static final String TAG = "BaseProtocolCoder";
    private OnRequestCallback<T> mListener;
    private Handler mHandler;
    private boolean isDone = false;
    private Call coderCall;
    protected Context context;
    protected String url;
    protected String method;
    protected Gson gson;

    public BaseProtocolCoder(Context context) {
        this.context = context;
        mHandler = new Handler(context.getMainLooper());
    }

    protected void setMethod(String method) {
        this.method = method;
    }

    protected void setUrl(String interfaceName, String url) {
        this.url = url + interfaceName;
    }

    public void setOnRequestCallback(OnRequestCallback<T> listener) {
        mListener = listener;
    }


    /**
     * 准备包体
     */
    protected abstract void onBodyData();

    protected boolean isToastError() {
        return true;
    }


    private void initGson() {
        gson = new Gson();
    }

    @Override
    public void run() {
        initGson();
        onBodyData();
        if (!isDone) {
            coderCall = buildRequestCall();
            if (coderCall != null) {
                performRequest();
            } else {
                callbackFail(OnRequestCallback.RESULT_PARSE_FAIL, "参数异常");
            }
        }
    }

    protected abstract Call buildRequestCall() ;

    /**
     * 执行请求
     */
    private void performRequest() {
        try {
            Response response = coderCall.execute();
            parseResp(response);
        } catch (ConnectException e) {
            e.printStackTrace();
            callbackFail(OnRequestCallback.RESULT_FAIL_RETRY_LAYTER, "网络连接异常");
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            callbackFail(OnRequestCallback.RESULT_FAIL_RETRY_LAYTER, "网络连接超时");
        } catch (IOException e) {
            e.printStackTrace();
            callbackFail(OnRequestCallback.RESULT_FAIL_RETRY_LAYTER, "网络连接中断");
        } catch (Exception e) {
            e.printStackTrace();
            callbackFail(OnRequestCallback.RESULT_FAIL_RETRY_LAYTER, e.getMessage());
        }
    }


    /**
     * 解析回应包
     *
     * @param response
     * @throws Exception
     */
    protected abstract void parseResp(Response response) throws Exception;

    public void execute() {
        execute(this);
    }

    protected void execute(Runnable runnable) {
        if (runnable != null) {
            HttpEnvironment.getInstance().submit(runnable);
        }
    }

    protected void callbackSuccess(T t) {
        isDone = true;
        callbackResult(OnRequestCallback.RESULT_SUCCESS, OnRequestCallback.RESULT_MSG, t);
    }

    protected void callbackFail(int state, final String errorMsg) {
        isDone = true;
        if (isToastError() && !TextUtils.isEmpty(errorMsg)) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
        callbackResult(state, errorMsg, null);
    }

    protected void callbackResult(final int state, final String errorMsg, final T t) {
        Log.i(TAG, "callbackResult");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "mListener.onResult");
                if (mListener != null) {
                    mListener.onResult(state, errorMsg, t);
                }
            }
        });
    }

    protected void callbackSuccessSingleObject(JsonArray jsonArray, Class<T> cls) {
        callbackSuccess(JsonUtil.toObjectByJsonElement(jsonArray.get(0), cls));
    }


}
