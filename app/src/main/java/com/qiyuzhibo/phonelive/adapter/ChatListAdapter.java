package com.qiyuzhibo.phonelive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.bean.ChatBean;
import com.qiyuzhibo.phonelive.widget.BlackTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播间聊天
 */
public class ChatListAdapter extends BaseAdapter {
    private List<ChatBean> mChats = new ArrayList<>();

    private Context context ;


    public ChatListAdapter(Context mContext) {
        context = mContext;
    }

    public void setChats(List<ChatBean> chats){
        this.mChats = chats;
    }
    @Override
    public int getCount() {
        return mChats.size();
    }

    @Override
    public Object getItem(int position) {
        return mChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(AppContext.getInstance(),R.layout.item_live_chat,null);
            viewHolder = new ViewHolder();
            viewHolder.mChat1 = (BlackTextView) convertView.findViewById(R.id.tv_chat_1);
            viewHolder.mChat2 = (BlackTextView) convertView.findViewById(R.id.tv_chat_2);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChatBean c = mChats.get(position);

        viewHolder.mChat1.setText(c.getUserNick());
        viewHolder.mChat2.setText(c.getSendChatMsg());
        viewHolder.mChat2.setTextColor(context.getResources().getColor(R.color.white));
        return convertView;
    }
    protected class ViewHolder{
        BlackTextView mChat1,mChat2;
    }

}
