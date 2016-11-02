package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucar.superwechat.R;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
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





}
