package cn.ucai.superwechat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucar.superwechat.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    @BindView(R.id.tv_pr_nick)
    TextView tvPrNick;
    @BindView(R.id.tv_pr_weixinname)
    TextView tvPrWeixinname;
    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.layout_pr_avatar, R.id.icon_money, R.id.icon_me_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_pr_avatar:
                break;
            case R.id.icon_money:
                break;
            case R.id.icon_me_setting:
                break;
        }
    }
}
