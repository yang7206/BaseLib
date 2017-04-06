package com.yxy.lib.base.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yxy.lib.base.R;


/**
 * �����жԻ���
 *
 * @author yxy
 */
public class LoadingDialog extends BaseDialog {

    private TextView txt_des;

    private String des;

    private int loadingCount;

    private Object sync = new Object();

    public LoadingDialog(Context context) {
        super(context);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected View onInflateView(LayoutInflater inflater) {
        final ViewGroup root = null;
        View v = inflater.inflate(R.layout.dialog_loading, root,false);
//		ProgressBar progress_bar = (ProgressBar) v.findViewById(R.id.progress_bar);
//		progress_bar.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(getContext()).colors(getContext().getResources().getIntArray(R.array.colors)).build());

        txt_des = (TextView) v.findViewById(R.id.txt_des);
        if (!TextUtils.isEmpty(des)) {
            txt_des.setText(des);
            des = null;
        }
        return v;
    }

    @Override
    public void onBackPressed() {
    }

    private void setDescribeText(String text) {
        des = text;
        if (txt_des != null) {
            txt_des.setText(text);
        }
    }

    private void setDescribeText(int rid) {
        setDescribeText(getContext().getString(rid));
    }

    public final void loadStatusShow(String text) {
        synchronized (sync) {
            loadingCount++;
            if (loadingCount > 0) {
                if (TextUtils.isEmpty(text)) {
                    setDescribeText(R.string.lib_loading_msg);
                } else {
                    setDescribeText(text);
                }
                if (!isShowing()) {
                    show();
                }
            }
        }
    }

    public final void loadStatusShow(int textId) {
        String text;
        if (textId != 0) {
            text = getContext().getString(textId);
        } else {
            text = null;
        }
        loadStatusShow(text);
    }

    public final boolean loadStatusHide() {
        synchronized (sync) {
            loadingCount--;
            if (loadingCount < 0)
                loadingCount = 0;
            if (loadingCount == 0) {
                if (isShowing()) {
                    dismiss();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void show() {
        super.show();
        setWarpContent();
    }

}
