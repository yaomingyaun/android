package com.qiyuzhibo.phonelive.cocopay;

import android.app.Activity;
import android.widget.Toast;

import com.android.tedcoder.wkvideoplayer.dlna.util.LogUtil;
import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;
import com.qiyuzhibo.phonelive.utils.UIHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


public class CocoPay {
    private Activity context;

    public CocoPay(Activity context) {
        this.context = context;
    }

    /**
     * @dw 初始化COcoPay支付
     * @param price 价格
     * @param num 数量
     * */
    public void initPay(String uid,String price, String num,String changeid,int type) {
        PhoneLiveApi.getCocoPayOrderNum(uid, changeid, num, price, type, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(AppContext.getInstance(),"获取订单失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject res = new JSONObject(response);
                    LogUtil.e("LOGGER",res+"");
                    if (null != res.getJSONObject("data")){
                        if (null != res.getJSONObject("data").getString("info")){

                            UIHelper.showWebView(context,res.getJSONObject("data").getString("info"),"充值");
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
