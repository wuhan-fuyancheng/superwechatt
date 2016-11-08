package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.superwechat.utils.I;
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
    @BindView(R.id.bt_title_send)
    Button btTitleSend;
    private ProgressDialog progressDialog;
    //private String toAddUsername;
    String username;
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        username = getIntent().getStringExtra(I.User.USER_NAME);
        if (username == null) {
            finish();
        }
        msg = getString(R.string.addcontact_send_msg_prefix)
                + EaseUserUtils.getCurrentAppUserInfo().getMUserNick();
        initView();
    }

    private void initView() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitleAppname.setVisibility(View.VISIBLE);
        tvTitleAppname.setText(getString(R.string.add_friend));
        btTitleSend.setVisibility(View.VISIBLE);
        editAddfriend.setText(getString(R.string.addcontact_send_msg_prefix)
                + EaseUserUtils.getCurrentAppUserInfo().getMUserNick());
    }

    @OnClick({R.id.iv_back, R.id.bt_title_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finishRight(this);
                break;
            case R.id.bt_title_send:
                sendMsg();
                break;
        }
    }

    private void sendMsg() {
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.addcontact_be_friented);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(username, msg);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                            MFGT.finishRight(AddFriendActivity.this);
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

}
