package com.qiyuzhibo.phonelive.fragment;


import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.tedcoder.wkvideoplayer.dlna.util.LogUtil;
import com.google.gson.Gson;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.adapter.VideoAdapter3;
import com.qiyuzhibo.phonelive.api.remote.ApiUtils;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;
import com.qiyuzhibo.phonelive.bean.AVBean;
import com.qiyuzhibo.phonelive.bean.ActiveBean;
import com.qiyuzhibo.phonelive.bean.UserBean;
import com.qiyuzhibo.phonelive.broadcast.DouYinLayoutManager;
import com.qiyuzhibo.phonelive.broadcast.OnViewPagerListener;
import com.qiyuzhibo.phonelive.ui.customviews.RefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

//推荐，暂时弃用
public class RecommendFragment extends Fragment implements RefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private DouYinLayoutManager douYinLayoutManager;
    private int page;
    private MediaPlayer mMediaPlayer;
    List<AVBean> mUserList = new ArrayList<>();
    ActiveBean videoBean;
    private UserBean mUser;
    private VideoAdapter3 newestAdapter;
    private RefreshLayout refreshLayout;
    private SurfaceHolder mHolder;
    private int currentItemPosition = 0;  //用于临时保存第一出现要刷新数据时的item的position
    private boolean isNoMoreData;     //为 根据服务器返回的数据 是否有更多数据
    private int startLoadMorePosition = 3 ;// 当item滑动到list的倒数第三个位置是开启预加载
    private  SurfaceView mSurfaceView;
    private VideoView videoView;

    private int firstVisibleItem;
    private int lastVisibleItem;
   // private final int STOP_PLAYER = 0x2000;
  //  private final int START_PLAYER = 0x2001;
   // private final int PAUSE_PLAYER = 0x2002;
   // private final int SET_VIDEO_URL = 0x2003;
  // View view1;
    private boolean playerPaused;
   // private Handler handler;
