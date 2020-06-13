package com.qiyuzhibo.phonelive.ui;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.qiyuzhibo.phonelive.cocopay.CocoPay;
import com.google.gson.Gson;
import com.qiyuzhibo.phonelive.AppConfig;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.adapter.RechangeAdapter;
import com.qiyuzhibo.phonelive.alipay.Keys;
import com.qiyuzhibo.phonelive.bean.RechargeJson;
import com.qiyuzhibo.phonelive.ui.customviews.ActivityTitle;
import com.qiyuzhibo.phonelive.utils.DialogHelp;
import com.qiyuzhibo.phonelive.utils.StringUtils;
import com.qiyuzhibo.phonelive.WxPay.WChatPay;
import com.qiyuzhibo.phonelive.alipay.AliPay;
import com.qiyuzhibo.phonelive.api.remote.ApiUtils;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;
import com.qiyuzhibo.phonelive.base.ToolBarBaseActivity;
import com.qiyuzhibo.phonelive.bean.RechargeBean;
import com.qiyuzhibo.phonelive.widget.BlackTextView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * 我的钻石
 */
public class UserDiamondsActivity extends ToolBarBaseActivity {

    @InjectView(R.id.lv_select_num_list)
    GridView mSelectNumListItem;

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    private List<RechargeBean> mRechargeList = new ArrayList<>();

//    private final int WX_PAY    = 1;
//    private final int ALI_PAY   = 2;

    private final int WX_PAY = 2;
    private final int ALI_PAY = 1;

    private int PAY_MODE = WX_PAY;

    private BlackTextView mCoin;
    private View mHeadView;

    private WChatPay mWChatPay;
    private AliPay mAliPayUtils;
    private CocoPay cocoPay;

    private RechangeAdapter mRechangeAdapter;
    private RechargeJson mRechargeJson;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_diamonds;
    }


    @Override
    public void initView() {

        mActivityTitle.setTitle("我的" + AppConfig.CURRENCY_NAME);
        mCoin = (BlackTextView) findViewById(R.id.tv_coin);


        mSelectNumListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                actionPay(String.valueOf(mRechargeList.get(position).money), mRechargeList.get(position).coin
                        , mRechargeList.get(position).id, ALI_PAY);

                DialogHelp.getSelectDialog(UserDiamondsActivity.this, new String[]{"支付宝", "微信"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        PAY_MODE = i == 0 ? ALI_PAY : WX_PAY;
                        actionPay(String.valueOf(mRechargeList.get(position).money), mRechargeList.get(position).coin
                                , mRechargeList.get(position).id, PAY_MODE);

                    }
                }).create().show();


            }
        });
        mRechangeAdapter = new RechangeAdapter(mRechargeList);
        mSelectNumListItem.setAdapter(mRechangeAdapter);

        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void initData() {
        requestData();

        mAliPayUtils = new AliPay(this);
        mWChatPay = new WChatPay(this);
        cocoPay = new CocoPay(this);


    }

    private void actionPay(String money, String num, String changeid, int type) {

        String uid = AppContext.getInstance().getLoginUid();
        if (PAY_MODE == ALI_PAY && checkPayMode()) {

            mAliPayUtils.initPay(money, num,changeid);

        }else if(checkPayMode()){

            mWChatPay.initPay(money, num,changeid);
        }
        cocoPay.initPay(uid, money, num, changeid, type);
    }

    //检查支付配置
    private boolean checkPayMode() {

        if (PAY_MODE == ALI_PAY) {
            if (mRechargeJson.aliapp_switch.equals("1")) {
                return true;
            } else {

                showToast3("支付宝未开启", 0);
                return false;
            }
        } else if (PAY_MODE == WX_PAY) {
            if (mRechargeJson.wx_switch.equals("1")) {
                return true;
            } else {

                showToast3("微信未开启", 0);
                return false;
            }
        }

        return false;

    }

    private void requestData() {

        PhoneLiveApi.requestBalance(getUserID(), getUserToken(), new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray array = ApiUtils.checkIsSuccess(response);

                if (array != null) {

                    try {
                        mRechargeJson = new Gson().fromJson(array.getString(0), RechargeJson.class);
                        mRechargeList.clear();
                        mRechargeList.addAll(mRechargeJson.rules);
                        mRechangeAdapter.notifyDataSetChanged();
                        mCoin.setText(mRechargeJson.coin);

                        //微信支付appid
                        AppConfig.GLOBAL_WX_KEY = mRechargeJson.wx_appid;

                        //支付宝
                        Keys.DEFAULT_PARTNER = mRechargeJson.aliapp_partner;
                        Keys.DEFAULT_SELLER = mRechargeJson.aliapp_seller_id;
                        Keys.PRIVATE = mRechargeJson.aliapp_key_android;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        requestData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    //充值结果
    public void rechargeResult(boolean isOk, String rechargeMoney) {
        if (isOk) {
            mCoin.setText(String.valueOf(StringUtils.toInt(mCoin.getText().toString()) +
                    StringUtils.toInt(rechargeMoney)));
        }
    }


}
