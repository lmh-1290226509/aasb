package com.ddadai.baseviewlibrary.wheel.widget.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.ddadai.baseviewlibrary.wheel.widget.adapters.TimeWheelAdapter;
import com.ddadai.baseviewlibrary.wheel.widget.view.OnWheelChangedListener;
import com.ddadai.baseviewlibrary.wheel.widget.view.OnWheelScrollListener;
import com.ddadai.baseviewlibrary.wheel.widget.view.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeNewWheelBottomSheetDialog extends BaseWheelBottomSheetDialog {

    /**
     * 显示时间和日期-年月日时分秒
     **/
    public static final int TYPE_ALL = 1;
    /**
     * 只显示日期-年月日
     **/
    public static final int TYPE_ONLY_DATE = 2;
    /**
     * 只显示时间-时分秒
     **/
    public static final int TYPE_ONLY_TIME = 3;
    /**
     * 只显示年月
     **/
    public static final int TYPE_ONLY_YEAR_MONTH = 4;
    /**
     * 只显示年月日-时分
     **/
    public static final int TYPE_DATE_HOUR_MIN = 5;

    protected int timeType;

    protected OnYearMonthChangeListener mOnYearMonthChangeListener;

    protected List<String> years, months, days, hours, mins, secs;

    protected int minYear = -1;//最小的年份
    protected int minMonth = -1;//最小的月份
    protected int minDay = -1;//最小的日期
    protected int minHour = -1;//最小的日期
    protected int minMin = -1;//最小的日期
    protected int minSec = -1;//最小的日期
    protected int maxYear = -1;//最大的年份
    protected int maxMonth = -1;//最大的年份
    protected int maxDay = -1;//最大的年份
    protected int maxHour = -1;//最大的年份
    protected int maxMin = -1;//最大的年份
    protected int maxSec = -1;//最大的年份


    protected boolean is24Hour;//24小时制还是12小时制

    protected Calendar mCalendar;

    protected OnTimeWheelViewClickListener mOnTimeWheelViewClickListener;

    protected TimeNewWheelBottomSheetDialog(Activity context, int timeType, int minYear, int maxYear, boolean is24Hour) {
        super(context);
        mCalendar = Calendar.getInstance();
        this.timeType = timeType;
        this.is24Hour = is24Hour;


        initWheelView();
        setShowCengter(true);
        setCyclic(new boolean[]{false, false, false, false, false, false});


        setYear(minYear, maxYear);

        //设置时间
        initYears();
        initMonths();
        initDays(mCalendar.get(Calendar.YEAR) + "", mCalendar.get(Calendar.MONTH) + 1 + "");
        initTime();
        //显示当前时间
        getCurrentTime();

    }


    public TimeNewWheelBottomSheetDialog(Activity context, int timeType, int minYear, int maxYear) {
        this(context, timeType, minYear, maxYear, true);
    }


    public TimeNewWheelBottomSheetDialog(Activity context, int timeType) {
        this(context, timeType, -1, -1);
    }


    public void getCurrentTime() {
        if (minYear < 0) {
            wv1.setCurrentItem(mCalendar.get(Calendar.YEAR));
        } else {
            wv1.setCurrentItem(mCalendar.get(Calendar.YEAR) - minYear);
        }
        if (minMonth < 0) {
            wv2.setCurrentItem(mCalendar.get(Calendar.MONTH));
        } else {
            wv2.setCurrentItem(mCalendar.get(Calendar.MONTH) + 1 - minMonth);
        }
        if (minDay < 0) {
            wv3.setCurrentItem(mCalendar.get(Calendar.DAY_OF_MONTH) - 1);
        } else {
            wv3.setCurrentItem(mCalendar.get(Calendar.DAY_OF_MONTH) - minDay);
        }
        if (minHour < 0) {
            wv4.setCurrentItem(mCalendar.get(Calendar.HOUR_OF_DAY));
        } else {
            wv4.setCurrentItem(mCalendar.get(Calendar.HOUR_OF_DAY) - minHour);
        }
        if (minMin < 0) {
            wv5.setCurrentItem(mCalendar.get(Calendar.MINUTE));
        } else {
            wv5.setCurrentItem(mCalendar.get(Calendar.MINUTE) - minMin);
        }
        if (minSec < 0) {
            wv6.setCurrentItem(mCalendar.get(Calendar.SECOND));
        } else {
            wv6.setCurrentItem(mCalendar.get(Calendar.SECOND) - minSec);
        }
    }

    public void setMinTimeForCurrentTime() {
        setMinTime(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1, mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), mCalendar.get(Calendar.SECOND));
    }


    //设置小于0的 则是不设置最小的
    public void setMinTime(int year, int month, int day, int hour, int min, int sec) {
        this.minYear = year;
        this.minMonth = month;
        this.minDay = day;
        this.minHour = hour;
        this.minMin = min;
        this.minSec = sec;
        initDateTime();
    }


    //设置小于0的 则是不设置最小的
    public void setMaxTime(int year, int month, int day, int hour, int min, int sec) {
        this.maxYear = year;
        this.maxMonth = month;
        this.maxDay = day;
        this.maxHour = hour;
        this.maxMin = min;
        this.maxSec = sec;
        initDateTime();
    }

    public void initDateTime() {
        //设置时间
        initYears();
        initMonths();
        initDays(mCalendar.get(Calendar.YEAR) + "", mCalendar.get(Calendar.MONTH) + 1 + "");
        initTime();
        //显示当前时间
        getCurrentTime();
    }


    public void setYear(int minYear, int maxYear) {
        if (minYear < 0) {
            this.minYear = mCalendar.get(Calendar.YEAR);
        } else {
            this.minYear = minYear;
        }
        if (maxYear < 0) {
            this.maxYear = mCalendar.get(Calendar.YEAR);
        } else {
            this.maxYear = maxYear;
        }
        if (this.minYear > this.maxYear) {
            int temp = this.minYear;
            this.minYear = this.maxYear;
            this.maxYear = temp;
        }

        int currentYear = mCalendar.get(Calendar.YEAR);
        if (currentYear < this.minYear || currentYear > this.maxYear) {
            mCalendar.set(Calendar.YEAR, this.minYear);
        }

    }

    protected void initWheelView() {
        switch (timeType) {
            case TYPE_ALL:
                wv1.setVisibility(View.VISIBLE);
                wv2.setVisibility(View.VISIBLE);
                wv3.setVisibility(View.VISIBLE);
                wv4.setVisibility(View.VISIBLE);
                wv5.setVisibility(View.VISIBLE);
                wv6.setVisibility(View.VISIBLE);
                mOnYearMonthChangeListener = new MyOnYearMonthChangeListener();
                break;
            case TYPE_ONLY_DATE:
                wv1.setVisibility(View.VISIBLE);
                wv2.setVisibility(View.VISIBLE);
                wv3.setVisibility(View.VISIBLE);
                mOnYearMonthChangeListener = new MyOnYearMonthChangeListener();
                break;
            case TYPE_ONLY_TIME:
                wv4.setVisibility(View.VISIBLE);
                wv5.setVisibility(View.VISIBLE);
                wv6.setVisibility(View.VISIBLE);
                break;
            case TYPE_ONLY_YEAR_MONTH:
                wv1.setVisibility(View.VISIBLE);
                wv2.setVisibility(View.VISIBLE);
                break;
            case TYPE_DATE_HOUR_MIN:
                wv1.setVisibility(View.VISIBLE);
                wv2.setVisibility(View.VISIBLE);
                wv3.setVisibility(View.VISIBLE);
                wv4.setVisibility(View.VISIBLE);
                wv5.setVisibility(View.VISIBLE);
                mOnYearMonthChangeListener = new MyOnYearMonthChangeListener();
                break;
        }


        for (int i = 0; i < wvs.length; i++) {
            setWv(i, wvs[i]);
        }
    }


    protected void setWv(final int index, final WheelView wv) {
        if (wv.getVisibility() != View.VISIBLE) {
            return;
        }
        wv.setVisibleItems(5);
        wv.setViewAdapter(new TimeWheelAdapter(mContext, index + 1));
        wv.setCurrentItem(0);
        wv.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                ((TimeWheelAdapter) wheel.getViewAdapter()).setIndex(wheel.getCurrentItem());
                if (mOnYearMonthChangeListener != null) {
                    mOnYearMonthChangeListener.change(wheel, oldValue, newValue);
                }
            }
        });
        wv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                ((TimeWheelAdapter) wheel.getViewAdapter()).setIndex(wheel.getCurrentItem());
                if (mOnYearMonthChangeListener != null) {
                    mOnYearMonthChangeListener.change(wheel, -1, -1);
                }
            }
        });
    }


    @Override
    protected void okClick() {
        super.okClick();

        if (mOnTimeWheelViewClickListener != null) {
            int year = 0, month = 0, day = 0, hour = 0, min = 0, sec = 0;
            try {

                if (years.size() > wv1.getCurrentItem()) {
                    year = Integer.parseInt(years.get(wv1.getCurrentItem()));
                }
                if (months.size() > wv2.getCurrentItem()) {
                    month = Integer.parseInt(months.get(wv2.getCurrentItem()));
                }
                if (days.size() > wv3.getCurrentItem()) {
                    day = Integer.parseInt(days.get(wv3.getCurrentItem()));
                }
                if (hours.size() > wv4.getCurrentItem()) {
                    if (is24Hour) {
                        hour = Integer.parseInt(hours.get(wv4.getCurrentItem()));
                    } else {
                        String str = hours.get(wv4.getCurrentItem());
                        if (str.contains("上午")) {
                            hour = Integer.parseInt(str.split("上午")[1]);
                        } else {
                            hour = Integer.parseInt(str.split("下午")[1]);
                            if (hour == 12) {
                                hour = 0;
                            }
                        }
                    }
                }
                if (mins.size() > wv5.getCurrentItem()) {
                    min = Integer.parseInt(mins.get(wv5.getCurrentItem()));
                }
                if (secs.size() > wv6.getCurrentItem()) {
                    sec = Integer.parseInt(secs.get(wv6.getCurrentItem()));
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mOnTimeWheelViewClickListener.getTime(year, month, day, hour, min, sec);
            }
        }
    }

    protected void initYears() {
        switch (timeType) {
            case TYPE_ALL:
                break;
            case TYPE_ONLY_DATE:
                break;
            case TYPE_ONLY_TIME:
                return;
            case TYPE_ONLY_YEAR_MONTH:
                break;
            case TYPE_DATE_HOUR_MIN:
                break;
        }
        if (years == null) {
            years = new ArrayList<>();
        } else {
            years.clear();
        }
        for (int i = minYear; i <= maxYear; i++) {
            years.add(i + "");
        }

        ((TimeWheelAdapter) wv1.getViewAdapter()).setModels(years);
    }

    protected void initMonths() {
        switch (timeType) {
            case TYPE_ALL:
                break;
            case TYPE_ONLY_DATE:
                break;
            case TYPE_ONLY_TIME:
                return;
            case TYPE_ONLY_YEAR_MONTH:
                break;
            case TYPE_DATE_HOUR_MIN:
                break;
        }
        if (months == null) {
            months = new ArrayList<>();
        } else {
            months.clear();
        }
        int currentYear = Integer.parseInt(years.get(wv1.getCurrentItem()));
        int tempMinMonth = 1;
        int tempMaxMonth = 12;
        if (currentYear == minYear && minMonth >= 0) {
            tempMinMonth = minMonth;
        }
        if (currentYear == maxYear && maxMonth >= 0) {
            tempMaxMonth = maxMonth;
        }
        for (int i = tempMinMonth; i <= tempMaxMonth; i++) {
            months.add(i + "");
        }
        ((TimeWheelAdapter) wv2.getViewAdapter()).setModels(months);
    }


    protected void initDays(String year, String month) {
        switch (timeType) {
            case TYPE_ALL:
                break;
            case TYPE_ONLY_DATE:
                break;
            case TYPE_ONLY_TIME:
                return;
            case TYPE_ONLY_YEAR_MONTH:
                return;
            case TYPE_DATE_HOUR_MIN:
                break;
        }
        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month)) {
            return;
        }
        int iYear = 0;
        int iMonth = 0;
        try {
            iYear = Integer.parseInt(year);
            iMonth = Integer.parseInt(month);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int tempMinDay = 1;
        int tempMaxDay = 31;

        if (iYear == minYear && iMonth == minMonth && minDay >= 0) {
            tempMinDay = minDay;
        }
        if (iYear == maxYear && iMonth == maxMonth && maxDay >= 0) {
            tempMaxDay = maxDay;
        }

        switch (iMonth) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (tempMaxDay > 30) {
                    tempMaxDay = 30;
                }
                break;
            case 2:
                if ((iYear % 4 == 0 && iYear % 100 != 0) && iYear % 400 == 0) {
                    if (tempMaxDay > 29) {
                        tempMaxDay = 29;
                    }
                } else {
                    if (tempMaxDay > 29) {
                        tempMaxDay = 28;
                    }
                }
                break;
        }

        if (days == null) {
            days = new ArrayList<>();
        } else {
            days.clear();
        }
        for (int i = tempMinDay; i <= tempMaxDay; i++) {
            days.add(i + "");
        }

        ((TimeWheelAdapter) wv3.getViewAdapter()).setModels(days);
    }


    protected void initHour() {
        int currentYear;
        if(wv1.getCurrentItem()<years.size()){
            currentYear = Integer.parseInt(years.get(wv1.getCurrentItem()));
        }else{
            currentYear = Integer.parseInt(years.get(wv1.getCurrentItem()));
        }
        int currentMonth = Integer.parseInt(months.get(wv2.getCurrentItem()));
        int currentDay = Integer.parseInt(days.get(wv3.getCurrentItem()));

        int tempMinHour=0;
        int tempMaxHour=23;
        if(currentYear==minYear&&currentMonth==minMonth&&currentDay==minDay){
            tempMinHour=minMin;
        }

        if(currentYear==maxYear&&currentMonth==maxMonth&&currentDay==maxDay){
            tempMaxHour=maxMin;
        }

        if (hours == null) {
            hours = new ArrayList<>();
        } else {
            hours.clear();
        }

        if(is24Hour){
            for (int i = tempMinHour; i <= tempMaxHour ; i++) {
                if(i<10){
                    hours.add("0"+i);
                }else{
                    hours.add(""+i);
                }
            }
        }else{
            for (int i = tempMinHour; i <= tempMaxHour ; i++) {
                if (i <= 12) {
                    hours.add("上午" + i);
                } else {
                    hours.add("下午" + (i % 12));
                }
            }
        }
        ((TimeWheelAdapter) wv4.getViewAdapter()).setModels(hours);
    }

    protected void initMin() {
        int currentYear = Integer.parseInt(years.get(wv1.getCurrentItem()));
        int currentMonth = Integer.parseInt(months.get(wv2.getCurrentItem()));
        int currentDay = Integer.parseInt(days.get(wv3.getCurrentItem()));
        int currentHour = Integer.parseInt(hours.get(wv4.getCurrentItem()));

        int tempMinMin=0;
        int tempMaxMin=59;
        if(currentYear==minYear&&currentMonth==minMonth&&currentDay==minDay&&currentHour==minHour){
            tempMinMin=minMin;
        }

        if(currentYear==maxYear&&currentMonth==maxMonth&&currentDay==maxDay&&currentHour==maxHour){
            tempMaxMin=maxMin;
        }

        if (mins == null) {
            mins = new ArrayList<>();
        } else {
            mins.clear();
        }

        for (int i = tempMinMin; i <= tempMaxMin ; i++) {
            if(i<10){
                mins.add("0"+i);
            }else{
                mins.add(""+i);
            }
        }

        ((TimeWheelAdapter) wv5.getViewAdapter()).setModels(mins);
    }

    protected void initSec() {
        int currentYear = Integer.parseInt(years.get(wv1.getCurrentItem()));
        int currentMonth = Integer.parseInt(months.get(wv2.getCurrentItem()));
        int currentDay = Integer.parseInt(days.get(wv3.getCurrentItem()));
        int currentHour = Integer.parseInt(hours.get(wv4.getCurrentItem()));
        int currentMin = Integer.parseInt(mins.get(wv5.getCurrentItem()));

        int tempMinSec=0;
        int tempMaxSec=59;
        if(currentYear==minYear&&currentMonth==minMonth&&currentDay==minDay&&currentHour==minHour&&currentMin==minMin){
            tempMinSec=minSec;
        }

        if(currentYear==maxYear&&currentMonth==maxMonth&&currentDay==maxDay&&currentHour==maxHour&&currentMin==maxMin){
            tempMaxSec=maxSec;
        }

        if (secs == null) {
            secs = new ArrayList<>();
        } else {
            secs.clear();
        }


        for (int i = tempMinSec; i <= tempMaxSec ; i++) {
            if(i<10){
                secs.add("0"+i);
            }else{
                secs.add(""+i);
            }
        }

        ((TimeWheelAdapter) wv6.getViewAdapter()).setModels(secs);
    }

    protected void initTime(){
        switch (timeType) {
            case TYPE_ALL:
                break;
            case TYPE_ONLY_DATE:
                return;
            case TYPE_ONLY_TIME:
                break;
            case TYPE_ONLY_YEAR_MONTH:
                return;
            case TYPE_DATE_HOUR_MIN:
                break;
        }
        hours=new ArrayList<>();
        mins=new ArrayList<>();
        secs=new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            String s="";
            if(i<10){
                s="0"+i;
            }else{
                s=i+"";
            }
            if(i<24&&is24Hour){
                hours.add(s);
            }
            if(i%5==0){
                mins.add(s);
            }
            secs.add(s);
        }
        if(!is24Hour){
            hours.add("下午12");
            for (int i = 1; i <= 23; i++) {
                if(i<=12){
                    hours.add("上午"+i);
                }else{
                    hours.add("下午"+(i%12));
                }
            }
        }
        ((TimeWheelAdapter)wv4.getViewAdapter()).setModels(hours);
        ((TimeWheelAdapter)wv5.getViewAdapter()).setModels(mins);

        if(timeType!=TYPE_DATE_HOUR_MIN){
            ((TimeWheelAdapter)wv6.getViewAdapter()).setModels(secs);
        }
    }