//    private  Handler handler1=new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what)
//            {
//                case STOP_PLAYER:
//                    stopPlayer();
//                    break;
//                case START_PLAYER:
//                    playVideo(view1);
//                    break;
//                case PAUSE_PLAYER:
//                    pausePlayer();
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_watch,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = AppContext.getInstance().getLoginUser();
        recyclerView = view.findViewById(R.id.gv_newest);
        refreshLayout=view.findViewById(R.id.refreshLayout);
        //上滑下滑
        douYinLayoutManager = new DouYinLayoutManager(getContext(), OrientationHelper.VERTICAL,false);
        recyclerView.setLayoutManager(douYinLayoutManager);
        newestAdapter=new VideoAdapter3(getContext());
        recyclerView.setAdapter(newestAdapter);

        refreshLayout.setScorllView(recyclerView);
        refreshLayout.setOnRefreshListener(this);
        //预加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = douYinLayoutManager.findLastVisibleItemPosition();
                int itemCount =douYinLayoutManager.getItemCount();
                if (lastVisibleItemPosition == (itemCount - startLoadMorePosition ) && dy >0
                        && currentItemPosition !=lastVisibleItemPosition ){
                    currentItemPosition = lastVisibleItemPosition;  //记录item的position
                    if (!isNoMoreData){
                        // 开始加载
                        getTach();
                    }
                }

            }
        });
     douYinLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
         @Override
         public void onPageRelease(boolean isNest, View position) {
             releaseVideo(position);
         }

         @Override
         public void onPageSelected(boolean isButten, View position) {
             int index = 0;
             if (isButten){
                 index = 0;
             }else {
                 index = 1;
             }
             playVideo(position);
         }
     });
        getTach();
    }

    //获取视频接口
    private void getTach() {
        page=1;
         int    caid=1;
        PhoneLiveApi.getVideo1(page,caid,mUser.token,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                refreshLayout.completeRefresh();
                recyclerView.setVisibility(View.VISIBLE);
                AppContext.toast("加载失败");
            }
            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("Logger",response);
                refreshLayout.completeRefresh();
                JSONArray resUserListJsonArr = ApiUtils.checkIsSuccess(response);
                if (null != resUserListJsonArr) {
                    try {
                        mUserList.clear();
                        Gson g = new Gson();
                        for (int i = 0; i < resUserListJsonArr.length(); i++) {
                            mUserList.add(g.fromJson(resUserListJsonArr.getString(i), AVBean.class));
                        }
                        if (mUserList.size() > 0) {
                            if (newestAdapter == null) {
                                newestAdapter = new VideoAdapter3(getContext());
                                recyclerView.setAdapter(newestAdapter);//BBB
                            } else {
                                newestAdapter.setData(mUserList);
                            }
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });
    }



    /**
     * 停止播放
     * @param
     */
    private void releaseVideo(View  itemView){
        //View itemView=recyclerView.getChildAt(position);
          //mSurfaceView = itemView.findViewById(R.id.test_surfaceView);
     videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
    videoView.stopPlayback();
//        mMediaPlayer = new MediaPlayer();
//        mHolder = mSurfaceView.getHolder();
//        mHolder.setKeepScreenOn(true);
//
//        mHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mediaPlayer) {
//
//
//                    }
//                });
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//                mMediaPlayer.release();
//            }
//        });
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }

    /**
     * 开始播放
     * @param
     */

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void playVideo(View itemView) {
        //View itemView=recyclerView.getChildAt(0);
      //  final SurfaceView mSurfaceView = itemView.findViewById(R.id.test_surfaceView);
     videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();
//        mMediaPlayer = new MediaPlayer();
//        mHolder = mSurfaceView.getHolder();
//        mHolder.setKeepScreenOn(true);
//        mHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//
//                    mMediaPlayer.start();
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//
//            }
//        });

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()){
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                }else {
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;

                }
            }
        });

}

    @Override
    public void onRefresh() {
        //   getTach();
        Toast.makeText(getContext(), "上拉数据", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore() {
        int    caid=1;
        page++;
        PhoneLiveApi.getVideo1(page, caid,mUser.token,mLoadMoreCallback);
    }
    private StringCallback mLoadMoreCallback=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            refreshLayout.completeRefresh();

            recyclerView.setVisibility(View.INVISIBLE);
            AppContext.toast("加载失败");
        }

        @Override
        public void onResponse(String response, int id) {

            refreshLayout.completeRefresh();
            JSONArray resUserListJsonArr = ApiUtils.checkIsSuccess(response);

            if (resUserListJsonArr.length()>0) {

                try {
                    List<AVBean> list=new ArrayList<>();
                    Gson g = new Gson();
                    for (int i = 0; i < resUserListJsonArr.length(); i++) {
                        list.add(g.fromJson(resUserListJsonArr.getString(i), AVBean.class));
                    }

                    if (list.size() > 0) {
                        if (newestAdapter != null) {
                            newestAdapter.insertList(list);

                        }
                    } else {

                        recyclerView.setVisibility(View.INVISIBLE);

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            } else {
                AppContext.toast("已经没有更多数据了");
            }
        }
    };
//    private void stopPlayer() {
//        if (null != videoView) {
//            videoView.stopPlayback();
//            handler.removeCallbacksAndMessages(null);
//        }
//    }
//    private void pausePlayer() {
//        if (null != videoView) {
//            playerPaused = true;
//       videoView.getCurrentPosition();
//            videoView.pause();
//        }
//    }
//    private void sendStopVideoMsg() {
//        removeMessages();
//        if (!handler.hasMessages(STOP_PLAYER)) {
//            if (null != videoView) {
//                handler.sendEmptyMessage(STOP_PLAYER);
//            }
//        }
//    }
//    private void sendPauseVideoMsg() {
//        removeMessages();
//        if (!handler.hasMessages(PAUSE_PLAYER)) {
//            if (null != videoView) {
//                handler.sendEmptyMessage(PAUSE_PLAYER);
//            }
//        }
//    }
//    private void removeMessages() {
//        if (handler.hasMessages(START_PLAYER)) {
//            handler.removeMessages(START_PLAYER);
//        }
//        if (handler.hasMessages(STOP_PLAYER)) {
//            handler.removeMessages(STOP_PLAYER);
//        }
//        if (handler.hasMessages(PAUSE_PLAYER)) {
//            handler.removeMessages(PAUSE_PLAYER);
//        }
//        if (handler.hasMessages(SET_VIDEO_URL)) {
//            handler.removeMessages(SET_VIDEO_URL);
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
     newestAdapter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
       //newestAdapter.onPause();
        //sendPauseVideoMsg();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
     // newestAdapter.onDestroy();
        //sendStopVideoMsg();
        newestAdapter.release();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    //handler.removeCallbacks((Runnable) handler1);
       //newestAdapter.onDestroyView();
        newestAdapter.release();
        videoView.stopPlayback();
    }


    }
