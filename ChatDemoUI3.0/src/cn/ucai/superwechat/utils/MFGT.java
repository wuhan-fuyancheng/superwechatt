package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Intent;

import com.hyphenate.chat.EMClient;

import cn.ucai.superwechat.ui.AddContactActivity;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucai.superwechat.ui.SettingActivity;
import cn.ucai.superwechat.ui.UserProfileActivity;
import cn.ucar.superwechat.R;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void finishRight(Activity context){
        context.finish();
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void startActivity(Activity context, Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    private static void startActivityForResult(Activity context, Intent intent, int requestCode) {  //将带返回值得到跳转的方法提取出来
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotologin(Activity context){
        startActivity(context, LoginActivity.class);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoregister(Activity context){
        startActivity(context, RegisterActivity.class);
        context.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoUserProfile(Activity context){
        Intent intent=new Intent(context,UserProfileActivity.class);
        intent.putExtra("setting",true);
        intent.putExtra("username",EMClient.getInstance().getCurrentUser());
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoSettingActivity(Activity context){
        startActivity(context, SettingActivity.class);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void logingotoMainacitvity(Activity context){
        Intent intent = new Intent(context,
               MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

    }
    public static void gotoAddContact(Activity context){
        startActivity(context, AddContactActivity.class);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }



}
