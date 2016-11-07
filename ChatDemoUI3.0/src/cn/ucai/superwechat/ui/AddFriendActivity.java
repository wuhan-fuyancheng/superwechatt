package cn.ucai.superwechat.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucar.superwechat.R;

public class AddFriendActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title_appname)
    TextView tvTitleAppname;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.edit_addfriend)
    EditText editAddfriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitleAppname.setVisibility(View.VISIBLE);
        tvTitleAppname.setText(getString(R.string.add_friend));
        ivTitleRight.setVisibility(View.VISIBLE);
        editAddfriend.setText(getString(R.string.addcontact_send_msg_prefix)
        + EaseUserUtils.getCurrentAppUserInfo().getMUserNick());
    }

    @OnClick({R.id.iv_back, R.id.iv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finishRight(this);
                break;
            case R.id.iv_title_right:
                break;
        }
    }
}
