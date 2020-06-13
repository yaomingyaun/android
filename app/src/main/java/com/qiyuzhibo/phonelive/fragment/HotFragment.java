package com.qiyuzhibo.phonelive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qiyuzhibo.phonelive.adapter.LiveUserAdapter;
import com.qiyuzhibo.phonelive.bean.LiveJson;
import com.google.gson.Gson;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.api.remote.ApiUtils;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;
import com.qiyuzhibo.phonelive.base.BaseFragment;
import com.qiyuzhibo.phonelive.ui.VideoPlayerActivity;
import com.qiyuzhibo.phonelive.ui.other.OnItemEvent;
import com.qiyuzhibo.phonelive.utils.StringUtils;
import com.qiyuzhibo.phonelive.utils.TLog;
import com.qiyuzhibo.phonelive.widget.SlideshowView;
import com.qiyuzhibo.phonelive.widget.WPSwipeRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * @author 魏鹏
 * @dw 首页热门
 */
public class HotFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.lv_live_room)
    GridView mListUserRoom;

    //默认提示
    @InjectView(R.id.fensi)
    LinearLayout mLlFensi;

    @InjectView(R.id.load)
    LinearLayout mLoad;


    @InjectView(R.id.slideshowView)
    SlideshowView mSlideshowView;

    @InjectView(R.id.refreshLayout)
    WPSwipeRefreshLayout mSwipeRefreshLayout;

    private List<LiveJson> mUserList = new ArrayList<>();

    private LayoutInflater inflater;

    private LiveUserAdapter mHotUserListAdapter;

    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index_hot, null);
        ButterKnife.inject(this, view);
        this.inflater = inflater;

        initView();
        initData();

        return view;
    }

    private void initView() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.global));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mListUserRoom.setOnItemClickListener(new OnItemEvent(1000) {
            @Override
            public void singleClick(View v, int position) {
                if (AppContext.getInstance().getLoginUid() == null|| StringUtils.toInt(AppContext.getInstance().getLoginUid())==0) {
                    Toast.makeText(getContext(),"请登录..",Toast.LENGTH_SHORT).show();
                    return;
                }
                VideoPlayerActivity.startVideoPlayerActivity(getContext(), mUserList.get(position));
            }

        });
    }

    @Override
    public void initData() {

        //2016.09.06 无数据不显示轮播修改 wp
        mHotUserListAdapter = new LiveUserAdapter(getActivity().getLayoutInflater(), mUserList);
        mListUserRoom.setAdapter(mHotUserListAdapter);
    }


    private StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

            mSwipeRefreshLayout.setRefreshing(false);
            mLlFensi.setVisibility(View.GONE);
            mLoad.setVisibility(View.VISIBLE);
            mListUserRoom.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onResponse(String s, int id) {
            mSwipeRefreshLayout.setRefreshing(false);

            JSONArray res = ApiUtils.checkIsSuccess(s);

            try {
                if (res != null) {
                    TLog.error("2211");
                    mUserList.clear();
                    mHotUserListAdapter.notifyDataSetChanged();

                    //直播数据
                    JSONArray list = res.getJSONObject(0).getJSONArray("list");

                    if (list.length()<1){
                        mLlFensi.setVisibility(View.VISIBLE);
                        mLoad.setVisibility(View.GONE);
                    }else{
                        mLlFensi.setVisibility(View.GONE);
                        mLoad.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < list.length(); i++) {

                        LiveJson live = new Gson().fromJson(list.getJSONObject(i).toString(), LiveJson.class);
                        mUserList.add(live);
                    }

                    //轮播

                    if (isFirst) {
                        JSONArray rollPics = res.getJSONObject(0).getJSONArray("slide");
                        mSlideshowView.addDataToUI(rollPics);
                    }

                    isFirst = false;
                    fillUI();

                } else {
                    TLog.error("2222");
                    mLlFensi.setVisibility(View.VISIBLE);
                    mLoad.setVisibility(View.GONE);
                    mListUserRoom.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void fillUI() {

//        mLlFensi.setVisibility(View.GONE);
//        mLoad.setVisibility(View.GONE);
        mListUserRoom.setVisibility(View.VISIBLE);

        mHotUserListAdapter.notifyDataSetChanged();

    }

    public void selectTermsScreen() {
        PhoneLiveApi.requestHotData(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        selectTermsScreen();
    }

    //下拉刷新
    @Override
    public void onRefresh() {

        selectTermsScreen();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("selectTermsScreen");
    }
}
