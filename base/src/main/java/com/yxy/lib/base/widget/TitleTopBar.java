package com.yxy.lib.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxy.lib.base.R;

/**
 * Created by Administrator on 2016/8/31.
 */
public class TitleTopBar extends LinearLayout {
    public TitleTopBar(Context context) {
        super(context);
        init(null);
    }

    public TitleTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    TextView tv_title;
    View layout_root;
    LinearLayout parent_left;
    ImageView iv_back;
    TextView tv_back_text;
    LinearLayout parent_right;
    ImageView iv_title_down;
    ImageView iv_title_bg;
    ImageView iv_right;
    TextView tv_right;
    ToggleView toggle_view;

    private void initView(){
        layout_root = findViewById(R.id.layout_root);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back_text = (TextView) findViewById(R.id.tv_back_text);
        tv_right = (TextView) findViewById(R.id.tv_right);
        toggle_view = (ToggleView) findViewById(R.id.toggle_view);
        parent_left = (LinearLayout)findViewById(R.id.parent_left);
        parent_right = (LinearLayout)findViewById(R.id.parent_right);
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_title_down = (ImageView)findViewById(R.id.iv_title_down);
        iv_title_bg = (ImageView)findViewById(R.id.iv_title_bg);
        iv_right = (ImageView)findViewById(R.id.iv_right);
    }

