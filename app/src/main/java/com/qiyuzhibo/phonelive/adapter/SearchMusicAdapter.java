package com.qiyuzhibo.phonelive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.qiyuzhibo.phonelive.AppConfig;
import com.qiyuzhibo.phonelive.bean.MusicBean;
import com.qiyuzhibo.phonelive.fragment.SearchMusicDialogFragment;
import com.qiyuzhibo.phonelive.utils.DBManager;
import com.dd.CircularProgressButton;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.ui.StartLiveActivity;
import com.qiyuzhibo.phonelive.utils.TLog;
import com.qiyuzhibo.phonelive.widget.BlackTextView;

import java.io.File;
import java.util.List;

/**
 * 搜索音乐列表
 */
public class SearchMusicAdapter extends BaseAdapter {
    private List<MusicBean> mMusicList;
    private SearchMusicDialogFragment mFragment;
    private DBManager mDbManager;
    private Context context;

    public SearchMusicAdapter(List<MusicBean> MusicList, SearchMusicDialogFragment fragment, DBManager dbManager,Context mContext){
        this.mMusicList =  MusicList;
        this.mFragment = fragment;
        this.mDbManager = dbManager;
        this.context = mContext;
    }
    public void notifyDataSetChangedMusicList(List<MusicBean> MusicList){
        this.mMusicList =  MusicList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mMusicList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        convertView = View.inflate(context, R.layout.item_search_music,null);
        viewHolder.mMusicName = (BlackTextView) convertView.findViewById(R.id.item_tv_search_music_name);
        viewHolder.mMusicAuthor = (BlackTextView) convertView.findViewById(R.id.item_tv_search_music_author);
        viewHolder.mBtnDownload = (Button) convertView.findViewById(R.id.item_btn_search_music_download);

        final MusicBean music = mMusicList.get(position);
        viewHolder.mMusicName.setText(music.audio_name);
        viewHolder.mMusicAuthor.setText(music.artist_name);
        final File file = new File(AppConfig.DEFAULT_SAVE_MUSIC_PATH + music.audio_name + ".mp3");


        //判断该音乐是否存在
        if(mDbManager.queryFromEncryptedSongId(music.audio_id).getCount() != 0){
            viewHolder.mBtnDownload.setText(R.string.select);
        }else{
            viewHolder.mBtnDownload.setText(R.string.download);
        }
        //点击下载或播放
        viewHolder.mBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                //判断该音乐是否存在,存在直接播放
                if(mDbManager.queryFromEncryptedSongId(music.audio_id).getCount() != 0){
                    TLog.error("存在");
                    intent = new Intent();
                    ((StartLiveActivity)mFragment.getActivity()).onSelectMusic(intent.putExtra("filepath",file.getPath()));
                }else {
                    TLog.error("不存在");
                    mFragment.downloadMusic(music,(Button)v);
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        BlackTextView mMusicName,mMusicAuthor;
        Button mBtnDownload;
    }
}
