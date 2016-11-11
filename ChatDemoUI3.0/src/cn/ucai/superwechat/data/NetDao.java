package cn.ucai.superwechat.data;


import android.content.Context;


import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.io.File;

import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.utils.I;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MD5;

public class NetDao {

    public static void register(Context context, String name, String nick, String password,
                                OkHttpUtils.OnCompleteListener<Result> listner){
        OkHttpUtils<Result> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,name)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listner);
    }
    public static void unregister(Context context, String name,OkHttpUtils.OnCompleteListener<Result> listner){
        OkHttpUtils<Result> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME,name)
                .targetClass(Result.class)
                .execute(listner);
    }

    public static void login(Context context, String name, String password, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,name)
                .addParam(I.User.PASSWORD,MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(listener);
    }
    public static void updateNick(Context context, String nick, String username, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void updateAvatar(Context context, String name, File file, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,name)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    public static void searchUser(Context context, String name,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,name)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void addFriendUser(Context context, String name,String cusername,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CONTACT)
                .addParam(I.Contact.USER_NAME,name)
                .addParam(I.Contact.CU_NAME,cusername)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void deleteContact(Context context, String name,String cusername,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CONTACT)
                .addParam(I.Contact.USER_NAME,name)
                .addParam(I.Contact.CU_NAME,cusername)
                .targetClass(String.class)
                .execute(listener);}
    public static void updateContactList(Context context,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME, EMClient.getInstance().getCurrentUser())
                .targetClass(String.class)
                .execute(listener);
    }
    public static void createGroup(Context context, EMGroup emGroup,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,emGroup.getGroupId())
                .addParam(I.Group.NAME,emGroup.getGroupName())
                .addParam(I.Group.DESCRIPTION,emGroup.getDescription())
                .addParam(I.Group.OWNER,emGroup.getOwner())
                .addParam(I.Group.IS_PUBLIC,String.valueOf(emGroup.isPublic()))
                .addParam(I.Group.ALLOW_INVITES,String.valueOf(emGroup.isAllowInvites()))
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    public static void createGroup(Context context, EMGroup emGroup,File file,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,emGroup.getGroupId())
                .addParam(I.Group.NAME,emGroup.getGroupName())
                .addParam(I.Group.DESCRIPTION,emGroup.getDescription())
                .addParam(I.Group.OWNER,emGroup.getOwner())
                .addParam(I.Group.IS_PUBLIC,String.valueOf(emGroup.isPublic()))
                .addParam(I.Group.ALLOW_INVITES,String.valueOf(emGroup.isAllowInvites()))
                .targetClass(String.class)
                .addFile2(file)
                .post()
                .execute(listener);
    }
    public static void addGroupMembers(Context context,EMGroup emGroup,OkHttpUtils.OnCompleteListener<String> listener){
        String memberArr="";
        for (String m:emGroup.getMembers()){
            if (!m.equals(SuperWeChatHelper.getInstance().getCurrentUsernName())){
                memberArr += m +",";
             //  L.e("memberArr="+memberArr);
            }
        }
            memberArr=memberArr.substring(0,memberArr.length()-1);
            L.e("memberArr="+memberArr);
            OkHttpUtils<String> utils=new OkHttpUtils<>(context);
            utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBER)
                    .addParam(I.Member.GROUP_HX_ID,emGroup.getGroupId())
                    .addParam(I.Member.USER_NAME,memberArr)
                    .targetClass(String.class)
                    .execute(listener);

    }


}