//    protected void initTime() {
//        switch (timeType) {
//            case TYPE_ALL:
//                break;
//            case TYPE_ONLY_DATE:
//                return;
//            case TYPE_ONLY_TIME:
//                break;
//            case TYPE_ONLY_YEAR_MONTH:
//                return;
//            case TYPE_DATE_HOUR_MIN:
//                break;
//        }
//
//        initHour();
//        initMin();
//        if (timeType != TYPE_DATE_HOUR_MIN) {
//            initSec();
//        }
//
////        for (int i = 0; i < 60; i++) {
////            String s = "";
////            if (i < 10) {
////                s = "0" + i;
////            } else {
////                s = i + "";
////            }
////            if (i < 24 && is24Hour) {
////                hours.add(s);
////            }
////            mins.add(s);
////            secs.add(s);
////        }
////        if (!is24Hour) {
////            hours.add("下午12");
////            for (int i = 1; i <= 23; i++) {
////                if (i <= 12) {
////                    hours.add("上午" + i);
////                } else {
////                    hours.add("下午" + (i % 12));
////                }
////            }
////        }
////        ((TimeWheelAdapter) wv4.getViewAdapter()).setModels(hours);
////        ((TimeWheelAdapter) wv5.getViewAdapter()).setModels(mins);
////
////        if (timeType != TYPE_DATE_HOUR_MIN) {
////            initSec();
////        }
//    }


    protected void setOnYearMonthChangeListener(OnYearMonthChangeListener l) {
        this.mOnYearMonthChangeListener = l;
    }

    protected interface OnYearMonthChangeListener {
        void change(WheelView wheel, int oldValue, int newValue);
    }


    /***
     * 默认的年月变化监听
     * @author user
     *
     */
    protected class MyOnYearMonthChangeListener implements OnYearMonthChangeListener {

        @Override
        public void change(WheelView wheel, int oldValue, int newValue) {
            if (wheel == wv1) {
                initMonths();
                if (wv2.getCurrentItem() >= months.size()) {
                    wv2.setCurrentItem(0, true);
                } else {
                    initDays(minYear + wv1.getCurrentItem() + "", months.get(wv2.getCurrentItem()));
                    initTime();
                }
            } else if (wheel == wv2) {
                initDays(minYear + wv1.getCurrentItem() + "", months.get(wv2.getCurrentItem()));
                if (wv3.getCurrentItem() >= days.size()) {
                    wv3.setCurrentItem(0, true);
                } else {
                    initTime();
                }
            } else if (wheel == wv3) {
                initTime();
            } else if (wheel == wv4) {
//                initTime();
            } else if (wheel == wv5) {
//                initTime();
            } else if (wheel == wv6) {
//                initTime();
            }
        }

    }


    public void setOnTimeWheelViewClickListener(OnTimeWheelViewClickListener l) {
        mOnTimeWheelViewClickListener = l;
    }

    public interface OnTimeWheelViewClickListener {
        void getTime(int year, int month, int day, int hour, int min, int sec);
    }
}
