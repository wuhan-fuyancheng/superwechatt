package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.NetDao;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.I;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;
import cn.ucar.superwechat.R;

public class FriendActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_appname)
    TextView tvTitleAppname;
    @BindView(R.id.iv_frend_avatar)
    ImageView ivFrendAvatar;
    @BindView(R.id.tv_frend_nick)
    TextView tvFrendNick;
    @BindView(R.id.tv_frend_name)
    TextView tvFrendName;
    @BindView(R.id.bt_friend_addcontact)
    Button btFriendAddcontact;
    @BindView(R.id.bt_friend_sendmessage)
    Button btFriendSendmessage;
    @BindView(R.id.bt_friend_shiping)
    Button btFriendShiping;
    User user=null;
    String username=null;
    boolean isFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        username= (String) getIntent().getSerializableExtra(I.User.USER_NAME);

        user=SuperWeChatHelper.getInstance().getAppcontactList().get(username);
        if (user==null){
            syncUserInfo();
            isFriend=false;
        }else {
            isFriend=true;
            initView();
        }
    }

    private void syncUserInfo() {
        NetDao.syncUserInfo(this, username, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s!=null){
                    Result result= ResultUtils.getResultFromJson(s,User.class);
                    if (result!=null&&result.isRetMsg()){
                        User u= (User) result.getRetData();
                        if (u!=null){
                            user=u;
                            initView();
                            if (isFriend){
                                SuperWeChatHelper.getInstance().saveAppContact(user);
                            }

                            else {
                                syncFail();
                            }

                        }else {
                            syncFail();
                        }
                    }else {
                        syncFail();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void syncFail() {
        if (!isFriend){
            MFGT.finishRight(this);
            return;
        }
    }

    private void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitleAppname.setVisibility(View.VISIBLE);
        tvTitleAppname.setText(getString(R.string.userinfo_txt_profile));
        setUserInfo();
        isFriends();
    }
    private void setUserInfo() {
        EaseUserUtils.setUserAvatar(this,user.getMUserName(),ivFrendAvatar);
        EaseUserUtils.setUserNick(user.getMUserNick(),tvFrendNick);
        EaseUserUtils.setUsername(user.getMUserName(),tvFrendName);
    }

    @OnClick({R.id.iv_back, R.id.bt_friend_addcontact, R.id.bt_friend_sendmessage, R.id.bt_friend_shiping})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finishRight(this);
                break;
            case R.id.bt_friend_addcontact:
            MFGT.gotoYanzhengAcitiviy(this,user.getMUserName());
                break;
            case R.id.bt_friend_sendmessage:
            MFGT.gotoChat(this,user.getMUserName());
                break;
            case R.id.bt_friend_shiping:
                if (!EMClient.getInstance().isConnected())
                    Toast.makeText(this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(this, VideoCallActivity.class).putExtra("username", user.getMUserName())
                            .putExtra("isComingCall", false));
                    // videoCallBtn.setEnabled(false);
                    //inputMenu.hideExtendMenuContainer();
                }

                break;
        }
    }

    public void isFriends() {
        if (isFriend){
            btFriendSendmessage.setVisibility(View.VISIBLE);
            btFriendShiping.setVisibility(View.VISIBLE);
        }else {
            btFriendAddcontact.setVisibility(View.VISIBLE);
        }

    }
}
