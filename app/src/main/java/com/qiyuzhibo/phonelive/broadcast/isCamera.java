package com.qiyuzhibo.phonelive.broadcast;

import android.hardware.Camera;

public class isCamera {
    public static boolean isCameraUseable() {

        boolean canUse = true;

        Camera mCamera = null;

        try {

            mCamera = Camera.open();

// setParameters 是针对魅族MX5。MX5通过Camera.open()拿到的Camera对象不为null

            Camera.Parameters mParameters = mCamera.getParameters();

            mCamera.setParameters(mParameters);

        } catch (Exception e) {

            canUse = false;


        }

        return canUse;
    }
}
