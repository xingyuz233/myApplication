package com.example.xyzhang.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mPhoneNumberEditText;
    private EditText mPassWordEditText;
    private TextView mSignInBtn;
    private String originAddress = "http://localhost:8080/testapp/login.php";
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("OK".equals(msg.obj.toString())){
                jumpToInfo();
            }else if ("Wrong".equals(msg.obj.toString())){
                //todo
            }else {
                //todo
            }
        }
    };

    private void jumpToInfo() {
        Intent intent = new Intent(SignInActivity.this, send_code.class);
        startActivity(intent);
    }

    private void jumpToJoinNow() {
        Intent intent = new Intent(SignInActivity.this, join_now.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInBtn:
                login();
                break;
            case R.id.joinNowBtn:
                jumpToJoinNow();
                break;
        }
    }

    private boolean isInputValid() {
        //检查用户输入的合法性，这里暂且默认用户输入合法
        return true;
    }
    private void initView() {
        mPhoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        mPassWordEditText = (EditText) findViewById(R.id.passwordEditText);
        mSignInBtn = (TextView) findViewById(R.id.signInBtn);
    }

    private void initEvent() {
        mSignInBtn.setOnClickListener(this);
    }

    public void login() {
        //取得用户输入的账号和密码
        if (!isInputValid()){
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(User.PHONENUMBER, mPhoneNumberEditText.getText().toString());
        params.put(User.PASSWORD, mPassWordEditText.getText().toString());
        try {
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            HttpUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Message message = new Message();
                    message.obj = response;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    Message message = new Message();
                    message.obj = e.toString();
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}