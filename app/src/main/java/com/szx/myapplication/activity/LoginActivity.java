package com.szx.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szx.myapplication.R;
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.UrlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private EditText mEt_Username;
    private EditText mEt_Password;
    private Button mBtn_login;
    private String mUserName;
    private String mPassword;
    private String mResponse;
    private Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEt_Username = (EditText) findViewById(R.id.login_username);
        mEt_Password = (EditText) findViewById(R.id.login_password);
        mBtn_login = (Button) findViewById(R.id.login_sign_button);
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUserName = mEt_Username.getText().toString();
                mPassword = mEt_Password.getText().toString();
                if (!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPassword)) {
                    RequestParams params = new RequestParams();
                    params.put("username", mUserName);
                    params.put("password", mPassword);
                    AsyncHttpUtil.post(LoginActivity.this, UrlUtil.getLoginUrl(), params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            mResponse = new String(responseBody);
                            doc = Jsoup.parse(new String(responseBody));

                            Log.w("LoginActivity", "onSuccess: " + new String(responseBody));

                            if (mResponse.contains("欢迎您回来")) {
                                Elements elemets = doc.select("ul.user_fun").select("li");
                                String href = elemets.get(2).select("a").attr("href");
                                Log.w("LoginActivity", "onSuccess: " + href);

                            } else if (mResponse.contains("登录失败，您还可以尝试")) {
                                String reason = doc.select("div.jump_c").select("p").get(0).text();
                                Toast.makeText(LoginActivity.this, reason, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(LoginActivity.this, "登陆失败，请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

