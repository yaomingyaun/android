package com.qiyuzhibo.phonelive.ui;


import android.annotation.SuppressLint;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.qiyuzhibo.phonelive.R;


import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlay2Activity extends AppCompatActivity {
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private SensorManager sensorManager;
    private JZVideoPlayer.JZAutoFullscreenListener jzAutoFullscreenListener;

    private VideoPlayer video_player;

    private  String intent;
    private TextView tags11;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play2);

        //获得视屏标题和视频连接诶
         intent=getIntent().getStringExtra("video");
        String title=getIntent().getStringExtra("title");
        String thumb=getIntent().getStringExtra("thumb");
        String tagas=getIntent().getStringExtra("tagas");
        //ijkPlayer
       // String url = "http://9890.vod.myqcloud.com/9890_9c1fa3e2aea011e59fc841df10c92278.f20.mp4";
//        View rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
//        setContentView(rootView);
//        playerView=   new PlayerView(this)
//                .setTitle("什么")
//                .setScaleType(PlayStateParams.fitparent)
//                .hideMenu(true)
//                .forbidTouch(false)
//                .setPlaySource(url)
//                .startPlay();


         video_player= (VideoPlayer) findViewById(R.id.videoppp);
        tags11= (TextView) findViewById(R.id.tags11);
        tags11.setText(tagas);
        video_player.setPlayerType(VideoPlayer.AUTOFILL_TYPE_DATE);
         video_player.setUp(intent,null);
         //是否从上一次的位置继续播放
         video_player.continueFromLastPosition(true);
         //设置播放速度
         video_player.setSpeed(1.0f);

         //创建视频控制器
        VideoPlayerController controller = new VideoPlayerController(this);
         controller.setTitle(title);
         //设置视频时长
         controller.setLength(98000);

         //设置5秒不操作后则隐藏头部和底部布局视图
         controller.setHideTime(5000);
         //设置视频控制器
         video_player.setController(controller);
         video_player.postDelayed(new Runnable() {
            @Override
                public void run() {
                    video_player.start();
                    }
                    },500);
        controller.setLoadingType(2);




//        //饺子
//        jzVideoPlayerStandard= (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
//        //用于实现重力感应下切换横竖屏
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        jzAutoFullscreenListener = new JZVideoPlayer.JZAutoFullscreenListener();
//        jzVideoPlayerStandard.SAVE_PROGRESS=true;//保存进度
//     //播放视频
//        JZVideoPlayer.setVideoImageDisplayType(JZVideoPlayer.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
//        jzVideoPlayerStandard.setUp(intent,  JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, title);
//        //设置背景图片
//     Glide.with(this).load(thumb).into(jzVideoPlayerStandard.thumbImageView);

        //播放比例,可以设置为16:9,4:3
//        jzVideoPlayerStandard.widthRatio = 4;
//        jzVideoPlayerStandard.heightRatio = 3;
        //设置全屏播放
//        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //横向
//        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;  //纵向
    }



    @Override
    protected void onDestroy() {
//        if (mMediaPlayer != null) {
//            if (mMediaPlayer.isPlaying()) {
//                mMediaPlayer.stop();
//            }
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//        }
      //  VideoPlayerManager.instance().releaseVideoPlayer();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(JZVideoPlayer.backPress())
       {
         return;
       }
//        if (VideoPlayerManager.instance().onBackPressed()){
//            return;
//        }else {
//            //销毁页面
//            VideoPlayerManager.instance().releaseVideoPlayer();
//        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // sensorManager.unregisterListener(jzAutoFullscreenListener);
       // JZVideoPlayer.releaseAllVideos();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //播放器重力感应
      //  Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      //  sensorManager.registerListener(jzAutoFullscreenListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  VideoPlayerManager.instance().releaseVideoPlayer();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //从后台切换到前台，当视频暂停时或者缓冲暂停时，调用该方法重新开启视频播放
       // VideoPlayerManager.instance().resumeVideoPlayer();
    }



}
