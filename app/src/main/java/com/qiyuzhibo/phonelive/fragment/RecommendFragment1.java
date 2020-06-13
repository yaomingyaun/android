package com.qiyuzhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tedcoder.wkvideoplayer.dlna.util.LogUtil;
import com.google.gson.Gson;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.adapter.LieBiaoAdapter3;
import com.qiyuzhibo.phonelive.api.remote.ApiUtils;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;
import com.qiyuzhibo.phonelive.bean.AVBeanLB;
import com.qiyuzhibo.phonelive.bean.UserBean;
import com.qiyuzhibo.phonelive.ui.customviews.RefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class RecommendFragment1 extends Fragment implements RefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private  RefreshLayout refreshLayout;
    private int page;
    private LieBiaoAdapter3 lieBiaoAdapter3;
    List<AVBeanLB> mUserList = new ArrayList<>();
    private UserBean mUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frafment_recoment,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //声明注册
       // EventBus.getDefault().register(this);

        mUser = AppContext.getInstance().getLoginUser();
        recyclerView=view.findViewById(R.id.lb_newest);
        refreshLayout=view.findViewById(R.id.refreshLayout);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refreshLayout.setScorllView(recyclerView);
        refreshLayout.setOnRefreshListener(this);
    getWatch();
    }

//    //判断  是否是title
//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)   //必写  重要
//    public  void onMessage(Message bean)
//    {
//        if (bean.getId().equals("title"))
//        {
//            bean.getObject();
//         Toast.makeText(getContext(), bean.getObject()+"", Toast.LENGTH_SHORT).show();
//
//        }
//
//    }

    private void getWatch() {
        page=1;
        int caid=1;
        PhoneLiveApi.getVideo1(page, caid,mUser.token,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                refreshLayout.completeRefresh();
                recyclerView.setVisibility(View.INVISIBLE);
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
                            mUserList.add(g.fromJson(resUserListJsonArr.getString(i), AVBeanLB.class));
                        }

                        if (mUserList.size() > 0) {
                            if (lieBiaoAdapter3 == null) {
                                lieBiaoAdapter3 = new LieBiaoAdapter3(getActivity(), mUserList);
                                recyclerView.setAdapter(lieBiaoAdapter3);//BBB
                            } else {
                                lieBiaoAdapter3.setData(mUserList);
                                Log.i("AAQSQWDQDWQ",   mUserList+"");
                            }
                        } else {

                            recyclerView.setVisibility(View.INVISIBLE);

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                } else {

                    recyclerView.setVisibility(View.INVISIBLE);
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
        int caid=1;
        PhoneLiveApi.getVideo1(page,caid,mUser.token, mLoadMoreCallback);


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
                    List<AVBeanLB> list=new ArrayList<>();
                    Gson g = new Gson();
                    for (int i = 0; i < resUserListJsonArr.length(); i++) {
                        list.add(g.fromJson(resUserListJsonArr.getString(i), AVBeanLB.class));
                    }
                    if (list.size() > 0) {
                        if (lieBiaoAdapter3 != null) {
                            lieBiaoAdapter3.insertList(list);
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
    public void onDestroy() {
        super.onDestroy();
      //  EventBus.getDefault().unregister(this);

    }
}
