package com.yxy.lib.base.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxy.lib.base.dialog.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/31.
 */
public abstract class BaseFragment extends Fragment {

    protected View root;
    private DialogHolder dialogHolder;
    private Handler mHandler;

    protected boolean isKitkatVersionUp(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dialogHolder = new DialogHolder();
        mHandler = new Handler(context.getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(fragmentResId(), null, false);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ButterKnife.bind(this, root);
        initView(savedInstanceState);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected void showLoading(String text) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading(text);
            return;
        }
        dialogHolder.showLoading(text);
    }

    protected void showLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading();
            return;
        }
        dialogHolder.showLoading();
    }

    protected void showLoading(int text) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading(text);
            return;
        }
        dialogHolder.showLoading(text);
    }

    protected void hideLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideLoading();
            return;
        }
        dialogHolder.hideLoading();
    }

    protected void hideLoadingDelay(long time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        }, time);
    }

    protected void postRunable(Runnable runnable,long time){
        mHandler.postDelayed(runnable, time);
    }

    protected void setResult(int resultCode, Intent data) {
        if (data != null) {
            getActivity().setResult(resultCode, data);
        } else {
            getActivity().setResult(resultCode);
        }
    }

    protected void setResult(int resultCode) {
        setResult(resultCode, null);
    }

    private class DialogHolder {

        private LoadingDialog loadingDialog;

        protected void showLoading(String text) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(getActivity());
            }
            loadingDialog.loadStatusShow(text);
        }

        protected void showLoading(int text) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(getActivity());
            }
            loadingDialog.loadStatusShow(text);
        }

        protected void showLoading() {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(getActivity());
            }
            loadingDialog.loadStatusShow("");
        }

        protected void hideLoading() {
            if (loadingDialog != null) {
                loadingDialog.loadStatusHide();
            }
        }
    }


    protected abstract int fragmentResId();

    protected abstract void initView(Bundle savedInstanceState);

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
