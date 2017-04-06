package com.yxy.lib.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yxy.lib.base.R;


/**
 * Created by Administrator on 2016/9/13.
 */
public class ToggleView extends LinearLayout {
    private RadioButton cb_left;
    private RadioButton cb_right;
    private RadioGroup radio;

    public ToggleView(Context context) {
        super(context);
        init(null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_toggle, this);
        cb_left = (RadioButton) findViewById(R.id.cb_left);
        cb_right = (RadioButton) findViewById(R.id.cb_right);
        radio = (RadioGroup) findViewById(R.id.radio);
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleView);
            CharSequence charSequence[] = array.getTextArray(R.styleable.ToggleView_tv_textArr);
            setTextArray(charSequence);
            array.recycle();
        }
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.cb_left){
                    cb_left.setTextColor(getResources().getColor(R.color.red));
                    cb_right.setTextColor(getResources().getColor(R.color.white));
                }else {
                    cb_left.setTextColor(getResources().getColor(R.color.white));
                    cb_right.setTextColor(getResources().getColor(R.color.red));
                }
                if (listener != null) {
                    listener.onToggle(i == R.id.cb_left);
                }
            }
        });
    }

    public void setTextArray(CharSequence charSequence[]) {
        if (charSequence != null && charSequence.length >= 2) {
            cb_left.setText(charSequence[0]);
            cb_right.setText(charSequence[1]);
        }
    }

    private OnToggleChangeListener listener;

    public void setOnToggleChangeListener(OnToggleChangeListener listener) {
        this.listener = listener;
    }

    public interface OnToggleChangeListener {
        void onToggle(boolean left);
    }

}
