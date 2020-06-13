package com.qiyuzhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.qiyuzhibo.phonelive.AppConfig;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.api.remote.ApiUtils;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;
import com.qiyuzhibo.phonelive.base.AbsFragment;
import com.qiyuzhibo.phonelive.bean.ActiveBean;
import com.qiyuzhibo.phonelive.bean.Message;
import com.qiyuzhibo.phonelive.bean.ProfitBean;
import com.qiyuzhibo.phonelive.bean.TabBean;
import com.qiyuzhibo.phonelive.ui.MainActivity;
import com.qiyuzhibo.phonelive.ui.customviews.ViewPagerIndicator;
import com.qiyuzhibo.phonelive.utils.UIHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class VideoFargment2 extends AbsFragment implements MainActivity.OnResumeCallback  {
    private View mRootView;
    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private ImageView btn_top;
    private TabBean tabBeans1;
    private TabBean tabBeans2;

    HH f6;
    private List<Fragment> fragmentList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_video2, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mIndicator = (ViewPagerIndicator) mRootView.findViewById(R.id.indicator);
        btn_top = (ImageView) mRootView.findViewById(R.id.btn_top);
        btn_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showWebView(getContext(), AppConfig.MAIN_URL + "/index.php?g=Appapi&m=RankedList&a=index&uid="+ AppContext.getInstance().getLoginUser().id + "&token=" + AppContext.getInstance().getToken(), "");
            }
        });
    //获取导航栏
        PhoneLiveApi.getTab(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

                JSONArray res = ApiUtils.checkIsSuccess(response);
                try {



                    tabBeans1= new Gson().fromJson(res.getString(1), TabBean.class);
                    tabBeans2= new Gson().fromJson(res.getString(2), TabBean.class);

                //    Toast.makeText(getContext(), tabBeans1.getName()+"", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
//        List<String> list=new ArrayList<>();
//        list.add(tabBeans1.getName());

        mIndicator.setTitles(new String[]{"推荐","日韩" ,"国产", "自拍","欧美","动漫"});
        mIndicator.setVisibleChildCount(6);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.viewPager);
        if(savedInstanceState==null){
            fragmentList = new ArrayList<>();
            //推荐
             f6 = new HH();
            RecommendFragment1     f1 = new RecommendFragment1();
            //户外直播改为视频
          //  HotFragment f2 = new HotFragment();
            //国产
           RecommendFragment2 f2 = new RecommendFragment2();
            //热舞改为视频
        //    NearFragment f3 = new NearFragment();
            //自拍替换为用户上传
           // RecommendFragment3 f3 = new RecommendFragment3();
            WatchFragment f3=new WatchFragment();
            //欧美
            RecommendFragment4 f4=new RecommendFragment4();
            //日韩
            RecommendFragment5 f5=new RecommendFragment5();
           // PersontoPersonFragment f4= new PersontoPersonFragment();
            //附近


            fragmentList.add(f6);
            fragmentList.add(f1);
           fragmentList.add(f2);
            fragmentList.add(f3);
            fragmentList.add(f4);
            fragmentList.add(f5);
           // fragmentList.add(f5);

//            fragmentList.add(f4);
            mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return fragmentList.get(position);
                }

                @Override
                public int getCount() {
                    return fragmentList.size();
                }
            });
            mViewPager.setOffscreenPageLimit(4);
            mIndicator.setViewPager(mViewPager);
            mIndicator.setListener(new ViewPagerIndicator.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    publishPosition(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        mRootView.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showScreen(getActivity());
            }
        });
            //判断滑到f2
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {

                if(position==1)
                {

                    f6.onPause();

                }
                if(position==2)
                {

                    f6.onPause();

                }
                if(position==3)
                {

                    f6.onPause();

                }
                if(position==4)
                {

                    f6.onPause();

                }
                if (position==0)
                {

                f6.start();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void publishPosition(int position) {
//        for (PageChangedListener listener : mPageChangedListeners) {
//            listener.onPageChanged(position);
//        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            publishPosition(mViewPager.getCurrentItem());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResumeRefresh() {
        publishPosition(mViewPager.getCurrentItem());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ///划走销毁
       // f6.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        VideoView videoView;
       // f6.onDestroyView();
    }
}
