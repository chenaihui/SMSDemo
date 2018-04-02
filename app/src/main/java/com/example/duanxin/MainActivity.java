package com.example.duanxin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.ContactsPage;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button identifying_code;
    private Button Friend;
    private EditText phone_Number;
    private Button get_identifying_code;
    private EditText identifying_code_in;
    private Button put_identifying_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        identifying_code = (Button) findViewById(R.id.identifying_code);
        identifying_code.setOnClickListener(this);
        Friend = (Button) findViewById(R.id.Friend);
        Friend.setOnClickListener(this);
        phone_Number = (EditText) findViewById(R.id.phone_Number);
        phone_Number.setOnClickListener(this);
        get_identifying_code = (Button) findViewById(R.id.get_identifying_code);
        get_identifying_code.setOnClickListener(this);
        identifying_code_in = (EditText) findViewById(R.id.identifying_code_in);
        identifying_code_in.setOnClickListener(this);
        put_identifying_code = (Button) findViewById(R.id.put_identifying_code);
        put_identifying_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identifying_code:
                opeRegisterPager();
                break;
            case R.id.Friend:
                ContactsPage contactsPage = new ContactsPage();
                contactsPage.show(this);
                break;
            case R.id.get_identifying_code:
                String phone = phone_Number.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.getVerificationCode("86", phone);
                    Toast.makeText(this, "手机号是：" + phone, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.put_identifying_code:
                String phone2 = phone_Number.getText().toString().trim();
                String code = identifying_code_in.getText().toString().trim();
                if (TextUtils.isEmpty(phone2) || TextUtils.isEmpty(code)) {
                    Toast.makeText(this, "手机号和验证码均不能为空", Toast.LENGTH_SHORT).show();
                    SMSSDK.submitVerificationCode("+86", phone2, code);
                } else {
                    Toast.makeText(this, "手机号是：" + phone2 + "验证码是：" + code, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void opeRegisterPager() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked") HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Log.d("TAG", "opeRegisterPager()--country=" + country + "--phone" + phone);
                }
            }
        });
        registerPage.show(this);

    }

    private void submit() {
        // validate
        String phoneString = phone_Number.getText().toString().trim();
        if (TextUtils.isEmpty(phoneString)) {
            Toast.makeText(this, "13148120592", Toast.LENGTH_SHORT).show();
            return;
        }

        String et2String = identifying_code_in.getText().toString().trim();
        if (TextUtils.isEmpty(et2String)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }

    //防止内存泄漏
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    @SuppressWarnings("unchecked") HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Log.d("TAG", "提交验证码成功--country=" + country + "--phone" + phone);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    Log.d("TAG", "获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };


}
