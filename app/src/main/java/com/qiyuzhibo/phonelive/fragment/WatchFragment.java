package com.qiyuzhibo.phonelive.fragment;

import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;




import com.android.tedcoder.wkvideoplayer.dlna.util.LogUtil;
import com.google.gson.Gson;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.adapter.VideoAdapter;
import com.qiyuzhibo.phonelive.api.remote.ApiUtils;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;

import com.qiyuzhibo.phonelive.base.BaseFragment;

import com.qiyuzhibo.phonelive.bean.ActiveBean;
import com.qiyuzhibo.phonelive.ui.customviews.RefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class WatchFragment extends BaseFragment implements RefreshLayout.OnRefreshListener{
    List<ActiveBean> mUserList = new ArrayList<>();
    @InjectView(R.id.gv_newest)
    RecyclerView mRecyclerView;
    @InjectView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    @InjectView(R.id.asdasda)
    TextView asdasda;
    private int page;
    private VideoAdapter newestAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch, null);
        ButterKnife.inject(this, view);

        initView(view);
        getWatch();
        return  view;

    }
@OnClick(R.id.asdasda)
public  void  onClick(View view)
{
    if(view.getId()==R.id.asdasda)
    {


    }
}
    @Override
    public void initView(View view) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRefreshLayout.setScorllView(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(this);
    }
    private void getWatch() {
        page=1;
        PhoneLiveApi.getVideo(page, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mRefreshLayout.completeRefresh();
                mRecyclerView.setVisibility(View.INVISIBLE);
                AppContext.toast("加载失败");
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e("Logger",response);
                mRefreshLayout.completeRefresh();
                JSONArray resUserListJsonArr = ApiUtils.checkIsSuccess(response);
                if (null != resUserListJsonArr) {
                    try {
                        mUserList.clear();
                        Gson g = new Gson();
                        for (int i = 0; i < resUserListJsonArr.length(); i++) {
                            mUserList.add(g.fromJson(resUserListJsonArr.getString(i), ActiveBean.class));
                        }

                        if (mUserList.size() > 0) {
                            if (newestAdapter == null) {
                                newestAdapter = new VideoAdapter(getActivity(), mUserList);
                                mRecyclerView.setAdapter(newestAdapter);//BBB
                            } else {
                                newestAdapter.setData(mUserList);
                                Log.i("AAQSQWDQDWQ",   mUserList+"");
                            }
                        } else {

                            mRecyclerView.setVisibility(View.INVISIBLE);

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                } else {

                    mRecyclerView.setVisibility(View.INVISIBLE);
                }

            }
        });

    }


    @Override
    public void onRefresh() {
        getWatch();
    }

    @Override
    public void onLoadMore() {
        page++;
        PhoneLiveApi.getVideo(page, mLoadMoreCallback);
    }
    private StringCallback mLoadMoreCallback=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeRefresh();

            mRecyclerView.setVisibility(View.INVISIBLE);
            AppContext.toast("加载失败");
        }

        @Override
        public void onResponse(String response, int id) {

            mRefreshLayout.completeRefresh();
            JSONArray resUserListJsonArr = ApiUtils.checkIsSuccess(response);

            if (resUserListJsonArr.length()>0) {

                try {
                    List<ActiveBean> list=new ArrayList<>();
                    Gson g = new Gson();
                    for (int i = 0; i < resUserListJsonArr.length(); i++) {
                        list.add(g.fromJson(resUserListJsonArr.getString(i), ActiveBean.class));
                    }

                    if (list.size() > 0) {
                        if (newestAdapter != null) {
                            newestAdapter.insertList(list);

                        }
                    } else {

                        mRecyclerView.setVisibility(View.INVISIBLE);

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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
