package com.qiyuzhibo.phonelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.bean.AVBean;
import com.qiyuzhibo.phonelive.bean.AVBeanLB;
import com.qiyuzhibo.phonelive.bean.ActiveBean;
import com.qiyuzhibo.phonelive.broadcast.CustomerVideoView;
import com.qiyuzhibo.phonelive.ui.VideoPlay2Activity;
import com.qiyuzhibo.phonelive.widget.AvatarView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by weilian on 2017/9/8.
 */

public class LieBiaoAdapter3 extends RecyclerView.Adapter<LieBiaoAdapter3.Vh> {

    private List<AVBeanLB> mUserList;
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;
    public LieBiaoAdapter3(Context context,List<AVBeanLB> userList) {
        mContext = context;
        mUserList=userList;
        mInflater = LayoutInflater.from(mContext);
    }
    public void clear() {
        mUserList.clear();
        notifyDataSetChanged();
    }
    public void insertList(List<AVBeanLB> list) {
        int p = mUserList.size();
        mUserList.addAll(list);
        notifyItemRangeInserted(p, list.size());
        notifyItemRangeChanged(p, list.size());
        mRecyclerView.scrollToPosition(p);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public void setData(List<AVBeanLB> list) {
        this.mUserList = list;
        notifyDataSetChanged();
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.lie, parent, false));
    }

    @Override
    public void onBindViewHolder(final Vh holder, final int position) {

    holder.lb_title.setText(mUserList.get(position).getTitle()+"");
    holder.item_tv_head.setAvatarUrl(mUserList.get(position).getThumb());
    holder.tags.setText(mUserList.get(position).getTags()+"");
        Glide.with(mContext).load(mUserList.get(position).getThumb()).placeholder(R.drawable.live_show_login_bg).into(holder.iv_bg);

        holder.iv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, VideoPlay2Activity.class);
                intent.putExtra("video",mUserList.get(position).getHref()+"");
                intent.putExtra("title",mUserList.get(position).getTitle()+"");
                intent.putExtra("thumb",mUserList.get(position).getThumb()+"");
                intent.putExtra("tagas",mUserList.get(position).getTags()+"");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
 public   class Vh extends RecyclerView.ViewHolder {
        TextView lb_title,tags;
     AvatarView item_tv_head;
     ImageView iv_bg;
        public Vh(View itemView) {
            super(itemView);
            lb_title =  itemView.findViewById(R.id.lb_title);
            item_tv_head=itemView.findViewById(R.id.item_tv_head);
            iv_bg=itemView.findViewById(R.id.cover);
            tags=itemView.findViewById(R.id.tags);
        }

    }
    //秒转换为时分秒
    public static String getTime(int second) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(second);
        return hms;
    }

}
