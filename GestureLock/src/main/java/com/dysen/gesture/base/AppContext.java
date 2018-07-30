package com.dysen.gesture.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.widget.Toast;

import com.dysen.gesture.ui.UIHelper;
import com.dysen.gesture.R;
import com.dysen.gesture.activity.GestureAty;
import com.dysen.gesture.utils.AppUtils;
import com.dysen.gesture.utils.DateUtils;
import com.dysen.gesture.utils.LogUtils;
import com.dysen.gesture.utils.SharedPreUtils;

/**
 * @package com.dysen.gesturelock.activity
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/27 - 下午2:07
 * @info
 */
public class AppContext extends Application {
    public static String T_TIME_OUT = "t_time_out";
    private String TAG = getClass().getSimpleName();
    private static AppContext app;
    private static Boolean deBugMode = true;

    public static AppContext getInstance() {
        return app;
    }

    public static Boolean getDeBugMode() {
        return deBugMode;
    }

    public int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityStopped");
                count--;
                if (count == 0) {
                    LogUtils.v("viclee", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityStarted");
                if (count == 0) {
                    LogUtils.v("viclee", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle" + (System.currentTimeMillis() - mLastActionTime > mTotalTime));
                    if (System.currentTimeMillis() - mLastActionTime > mTotalTime) {
                        if (!AppUtils.newInstance(app).getRunningActivitySimpleName().equals("SplashActivity"))
                            UIHelper.showGesture(app, GestureAty.TYPE_UNLOCK);
                    }
                }
                count++;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtils.v("viclee", activity + "\tonActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtils.v("viclee", activity + "\tonActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.v("viclee", activity + "\tonActivityCreated");
            }
        });

    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public void copy(String content, Context context) {
        try {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
            UIHelper.ToastMessage(context, "内容已复制到剪切板");
        } catch (Exception e) {

        }
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public String paste(Context context) {
        try {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            String str = "";
            if (cmb.getText() != null) {
                str = cmb.getText().toString().trim();
            }
            if (str.equals("")) {
                UIHelper.ToastMessage(context, "内容为空");
            }
            return str;
        } catch (Exception e) {
            return "";
        }

    }

    /*----------------------------------定时器 开始--------------------------------------------*/

    public static long mLastActionTime = -1; // 上一次操作时间
    public static long mNowActionTime = -1; // 当前操作时间
    //    protected static long mTotalTime = 5 * 60 * 1000;//默认5分钟自动退出
    protected static long mTotalTime = 10 * 1000;//默认5分钟自动退出

    public static String ANSWER = "answer";
    public static String ISUNLOCK = "isunlock";
    public static final String KEY_USER_LOCK = "user_lock";

    public void show() {
        mLastActionTime = mNowActionTime;
        // 初始化上次操作时间为登录成功的时间
        if (mLastActionTime == -1)
            mLastActionTime = mNowActionTime = System.currentTimeMillis();
        else
            mNowActionTime = System.currentTimeMillis();

        LogUtils.e(TAG, DateUtils.getDateString(mLastActionTime) + "==user touch==" + DateUtils.getDateString(mNowActionTime));
        long time = SharedPreUtils.getInstance(app).get(AppContext.T_TIME_OUT, 0l);
        if (time > 0)
            mTotalTime = time;
        LogUtils.e(TAG, time+"========time=mTotalTime=========="+mTotalTime);
        if (mNowActionTime - mLastActionTime > mTotalTime)
            UIHelper.showGesture(app, GestureAty.TYPE_UNLOCK);
    }

    public long getmTotalTime() {
        return mTotalTime;
    }

    public void setmTotalTime(long totalTime) {
        mTotalTime = totalTime * 1000;
    }

    /*------------------------------------定时器 结束🔚------------------------------------------*/

    /*------------------------------------通知 常量------------------------------------------*/
    public static final String KEY_MSGID = "msg_id";
    public static final String KEY_WHICH_PUSH_SDK = "rom_type";
    public static final String KEY_TITLE = "n_title";
    public static final String KEY_CONTENT = "n_content";
    public static final String KEY_EXTRAS = "n_extras";
    public static final String MAIN_TYPE = "main_type";
    public static final String MAIN_TYPE_NOTIFICATION = "101";
    public static final String MAIN_TYPE_MESSAGE = "102";
    /*------------------------------------通知 结束🔚------------------------------------------*/
}