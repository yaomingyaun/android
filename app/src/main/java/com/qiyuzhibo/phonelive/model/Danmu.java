package com.qiyuzhibo.phonelive.model;

import android.graphics.Bitmap;

/**
 * Created by feiyang on 16/3/2.
 */
public class Danmu {
    public long   id;
    public String    userId;
    public String type;
    public String content;
    public Object obj;
    public Bitmap avatarBitmap;



    public Danmu(long id, String userId, String type, Bitmap avatarImg, String content, Object obj) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.avatarBitmap=avatarImg;
        this.content = content;
        this.obj = obj;
    }

    public Danmu() {
    }
}
