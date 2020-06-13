package com.qiyuzhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qiyuzhibo.phonelive.AppConfig;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.base.AbsFragment;
import com.qiyuzhibo.phonelive.ui.MainActivity;
import com.qiyuzhibo.phonelive.ui.customviews.ViewPagerIndicator;
import com.qiyuzhibo.phonelive.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;
//暂时弃用，用的是VideoFargment2
public class HomeFragment extends AbsFragment implements MainActivity.OnResumeCallback {

    private View mRootView;
    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private ImageView btn_top;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
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
                UIHelper.showWebView(getContext(), AppConfig.MAIN_URL + "/index.php?g=Appapi&m=RankedList&a=index&uid="+AppContext.getInstance().getLoginUser().id + "&token=" + AppContext.getInstance().getToken(), "");
            }
        });
        mIndicator.setTitles(new String[]{"直播", "视频", "附近","看片"});
        mIndicator.setVisibleChildCount(4);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.viewPager);
        if(savedInstanceState==null){
            final List<Fragment> fragmentList = new ArrayList<>();
            VideoFragment f1 = new VideoFragment();
            HotFragment f2 = new HotFragment();
            NearFragment f3 = new NearFragment();
            PersontoPersonFragment f4= new PersontoPersonFragment();
            WatchFragment f5=new WatchFragment();
            fragmentList.add(f1);
            fragmentList.add(f2);
            fragmentList.add(f3);
            fragmentList.add(f5);
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
}
