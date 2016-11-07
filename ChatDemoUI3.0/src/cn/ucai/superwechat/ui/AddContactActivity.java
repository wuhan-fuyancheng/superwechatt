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

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;
import cn.ucar.superwechat.R;

public class AddContactActivity extends BaseActivity {
    final String TAG=AddContactActivity.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_appname)
    TextView tvTitleAppname;
    @BindView(R.id.tv_title_findfriends)
    TextView tvTitleFindfriends;
    @BindView(R.id.edit_note)
    EditText editNote;
    private String toAddUsername;
    private ProgressDialog progressDialog;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_add_contact);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitleAppname.setVisibility(View.VISIBLE);
        tvTitleFindfriends.setVisibility(View.VISIBLE);
        tvTitleAppname.setText(getString(R.string.menu_addfriend));
        tvTitleFindfriends.setText(getString(R.string.search));
    }


    /**
     * search contact
     * @param searchContact
     */
    public void searchContact() {
    name=editNote.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            new EaseAlertDialog(this,R.string.please_enter_ausername);
            return;
        }
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.addcontact_search);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        searchAppUser();

    }

    private void searchAppUser() {
        NetDao.searchUser(this, name, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s!=null){
                    Result result= ResultUtils.getResultFromJson(s, User.class);
                    L.i(TAG,"result="+result);
                    if (result!=null&&result.isRetMsg()){
                        User user= (User) result.getRetData();
                        if (user!=null){
                            MFGT.gotoAddFriend(AddContactActivity.this,user);
                        }
                    }else {
                        CommonUtils.showShortToast(R.string.search_user_fail);
                        progressDialog.dismiss();
                    }

                }else {
                    CommonUtils.showShortToast(R.string.search_user_fail);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                CommonUtils.showShortToast(R.string.search_user_fail);
            }
        });
    }

    /**
     *  add contact
     * @param view
     */
    public void addContact(View view) {
        if (EMClient.getInstance().getCurrentUser().equals(editNote.getText().toString())) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (SuperWeChatHelper.getInstance().getContactList().containsKey(editNote.getText().toString())) {
            //let the user know the contact already in your contact list
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(editNote.getText().toString())) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }



        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(toAddUsername, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }


    @OnClick({R.id.iv_back, R.id.tv_title_findfriends})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finishRight(this);
                break;
            case R.id.tv_title_findfriends:
                searchContact();
                break;
        }
    }
}
