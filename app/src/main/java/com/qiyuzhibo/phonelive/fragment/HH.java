package com.qiyuzhibo.phonelive.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.android.tedcoder.wkvideoplayer.dlna.util.LogUtil;
import com.google.gson.Gson;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.api.remote.ApiUtils;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;
import com.qiyuzhibo.phonelive.bean.AVBean;
import com.qiyuzhibo.phonelive.bean.TIBean;
import com.qiyuzhibo.phonelive.bean.UserBean;
import com.qiyuzhibo.phonelive.broadcast.DouYinLayoutManager;
import com.qiyuzhibo.phonelive.broadcast.OnViewPagerListener;
import com.qiyuzhibo.phonelive.broadcast.ToolsUtil;
import com.qiyuzhibo.phonelive.ui.customviews.RefreshLayout;
import com.qiyuzhibo.phonelive.widget.AvatarView;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Call;

public class HH extends Fragment implements RefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private DouYinLayoutManager douYinLayoutManager;

    List<TIBean> mUserList1 = new ArrayList<>();
    private UserBean mUser;

    private RefreshLayout refreshLayout;
    private int currentItemPosition = 0;  //用于临时保存第一出现要刷新数据时的item的position
    private boolean isNoMoreData;     //为 根据服务器返回的数据 是否有更多数据
    private int startLoadMorePosition = 3 ;// 当item滑动到list的倒数第三个位置是开启预加载
    private int page1;
    private MyAdapter newestAdapter;
    private   VideoView videoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hh,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取TOKen
        mUser = AppContext.getInstance().getLoginUser();
        //上滑下滑布局
        recyclerView = view.findViewById(R.id.gv_newest);
        refreshLayout=view.findViewById(R.id.refreshLayout);
        douYinLayoutManager = new DouYinLayoutManager(getContext(), OrientationHelper.VERTICAL,false);
        recyclerView.setLayoutManager(douYinLayoutManager);
        newestAdapter=new MyAdapter(getContext());
        recyclerView.setAdapter(newestAdapter);
        refreshLayout.setScorllView(recyclerView);
        refreshLayout.setOnRefreshListener(this);
        //请求数据
        getTach();
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
        //上滑下滑控制
        douYinLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onPageRelease(boolean isNest, View position) {
                //停止播放
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
                //开始播放
                playVideo(position);
            }
        });

    }
    //请求接口
    private void getTach() {
        page1=1;
        PhoneLiveApi.getVideohome(page1,mUser.token,new StringCallback() {
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
                        mUserList1.clear();
                        Gson g = new Gson();
                        for (int i = 0; i < resUserListJsonArr.length(); i++) {
                            mUserList1.add(g.fromJson(resUserListJsonArr.getString(i), TIBean.class));
                        }
                        if (mUserList1.size() > 0) {
                            if (newestAdapter == null) {
                                newestAdapter = new MyAdapter(getContext());
                                recyclerView.setAdapter(newestAdapter);
                            } else {
                                newestAdapter.setData(mUserList1);
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


        //adapter
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private List<TIBean> mUserList;
        private Context mContext;
        private RecyclerView mRecyclerView;
        private LayoutInflater mInflater;
        public MyAdapter(Context context){
            mContext = context;
            mUserList=new ArrayList<>();
            mInflater = LayoutInflater.from(mContext);
        }
        public void clear() {
            mUserList.clear();
            notifyDataSetChanged();
        }

        public void insertList(List<TIBean> list) {
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
        public void setData(List<TIBean> list) {
            this.mUserList = list;
            notifyDataSetChanged();
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.douyin, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
     //     String     uri="http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
            final Uri uri = Uri.parse(mUserList.get(position).getHref());
            videoView.setVideoURI(uri);
            //进度条  自带
//            MediaController nm=new MediaController(mContext);
//           videoView.setMediaController(nm);
           //头像
            holder.item_tv_head.setAvatarUrl(mUserList.get(position).getThumb()+"");
            //标题
            holder.title.setText(mUserList.get(position).getTitle()+"");
            //视频的总时长
            int time= Integer.parseInt(mUserList.get(position).getDuration());
           holder.data.setText(  ToolsUtil.getInstance(mContext).timeConversion(time));
           holder.h_time.setText(mUserList.get(position).getTags()+"");
           int p=videoView.getCurrentPosition();
           int t=videoView.getDuration();
           int max=holder.seekbar1.getMax();
           if(p>=0&&t!=0)
           {
               holder.seekbar1.setProgress(p*max/t);
           }

           holder.seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
               @Override
               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                   //                // 进度条开始拖动的时候

                // 首先判断是不是用户操作，不然每隔0.5秒定时器会刷新一直显示
                if (fromUser) {
                    SimpleDateFormat sdf;
                    double currentTime = (progress / 100.0 )* videoView.getDuration(); // 将当前位置换算成视频总时长的指定位置时间
                    int hour = (int) (currentTime / (3600 * 1000));
                    if ( hour < 0 || hour == 0) {
                        sdf = new SimpleDateFormat("mm:ss"); // 转换时间
                    } else {
                        sdf = new SimpleDateFormat("HH:mm:ss"); // 转换时间
                    }
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00")); // 设置中国时区
                    String result = sdf.format(currentTime) + " / " ; // 将时分秒转换成String类型


                 //   brightnessTextView.setVisibility(View.VISIBLE);
                   // brightnessTextView.setText( result);
                } /*else {
                    ((TextView)findViewById(R.id.id_progress)).setText(result);
//                }*/
               }

               @Override
               public void onStartTrackingTouch(SeekBar seekBar) {


                   int progress = seekBar.getProgress();
                   int position = progress * videoView.getDuration() / 100;
                   videoView.seekTo( position );

               }

               @Override
               public void onStopTrackingTouch(SeekBar seekBar) {
                    //控制视频跳转到目标位置
                int progress = seekBar.getProgress();
                int position = progress * videoView.getDuration() / 100;
                   videoView.seekTo( position );
               }
           });
        }
        @Override
        public int getItemCount() {
            return mUserList.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView img_thumb;
            ImageView img_play;
            RelativeLayout rootView;
            AvatarView item_tv_head;
            TextView title,data,h_time;
             SeekBar seekbar1;
            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
                item_tv_head=itemView.findViewById(R.id.item_tv_head);
                title =  itemView.findViewById(R.id.title);
                data=itemView.findViewById(R.id.data);
                h_time=itemView.findViewById(R.id.h_time);
                seekbar1=itemView.findViewById(R.id.seekbar1);
            }
        }
    }


    /**
     * 停止播放
     * @param
     */
    private void releaseVideo(View  itemView){
         videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
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
      getTach();
    }

    @Override
    public void onLoadMore() {
        page1++;
        PhoneLiveApi.getVideohome(page1,mUser.token,mLoadMoreCallback);
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
                    List<TIBean> list=new ArrayList<>();
                    Gson g = new Gson();
                    for (int i = 0; i < resUserListJsonArr.length(); i++) {
                        list.add(g.fromJson(resUserListJsonArr.getString(i), TIBean.class));
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

    @Override
    public void onPause() {
        super.onPause();
            videoView.pause();
    }
//退出
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        videoView.stopPlayback();
//    }

    @Override
    public void onResume() {
        super.onResume();
      // videoView.resume();
    }

    @Override
    public void onStart() {
        super.onStart();
        //videoView.start();
    }
    public  void  start()
    {
        videoView.start();
    }
}
