package com.yxy.lib.base.widget;

/**
 * Created by Administrator on 2015/11/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yxy.lib.base.utils.DeviceUtils;

public class IndexView extends View {
    private Paint paint = new Paint();
    private OnTouchLetterChangeListenner listenner;
    // 是否画出背景
    private boolean showBg = false;
    // 选中的项
    private int choose = -1;
    // 准备好的A~Z的字母数组
    private final String defaultAllLetters = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String noneFirstAllLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String allLetters = defaultAllLetters;
    private String[] letters = null;

    private int width;
    private int height;
    private int textSize;
    private int textColor;
    private int selectTextColor;
    private int selectBackGround;
    private Paint bgPaint;

    // 构造方法
    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        textSize = DeviceUtils.dip2px(context, 16);
        textColor = Color.LTGRAY;
        selectTextColor = Color.WHITE;
        selectBackGround = Color.argb(0x80, 0, 0, 0);
        buildLetters();
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(selectBackGround);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void buildLetters() {
        letters = new String[allLetters.length()];
        for (int i = 0; i < allLetters.length(); i++) {
            letters[i] = String.valueOf(allLetters.charAt(i));
        }
    }

    public void setNoneFirstAllLetters(){
        allLetters = noneFirstAllLetters;
        buildLetters();
        postInvalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBg) {
            // 画出背景
            canvas.drawRect(0, 0, width, height, bgPaint);
        }
        int singleHeight = height / letters.length;
        // 画字母
        for (int i = 0; i < letters.length; i++) {
            paint.setColor(textColor);
            // 设置字体格式
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(textSize);
            // 如果这一项被选中，则换一种颜色画
            if (i == choose) {
                paint.setColor(selectTextColor);
                paint.setFakeBoldText(true);
            }
            // 要画的字母的x,y坐标
            float posX = width / 2 - paint.measureText(letters[i]) / 2;
            float posY = i * singleHeight + singleHeight;
            // 画出字母
            canvas.drawText(letters[i], posX, posY, paint);
            // 重新设置画笔
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final float y = event.getY();
        // 算出点击的字母的索引
        final int index = (int) (y / getHeight() * letters.length);
        System.out.println("index  :" + index);
        // 保存上次点击的字母的索引到oldChoose
        final int oldChoose = choose;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                showBg = true;
                if (oldChoose != index && listenner != null) {
                    if (index < 0) {
                        choose = 0;
                    } else if (index >= letters.length) {
                        choose = letters.length - 1;
                    } else {
                        choose = index;
                    }
                    listenner.onTouchLetterChange(showBg, letters[choose]);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (oldChoose != index && listenner != null) {
                    if (index < 0) {
                        choose = 0;
                    } else if (index >= letters.length) {
                        choose = letters.length - 1;
                    } else {
                        choose = index;
                    }
                    listenner.onTouchLetterChange(showBg, letters[choose]);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                showBg = false;
                choose = -1;
                if (listenner != null) {
                    if (index <= 0) {
                        listenner.onTouchLetterChange(showBg, letters[0]);
                    } else if (index > 0 && index < letters.length) {
                        listenner.onTouchLetterChange(showBg, letters[index]);
                    } else if (index >= letters.length) {
                        listenner.onTouchLetterChange(showBg, letters[letters.length - 1]);
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    public void setOnTouchLetterChangeListenner(OnTouchLetterChangeListenner listenner) {
        this.listenner = listenner;
    }

    public interface OnTouchLetterChangeListenner {
        void onTouchLetterChange(boolean isTouched, String s);
    }

}