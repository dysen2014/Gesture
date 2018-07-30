package com.dysen.gesture.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dysen.gesture.activity.GestureAty;
import com.dysen.gesture.activity.GestureHint;
import com.dysen.gesture.activity.GestureSet;
import com.dysen.gesture.base.AppContext;
import com.dysen.gesture.utils.SharedPreUtils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {

    public final static String TAG = "UIHelper";

    public final static int RESULT_OK = 0x00;
    public final static int REQUEST_CODE = 0x01;

    public static void ToastMessage(Context cont, String msg) {
        if (cont == null || msg == null) {
            return;
        }
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, int msg) {
        if (cont == null || msg <= 0) {
            return;
        }
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, String msg, int time) {
        if (cont == null || msg == null) {
            return;
        }
        Toast.makeText(cont, msg, time).show();
    }

    public static void showNext(Activity context, Class cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void showNext(Activity context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }


    public static void showNext(Activity context, Class cls, String type) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        context.startActivity(intent);
    }

    public static void showGestureSet(Activity context){
        Intent intent = new Intent(context, GestureSet.class);
        context.startActivity(intent);
    }
    public static void showGestureHint(Activity context){
        Intent intent = new Intent(context, GestureHint.class);
        context.startActivity(intent);
    }
    public static void showGesture(Context context, String type){
        if (type.equals(GestureAty.TYPE_UNLOCK)) {
            String user_lock = SharedPreUtils.getInstance(context).get(AppContext.KEY_USER_LOCK, "");
            if ("1".equals(user_lock)) {
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                UIHelper.showGestureAty(context, bundle);
            }else {
                UIHelper.ToastMessage(context, "当前手势密码未开启，请开启后再来！");
                return;
            }
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            UIHelper.showGestureAty(context, bundle);
        }
    }
    public static void showGestureAty(Context context, Bundle mBundle){
        Intent intent = new Intent(context, GestureAty.class);
        intent.putExtras(mBundle);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
