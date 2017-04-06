package com.yxy.lib.base.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.yxy.lib.base.R;
import com.yxy.lib.base.widget.TitleTopBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Administrator on 2016/9/12.
 */
public class CalendarDialog extends BaseDialog implements CompactCalendarView.CompactCalendarViewListener, TitleTopBar.OnTopBarClickCallback {

    private CompactCalendarView compactcalendarView;
    private TitleTopBar title_bar;
    private View parent_view;
    private int year = -1;
    private int month = -1;
    private int day = -1;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

    public CalendarDialog(Context context) {
        super(context);
    }

    @Override
    protected View onInflateView(LayoutInflater inflater) {
        parent_view = inflater.inflate(R.layout.dialog_calendar, null);
        title_bar = (TitleTopBar) parent_view.findViewById(R.id.title_bar);
        compactcalendarView = (CompactCalendarView) parent_view.findViewById(R.id.compactcalendar_view);
        compactcalendarView.setLocale(TimeZone.getDefault(), Locale.CHINESE);
        compactcalendarView.setUseThreeLetterAbbreviation(true);
        compactcalendarView.setShouldShowMondayAsFirstDay(false);
        compactcalendarView.setListener(this);
        title_bar.setOnTopBarClickCallback(this);
        parent_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        buildTitle();
        return parent_view;
    }

    private void buildTitle() {
        Calendar calendar = Calendar.getInstance();
        if (year != -1 && month != -1 && day != -1) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DATE, day);
            compactcalendarView.setCurrentDate(calendar.getTime());
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
        }
        String text = format.format(new Date(calendar.getTimeInMillis()));
        title_bar.setTitle(text);

        getCurrMonthEvents(year, month);
    }

    @Override
    public void show() {
        super.show();
        setFullContent();
    }

    @Override
    public void onDayClick(Date dateClicked) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateClicked);
        if (callback != null) {
            callback.onDaySelect(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DATE));
        }
        dismiss();
    }

    private void getCurrMonthEvents(int year, int month) {
        compactcalendarView.removeAllEvents();
        List<Event> eventList = new ArrayList<>();
        compactcalendarView.addEvents(eventList);

        if (mOnReadyLoadDataCallback != null) {
            mOnReadyLoadDataCallback.loadMonthEvents(year, month);
        }
    }


    public void clearEvent(){
        compactcalendarView.removeAllEvents();
    }

    public void updateDataYellowPoint(int year, int month, List<Integer> haveEventDays) {
        updateData(year,month,haveEventDays,Color.YELLOW);
    }

    private void updateData(int year, int month, List<Integer> haveEventDays,int color) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        Date date = calendar.getTime();

        List<Event> eventList = new ArrayList<>();
        if (haveEventDays != null && haveEventDays.size() > 0) {
            for (Integer integer : haveEventDays) {
                eventList.add(addEvent(date, integer,color));
            }
        }
        compactcalendarView.addEvents(eventList);
    }

    public void updateDataGreenPoint(int year, int month, List<Integer> haveEventDays) {
        updateData(year,month,haveEventDays,Color.GREEN);
    }

    @Override
    public void onMonthScroll(Date date) {
        String text = format.format(date);
        title_bar.setTitle(text);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        getCurrMonthEvents(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
    }

    private Event addEvent(Date date, int day, int color) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, day);
        return new Event(color, calendar.getTimeInMillis());
    }

    @Override
    public void onLeftClick() {
        compactcalendarView.showPreviousMonth();
    }

    @Override
    public void onTitileClick() {

    }

    @Override
    public void onRightClick() {
        compactcalendarView.showNextMonth();
    }

    private OnCalendarDialogCallback callback;

    public void setOnCalendarDialogCallback(OnCalendarDialogCallback callback) {
        this.callback = callback;
    }

    public interface OnCalendarDialogCallback {
        void onDaySelect(int year, int month, int day);

    }

    private OnReadyLoadDataCallback mOnReadyLoadDataCallback;

    public void setOnReadyLoadDataCallback(OnReadyLoadDataCallback callback) {
        this.mOnReadyLoadDataCallback = callback;
    }

    public interface OnReadyLoadDataCallback {

        void loadMonthEvents(int year, int month);
    }

    public void setYearAndMonth(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        if (compactcalendarView != null) {
            buildTitle();
        }
    }
}
