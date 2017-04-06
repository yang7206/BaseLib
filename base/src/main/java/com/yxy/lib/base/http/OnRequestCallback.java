package com.yxy.lib.base.http;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface OnRequestCallback<T> {
    // 成功
    int RESULT_SUCCESS = 0;
    // 重试
    int RESULT_FAIL_RETRY_LAYTER = -2;
    // 解析错误
    int RESULT_PARSE_FAIL = -3;

    String RESULT_MSG = "success";

    void onResult(int status, String msg, T t);

}
