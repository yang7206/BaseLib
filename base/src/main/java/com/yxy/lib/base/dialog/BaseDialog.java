package com.yxy.lib.base.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.yxy.lib.base.R;
import com.yxy.lib.base.utils.DeviceUtils;



public abstract class BaseDialog extends Dialog implements android.view.View.OnClickListener {
	protected Context context;

	public BaseDialog(Context context) {
		super(context, R.style.dialog_style);
		this.context = context;
	}

	protected abstract View onInflateView(LayoutInflater inflater);

	protected void onInitView(View view) {
		// TODO: 子类实现
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = onInflateView(inflater);
		onInitView(view);

		setContentView(view);
	}

	@Override
	public void show() {
		super.show();
		setHPading30Content();
	}

	protected void setHPading30Content() {
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.width = (int) (DeviceUtils.getScreenWidth(context) - context.getResources().getDimension(R.dimen.dp30));
		lp.height = LayoutParams.WRAP_CONTENT;
		this.getWindow().setAttributes(lp);
	}

	protected void setWarpContent() {
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.width = LayoutParams.WRAP_CONTENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		this.getWindow().setAttributes(lp);
	}

	protected void setPaddingFullContent() {
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.width = (int) (DeviceUtils.getScreenWidth(context) - context.getResources().getDimension(R.dimen.dp30));
		lp.height = (int) (DeviceUtils.getScreenHeigth(context) - context.getResources().getDimension(R.dimen.dp50));
		this.getWindow().setAttributes(lp);
	}

	protected void setFullContent() {

		Rect frame = new Rect();
		((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.width = DeviceUtils.getScreenWidth(context);
		lp.height = DeviceUtils.getScreenHeigth(context) - statusBarHeight;
		this.getWindow().setAttributes(lp);
	}

	@Override
	public void onClick(View v) {
		// TODO: 子类实现
	}

	protected void setClickable(View parent, int... ids) {
		for (int i = 0; i < ids.length; i++) {
			View v = parent.findViewById(ids[i]);
			if (v != null) {
				v.setOnClickListener(this);
			}
		}
	}
}
