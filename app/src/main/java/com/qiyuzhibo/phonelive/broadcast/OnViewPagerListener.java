package com.qiyuzhibo.phonelive.broadcast;

import android.view.View;

public interface  OnViewPagerListener {
//    /**
//     * 初始化完成
//     */
//    void onInitComplete();
//
//    /**
//     * 释放的监听
//     * @param isNext
//     * @param position
//     */
//    void onPageRelease(boolean isNext, int position);
//
//    /**
//     * 选中的监听以及判断是否滑动到底部
//     * @param position
//     * @param isBottom
//     */
//    void onPageSelected(int position, boolean isBottom);.
//停止播放的监听
void onPageRelease(boolean isNest, View position);

    //播放的监听
    void onPageSelected(boolean isButten,View position);
}
