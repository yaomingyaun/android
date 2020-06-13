package com.qiyuzhibo.phonelive.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.qiyuzhibo.phonelive.AppContext;
import com.qiyuzhibo.phonelive.R;
import com.qiyuzhibo.phonelive.api.remote.ApiUtils;
import com.qiyuzhibo.phonelive.api.remote.PhoneLiveApi;
import com.qiyuzhibo.phonelive.base.ToolBarBaseActivity;
import com.qiyuzhibo.phonelive.bean.UserBean;
import com.qiyuzhibo.phonelive.ui.customviews.ActivityTitle;
import com.qiyuzhibo.phonelive.utils.LiveUtils;
import com.qiyuzhibo.phonelive.widget.BlackButton;
import com.qiyuzhibo.phonelive.widget.BlackEditText;
import com.qiyuzhibo.phonelive.widget.BlackTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * 修改资料
 */
public class EditInfoActivity extends ToolBarBaseActivity {
    @InjectView(R.id.edit_input)
    BlackEditText mInPutText;
    @InjectView(R.id.tv_prompt)
    BlackTextView mTvPrompt;
    @InjectView(R.id.iv_editInfo_clean)
    ImageView mInfoClean;
    @InjectView(R.id.btn_edit_save)
    BlackButton mBtnSave;

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;


    public final static String EDITKEY     = "EDITKEY";
    public final static String EDITACTION  = "EDITACTION";
    public final static String EDITPROMP   = "EDITPROMP";
    public final static String EDITDEFAULT = "EDITDEFAULT";
    private Intent intent;
    private String key;
    private String value;



    @Override
    public void initView() {

        mInfoClean.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);
        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    @Override
    public void initData() {
        intent = getIntent();
        mInPutText.setText(intent.getStringExtra(EDITDEFAULT));
        mActivityTitle.setTitle(intent.getStringExtra(EDITACTION));
        mTvPrompt.setText(intent.getStringExtra(EDITPROMP));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_save:
                saveInfo();
                break;
            case R.id.iv_editInfo_clean:
                mInPutText.setText("");
                break;
        }
    }
    /**
     * @dw 提交修改数据
     * */
    private void saveInfo() {
        key = intent.getStringExtra(EDITKEY);
        value = mInPutText.getText().toString();

        // zxy 0418  判断输入长度
        if(key.equals("user_nicename") && value.length()>15){
            showToast3("昵称长度超过限制",0);
            return;
        }else if(key.equals("signature") && value.length()>20){
            showToast3("签名长度超过限制",0);
            return;
        }else if(key.equals("user_nicename") && value.length() > 8){
            showToast3("昵称长度不符合要求",0);
            return;
        }

        // zxy 0418  判断输入长度
        PhoneLiveApi.saveInfo(LiveUtils.getFiledJson(key,value),
                getUserID(),
                getUserToken(),
                callback);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("saveInfo");
    }

    private StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e,int id) {
            showToast2(getString(R.string.editfail));
        }

        @Override
        public void onResponse(String s,int id) {
            JSONArray res = ApiUtils.checkIsSuccess(s);
            if(null != res){
                showToast3(getString(R.string.editsuccess),0);
                UserBean u =  AppContext.getInstance().getLoginUser();
                if(key.equals("user_nicename")){
                    u.user_nicename = value;
                }else if(key.equals("signature")){
                    u.signature = value;
                }
                AppContext.getInstance().updateUserInfo(u);
                finish();
            }

        }
    };

    @Override
    protected boolean hasActionBar() {
        return false;
    }
}