    private void init(AttributeSet attrs) {
        setClipToPadding(true);
        setFitsSystemWindows(true);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_title_bar, this);
        initView();
        tv_title.setVisibility(GONE);
        toggle_view.setVisibility(GONE);
        setBackgroundResource(R.color.blue);
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TitleTopBar);
            String title = array.getString(R.styleable.TitleTopBar_tb_title);
            setTitle(title);
            boolean backable = array.getBoolean(R.styleable.TitleTopBar_tb_backable, true);
            setBackable(backable);
            boolean clickable = array.getBoolean(R.styleable.TitleTopBar_tb_clickable, false);
            setTitleClickable(clickable);
            String leftText = array.getString(R.styleable.TitleTopBar_tb_leftText);
            setLeftText(leftText);
            String rightText = array.getString(R.styleable.TitleTopBar_tb_rightText);
            setRightText(rightText);
            int rightImg = array.getResourceId(R.styleable.TitleTopBar_tb_rightImg, 0);
            setRightImage(rightImg);
            int leftImg = array.getResourceId(R.styleable.TitleTopBar_tb_leftImg, 0);
            setLeftImage(leftImg);
            CharSequence charSequence[] = array.getTextArray(R.styleable.TitleTopBar_tb_titleArray);
            setTextArray(charSequence);
            array.recycle();
        }
        attachClick();
    }

    public void setTextArray(CharSequence charSequence[]) {
        if (charSequence != null && charSequence.length >= 2) {
            tv_title.setVisibility(GONE);
            toggle_view.setVisibility(VISIBLE);
            toggle_view.setTextArray(charSequence);
        }
    }

    private void attachClick() {
        parent_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAction(0);
            }
        });
        tv_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAction(1);
            }
        });
        iv_title_down.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAction(1);
            }
        });
        parent_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAction(2);
            }
        });
    }


    private void clickAction(int index) {
        if (index == 0) {
            if (mOnTopBarClickCallback != null && (iv_back.getVisibility() == VISIBLE || tv_back_text.getVisibility() == VISIBLE)) {
                mOnTopBarClickCallback.onLeftClick();
            }
        } else if (index == 1) {
            if (mOnTopBarClickCallback != null) {
                mOnTopBarClickCallback.onTitileClick();
            }
        } else if (index == 2) {
            if (mOnTopBarClickCallback != null && (iv_right.getVisibility() == VISIBLE || tv_right.getVisibility() == VISIBLE)) {
                mOnTopBarClickCallback.onRightClick();
            }
        }
    }

    public void setLayoutRootBackgroundColor(int color){
        layout_root.setBackgroundColor(color);
        setBackgroundColor(color);
    }

    public void setTitle(String title) {
        if (title != null) {
            tv_title.setVisibility(VISIBLE);
            toggle_view.setVisibility(GONE);
            tv_title.setText(title);
        }
    }

    public void setTitleBackground(int resId){
        if (resId!= 0){
            iv_title_bg.setVisibility(VISIBLE);
            tv_title.setVisibility(GONE);
            iv_title_bg.setImageResource(resId);
            iv_title_bg.setAdjustViewBounds(true);
        }else{
            iv_title_bg.setVisibility(GONE);
        }
    }

    public void setTitlBold(boolean isBlod){
        tv_title.setTypeface(isBlod ?Typeface.DEFAULT_BOLD:Typeface.DEFAULT);
    }

    public void setTitle(int title) {
        if (title != 0) {
            tv_title.setText(title);
            tv_title.setVisibility(VISIBLE);
            toggle_view.setVisibility(GONE);
        } else {
            tv_title.setText("");
        }
    }

    public void setBackable(boolean backable) {
        if (backable) {
            iv_back.setImageResource(R.mipmap.ic_left);
            iv_back.setVisibility(VISIBLE);
        } else {
            iv_back.setVisibility(GONE);
        }
    }
    public void setTitleClickable(boolean clickable) {
        if (clickable) {
            iv_title_down.setVisibility(VISIBLE);
        } else {
            iv_title_down.setVisibility(GONE);
        }
    }

    public void setLeftText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_back_text.setText(text);
            tv_back_text.setVisibility(VISIBLE);
        } else {
            tv_back_text.setVisibility(GONE);
        }
    }

    public void setLeftText(int textResId) {
        if (textResId != 0) {
            tv_back_text.setText(textResId);
            tv_back_text.setVisibility(VISIBLE);
        } else {
            tv_back_text.setVisibility(GONE);
        }
    }

    public void setRightText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_right.setText(text);
            tv_right.setVisibility(VISIBLE);
        } else {
            tv_right.setVisibility(GONE);
        }
    }

    public void setRightText(int textResId) {
        if (textResId != 0) {
            tv_right.setText(textResId);
            tv_right.setVisibility(VISIBLE);
        } else {
            tv_right.setVisibility(GONE);
        }
    }

    public void setRightImage(int res) {
        if (res != 0) {
            iv_right.setImageResource(res);
            iv_right.setVisibility(VISIBLE);
        } else {
            iv_right.setVisibility(GONE);
        }
    }

    public void setLeftImage(int res) {
        if (res != 0) {
            iv_back.setImageResource(res);
            iv_back.setVisibility(VISIBLE);
        }
    }

    public ImageView getRightImageView(){
        return iv_right;
    }

    private OnTopBarClickCallback mOnTopBarClickCallback;

    public void setOnTopBarClickCallback(OnTopBarClickCallback callback) {
        this.mOnTopBarClickCallback = callback;
    }

    public interface OnTopBarClickCallback {
        void onLeftClick();

        void onTitileClick();

        void onRightClick();
    }

    public void setTopBarOption(TopBarOption option) {
        setLeftText(option.leftText);
        if (option.useToggleView) {
            setTextArray(option.toggleChar);
        } else {
            setTitle(option.title);
        }
        setRightText(option.rightText);
        setRightImage(option.rightImgRes);
        setBackable(option.backable);
        setOnTopBarClickCallback(option.callback);
        if(option.titleBgColor!=-1){
            setLayoutRootBackgroundColor(option.titleBgColor);
        }
    }


    public static class TopBarOption {
        public String title;
        public String leftText;
        public String rightText;
        public int rightImgRes;
        public boolean backable = true;
        public OnTopBarClickCallback callback;
        public boolean useToggleView = false;
        public String toggleChar[];
        public int titleBgColor=-1;
        public int pageBgResId=-1;

        public TopBarOption(String title, String leftText) {
            this(title, leftText, null);
        }

        public TopBarOption(String title, String leftText, String rightText) {
            this.title = title;
            this.leftText = leftText;
            this.rightText = rightText;
        }
        public TopBarOption(String title, String leftText, int rightImgRes) {
            this.title = title;
            this.leftText = leftText;
            this.rightImgRes = rightImgRes;
        }

        public TopBarOption(String title, String leftText, String rightText, boolean backable, OnTopBarClickCallback callback) {
            this.title = title;
            this.leftText = leftText;
            this.rightText = rightText;
            this.backable = backable;
            this.callback = callback;
        }

        public TopBarOption(String toggleChar[], String leftText, String rightText, boolean backable, OnTopBarClickCallback callback) {
            this.useToggleView = true;
            this.toggleChar = toggleChar;
            this.leftText = leftText;
            this.rightText = rightText;
            this.backable = backable;
            this.callback = callback;
        }
    }

    public ToggleView getToggleView() {
        return toggle_view;
    }
}
