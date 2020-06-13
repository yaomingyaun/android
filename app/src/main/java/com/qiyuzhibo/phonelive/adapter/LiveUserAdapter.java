package com.qiyuzhibo.phonelive.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.bean.LiveJson;
import com.qiyuzhibo.phonelive.utils.LiveUtils;
import com.qiyuzhibo.phonelive.utils.SimpleUtils;
import com.qiyuzhibo.phonelive.widget.AvatarView;
import com.qiyuzhibo.phonelive.widget.BlackTextView;
import com.qiyuzhibo.phonelive.widget.LoadUrlImageView;
import com.qiyuzhibo.phonelive.R;

import java.util.List;

//热门主播
public class LiveUserAdapter extends BaseAdapter {
    private List<LiveJson> mUserList;
    private LayoutInflater inflater;

    public LiveUserAdapter(LayoutInflater inflater, List<LiveJson> mUserList) {
        this.mUserList = mUserList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hot_user, null);
            viewHolder = new ViewHolder();
            viewHolder.mUserNick = (BlackTextView) convertView.findViewById(R.id.tv_live_nick);
            viewHolder.mUserLocal = (BlackTextView) convertView.findViewById(R.id.tv_live_local);
            viewHolder.mUserNums = (BlackTextView) convertView.findViewById(R.id.tv_live_usernum);
            viewHolder.mUserHead = (AvatarView) convertView.findViewById(R.id.iv_live_user_head);
            viewHolder.mUserPic = (LoadUrlImageView) convertView.findViewById(R.id.iv_live_user_pic);
            viewHolder.mRoomTitle = (BlackTextView) convertView.findViewById(R.id.tv_hot_room_title);
            viewHolder.mAnchorLevel = (ImageView) convertView.findViewById(R.id.iv_live_user_anchorlevel);
            viewHolder.mIvType = (ImageView) convertView.findViewById(R.id.iv_live_hot_type);
            convertView.setTag(viewHolder);
        }
        LiveJson live = mUserList.get(position);
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.mUserNick.setText(live.user_nicename);
        viewHolder.mUserLocal.setText(live.city);
        viewHolder.mUserHead.setAvatarUrl(live.avatar_thumb);

        viewHolder.mUserNums.setText(live.nums);
        viewHolder.mAnchorLevel.setImageResource(LiveUtils.getAnchorLevelRes2(live.level_anchor));
        //用于平滑加载图片
        SimpleUtils.loadImageForView(AppContext.getInstance(), viewHolder.mUserPic, live.thumb, R.drawable.live_show_login_bg);
        if (live.type != null) {
            if (live.type.equals("0")) {
                viewHolder.mIvType.setImageResource(R.drawable.type0);
            }
            if (live.type.equals("1")) {
                viewHolder.mIvType.setImageResource(R.drawable.type1);
            }
            if (live.type.equals("2")) {
                viewHolder.mIvType.setImageResource(R.drawable.type2);
            }
            if (live.type.equals("3")) {
                viewHolder.mIvType.setImageResource(R.drawable.type3);
            }
        }
        if (!TextUtils.isEmpty(live.title)) {
            viewHolder.mRoomTitle.setVisibility(View.VISIBLE);
            viewHolder.mRoomTitle.setText(live.title);
        } else {
            viewHolder.mRoomTitle.setVisibility(View.GONE);
            viewHolder.mRoomTitle.setText("");
        }
        return convertView;
    }

    private class ViewHolder {
        public BlackTextView mUserNick, mUserLocal, mUserNums, mRoomTitle;
        public LoadUrlImageView mUserPic;
        public AvatarView mUserHead;
        public ImageView mIvType,mAnchorLevel;
    }
}


