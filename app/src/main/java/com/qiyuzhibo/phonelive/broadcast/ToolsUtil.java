package com.qiyuzhibo.phonelive.broadcast;

import android.content.Context;

//秒转换为时分秒
public class ToolsUtil {
    private static ToolsUtil toolsUtil;
    private Context mContext;

    private ToolsUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    public static ToolsUtil getInstance(Context context) {
        if (toolsUtil == null) {
            toolsUtil = new ToolsUtil(context);
        }
        return toolsUtil;
    }
    public String timeConversion(int time) {
        int hour = 0;
        int minutes = 0;
        int sencond = 0;
        int temp = time % 3600;
        if (time > 3600) {
            hour = time / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    minutes = temp / 60;
                    if (temp % 60 != 0) {
                        sencond = temp % 60;
                    }
                } else {
                    sencond = temp;
                }
            }
        } else {
            minutes = time / 60;
            if (time % 60 != 0) {
                sencond = time % 60;
            }
        }
        return (hour<10?("0"+hour):hour) + ":" + (minutes<10?("0"+minutes):minutes) + ":" + (sencond<10?("0"+sencond):sencond);
    }

}
