/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.MD5;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucar.superwechat.R;

/**
 * register screen
 *
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_appname)
    TextView tvTitleAppname;
    @BindView(R.id.et_rigister_username)
    EditText etRigisterUsername;
    @BindView(R.id.et_register_usernick)
    EditText etRegisterUsernick;
    @BindView(R.id.et_register_userpassword)
    EditText etRegisterUserpassword;
    @BindView(R.id.et_register_userpasswordtwo)
    EditText etRegisterUserpasswordtwo;
    ProgressDialog pd;
    RegisterActivity mContext;
    String username;
    String nickname;
    String pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_register);
        ButterKnife.bind(this);
        mContext=this;
        pd=new ProgressDialog(this);
        initView();

    }

    private void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitleAppname.setVisibility(View.VISIBLE);
        tvTitleAppname.setText(R.string.register);
    }

    public void register() {
        username = etRigisterUsername.getText().toString().trim();
        nickname = etRegisterUsernick.getText().toString().trim();
        pwd = etRegisterUserpassword.getText().toString().trim();
        String confirm_pwd = etRegisterUserpasswordtwo.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            etRigisterUsername.requestFocus();
            return;
        } else if (!username.matches("[a-zA-Z]\\w{5,15}")){
            Toast.makeText(this, getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
            etRigisterUsername.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(nickname)){
            Toast.makeText(this, getResources().getString(R.string.toast_nick_not_isnull), Toast.LENGTH_SHORT).show();
            etRegisterUserpassword.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            etRegisterUserpassword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            etRegisterUserpasswordtwo.requestFocus();
            return;
        } else if (!pwd.equals(confirm_pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();
            registerAppServer();
        }
    }

    private void registerAppServer() {
        NetDao.register(mContext, username, nickname, pwd, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                registerEMServer();
                if (result!=null){
                    registerEMServer();
                }else {
                    unregisterAppServer();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void unregisterAppServer() {
        NetDao.unregister(mContext, username, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                Toast.makeText(RegisterActivity.this, "取消注册成功", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            @Override
            public void onError(String error) {
                pd.dismiss();
            }
        });
    }

    private void registerEMServer() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    EMClient.getInstance().createAccount(username, MD5.getMessageDigest(pwd));
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            // save current user
                            SuperWeChatHelper.getInstance().setCurrentUserName(username);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                            MFGT.gotologin(mContext);
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        MFGT.finish(this);
    }

    @OnClick({R.id.iv_back, R.id.bt_register_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finish(this);
                break;
            case R.id.bt_register_login:
                register();
                break;
        }
    }
}
