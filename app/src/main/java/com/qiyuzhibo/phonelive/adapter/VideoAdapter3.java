package com.qiyuzhibo.phonelive.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;

import com.qiyuzhibo.phonelive.bean.AVBean;
import com.qiyuzhibo.phonelive.bean.ActiveBean;
import com.qiyuzhibo.phonelive.widget.AvatarView;




import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;

/**
 * Created by weilian on 2017/9/8.
 */

public class VideoAdapter3 extends RecyclerView.Adapter<VideoAdapter3.Vh> {

    private List<AVBean> mUserList;
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private SurfaceHolder mHolder;
    private    ActiveBean videoBean;
    private boolean isPlaying;
    private boolean isPlay = false;
    private MediaPlayer mediaPlayer;
    private int p = 0;//续播时间
    VideoView video_view;
    private SeekBar seekbar1;
    TextView data;
    private int  ducation;
    public VideoAdapter3(Context context) {
        mContext = context;
        mUserList=new ArrayList<>();
        mInflater = LayoutInflater.from(mContext);
    }
    public void clear() {
        mUserList.clear();
        notifyDataSetChanged();
    }
    public void insertList(List<AVBean> list) {
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

    public void setData(List<AVBean> list) {
        this.mUserList = list;
        notifyDataSetChanged();
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.douyin, parent, false));
    }

    @Override
    public void onBindViewHolder(final Vh holder, final int position) {
        final Uri uri = Uri.parse(mUserList.get(position).getHref());
     //   String uri="http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
        mediaPlayer=new MediaPlayer();
       video_view.setVideoURI(uri);

        //直接播放
       // video_view.start();

// 播放控制（进度拖放） 手写
//        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                // 进度条开始拖动的时候
//
//                // 首先判断是不是用户操作，不然每隔0.5秒定时器会刷新一直显示
//                if (fromUser) {
//                    SimpleDateFormat sdf;
//                    double currentTime = (progress / 100.0 )* video_view.getDuration(); // 将当前位置换算成视频总时长的指定位置时间
//                    int hour = (int) (currentTime / (3600 * 1000));
//                    if ( hour < 0 || hour == 0) {
//                        sdf = new SimpleDateFormat("mm:ss"); // 转换时间
//                    } else {
//                        sdf = new SimpleDateFormat("HH:mm:ss"); // 转换时间
//                    }
//                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00")); // 设置中国时区
//                    String result = sdf.format(currentTime) + " / " ; // 将时分秒转换成String类型
//
//
//                 //   brightnessTextView.setVisibility(View.VISIBLE);
//                   // brightnessTextView.setText( result);
//                } /*else {
//                    ((TextView)findViewById(R.id.id_progress)).setText(result);
//                }*/
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                // 控制视频跳转到目标位置
//                int progress = seekBar.getProgress();
//                int position = progress * video_view.getDuration() / 100;
//                video_view.seekTo( position );
//                ducation=position;
//               // data.setText(position+"");
//                // 隐藏掉主工具栏
//            }
//        });
        //进度条  自带
    MediaController nm=new MediaController(mContext);
    video_view.setMediaController(nm);
        //获取视频的时
                mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(String.valueOf(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.setText(getTime(mediaPlayer.getDuration()+ducation));
        //点赞
//        mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (holder.iv_video_laudgif != null && holder.iv_video_laudgif.getVisibility() == View.VISIBLE) {
//                    holder.iv_video_laudgif.setVisibility(View.GONE);
//                }
//            }
//        };
        //第二种播放
//
//        mHolder = holder.mSurfaceView.getHolder();
//        mHolder.setKeepScreenOn(true);
//        mHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(final SurfaceHolder holder1) {
//                //开始播放
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mediaPlayer.setDataSource(mContext,uri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mediaPlayer.setLooping(true);
//                mediaPlayer.setDisplay(mHolder);
//                mediaPlayer.prepareAsync();
//               // mediaPlayer.start();
//                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(final MediaPlayer mediaPlayer) {
//                        mediaPlayer.start();
//                        int msec=0;
//                        mediaPlayer.seekTo(msec);
//
//                      holder. seekbar.setMax(mediaPlayer.getDuration());
//                        new Thread()
//                        {
//                            @Override
//                            public void run() {
//
//                                try{
//                                    isPlay=true;
//                                    while (isPlay)
//                                    {
//                                        int curren=mediaPlayer.getCurrentPosition();
//                                        holder.seekbar.setProgress(curren);
//                                        sleep(500);
//                                    }
//                                }catch (Exception e)
//                                {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }.start();
//                    }
//                });
//            }
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//               // mediaPlayer.stop();
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//
//
//            }
//        });
//        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//            @Override
//            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
//                int videoWidth = mediaPlayer.getVideoWidth();
//                int videoHeight = mediaPlayer.getVideoHeight();
//                int surfaceWidth = holder.mSurfaceView.getWidth();
//                int surfaceHeight = holder.mSurfaceView.getHeight();
//                //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
//                float max;
//                if (mContext.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                    //竖屏模式下按视频宽度计算放大倍数值
//                    max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
//                } else {
//                    //横屏模式下按视频高度计算放大倍数值
//                    max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
//                }
//
//                //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
//                videoWidth = (int) Math.ceil((float) videoWidth / max);
//                videoHeight = (int) Math.ceil((float) videoHeight / max);
//
//                //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
//                params.addRule(RelativeLayout.CENTER_VERTICAL, holder.root_view.getId());
//                holder.mSurfaceView.setLayoutParams(params);
//
//            }
//        });
      holder.title.setText(mUserList.get(position).getTitle()+"");
//      holder.h_dx_num.setText(mUserList.get(position).getLikes()+"");
//      holder.h_xx_num.setText(mUserList.get(position).getComments()+"");

     holder.item_tv_head.setAvatarUrl(mUserList.get(position).getThumb()+"");
//        //判断是否为点赞
//        if(mUserList.get(position).getIslike().equals("1"))
//        {
//            holder.dianzai.setImageResource(R.mipmap.icon_like_1);
//        }else
//        {
//            holder.dianzai.setImageResource(R.mipmap.icon_like_2);
//        }
//        //判断是否为关注
//        Toast.makeText(mContext, mUserList.get(position).getUid()+"", Toast.LENGTH_SHORT).show();
//        holder.dianzai.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //判断是否点击过，无点击的话出现红色心心
//                if(mIsLike==0)
//                {
//                    if (holder.iv_video_laudgif.getVisibility() == View.GONE) {
//                        holder.iv_video_laudgif.setVisibility(View.VISIBLE);
//                        Glide.with(mContext).load(R.drawable.laud_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_video_laudgif);
//                        mHandler.sendEmptyMessageDelayed(0, 2000);
//                    }
//                }
//                final ActiveBean activeBean=new ActiveBean();
//                 PhoneLiveApi.addLike(mUserList.get(position).getUid(), mUserList.get(position).getId(), new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//                    @Override
//                    public void onResponse(String response, int id) {
//                        JSONArray res = ApiUtils.checkIsSuccess(response);
//                        if(res!=null)
//                        {
//                            try {
//                                if(activeBean!=null){
//                                    activeBean.setIslike(res.getJSONObject(0).getString("islike"));
//                                    activeBean.setLikes(res.getJSONObject(0).getString("likes"));
//                                }
//                                if(holder.h_dx_num!=null){
//                                    holder.h_dx_num.setText(activeBean.getLikes());
//                                }
//                                if(holder.dianzai!=null){
//                                    mIsLike = res.getJSONObject(0).getInt("islike");
//                                    if (mUserList.get(position).getIslike().equals("1")) {
//                                        holder.dianzai.setBackgroundResource(R.drawable.lauded);
//                                    } else {
//                                        holder.dianzai.setBackgroundResource(R.drawable.nolaud);
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                });
//            }
//
//
//        });
//        holder.h_guanzhu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                PhoneLiveApi.showFollow(AppContext.getInstance().getLoginUid(), mUserList.get(position).getUid(), AppContext.getInstance().getToken(), new PhoneLiveApi.AttentionCallback() {
//                    @Override
//                    public void callback(boolean isAttention) {
//                   holder.  h_guanzhu.setVisibility(View.GONE);
//
//                    }
//                });
//
//                holder.h_guanzhu.setVisibility(View.GONE);
//            }
//        });
//        SendGiftBean sendGiftBean=new SendGiftBean();


    }
//    private SeekBar.OnSeekBarChangeListener change  = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            int progress = seekBar.getProgress();
//            //mu
////            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
////                mediaPlayer.seekTo(progress );
////            }
//            if ( video_view.isPlaying()) {
//                video_view.seekTo(progress );
//            }
//        }
//    };

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
 public   class Vh extends RecyclerView.ViewHolder {
        TextView  title ,h_time,h_dx_num,h_xx_num;
        ImageView dianzai,iv_video_laudgif,pl,h_guanzhu;

        RelativeLayout root_view;
        AvatarView item_tv_head;
        SeekBar seekbar;
          SurfaceView mSurfaceView;
        public Vh(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.title);
            video_view=itemView.findViewById(R.id.video_view);
            h_dx_num=itemView.findViewById(R.id.h_dx_num);
            h_xx_num=itemView.findViewById(R.id.h_xx_num);
            dianzai=itemView.findViewById(R.id.dianzai);
            h_time=itemView.findViewById(R.id.h_time);
            pl=itemView.findViewById(R.id.pl);
            root_view=itemView.findViewById(R.id.root_view);
            item_tv_head=itemView.findViewById(R.id.item_tv_head);
            iv_video_laudgif=itemView.findViewById(R.id.iv_video_laudgif);
            h_guanzhu=itemView.findViewById(R.id.h_guanzhu);
            data=itemView.findViewById(R.id.data);
            mSurfaceView =itemView.findViewById(R.id.test_surfaceView);
            seekbar=itemView.findViewById(R.id.seekbar);
            seekbar1=itemView.findViewById(R.id.seekbar1);

        }

    }
    //秒转换为时分秒
    public static String getTime(int second) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(second);
        return hms;
    }





    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        // 当进度条停止修改的时候触发
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 取得当前进度条的刻度
            int progress = seekBar.getProgress();
            if (video_view.isPlaying()) {
                // 设置当前播放的位置
                video_view.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

        }
    };

    public void onDestroy() {
        video_view = null;
    }

    public void onResume() {
        //video_view.start();
        video_view = null;
    }

    public void onPause() {
        video_view = null;
        //video_view.stopPlayback();
    }
    public void release() {
        video_view.stopPlayback();
    }
}
