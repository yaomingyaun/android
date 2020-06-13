package com.qiyuzhibo.phonelive.broadcast;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class UnSlipViewPager extends ViewPager {
    private boolean isCanSlip = false;

    public boolean isCanSlip() {
        return isCanSlip;
    }

    public void setIsCanSlip(boolean isCanSlip) {
        this.isCanSlip = isCanSlip;
    }

    public UnSlipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnSlipViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanSlip)
            return super.onInterceptTouchEvent(arg0);
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanSlip)
            return super.onTouchEvent(arg0);
        else
            return false;
    }

}