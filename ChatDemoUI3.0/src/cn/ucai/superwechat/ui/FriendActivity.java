package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.I;
import cn.ucai.superwechat.utils.MFGT;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        user= (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        if (user==null){
            MFGT.finishRight(this);
        }
        initView();
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
                break;
            case R.id.bt_friend_shiping:
                break;
        }
    }

    public void isFriends() {
        if (SuperWeChatHelper.getInstance().getAppcontactList().containsKey(user.getMUserName())){
            btFriendSendmessage.setVisibility(View.VISIBLE);
            btFriendShiping.setVisibility(View.VISIBLE);
        }else {
            btFriendAddcontact.setVisibility(View.VISIBLE);
        }

    }
}
