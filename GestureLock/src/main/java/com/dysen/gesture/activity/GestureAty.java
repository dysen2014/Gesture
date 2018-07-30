package com.dysen.gesture.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dysen.gesture.R;
import com.dysen.gesture.base.AppContext;
import com.dysen.gesture.base.BaseActivity;
import com.dysen.gesture.lock.GestureLockDisplayView;
import com.dysen.gesture.lock.GestureLockLayout;
import com.dysen.gesture.lock.ILockView;
import com.dysen.gesture.lock.LockViewFactory;
import com.dysen.gesture.lock.QQLockView;
import com.dysen.gesture.ui.UIHelper;
import com.dysen.gesture.utils.SharedPreUtils;

import java.util.List;

/**
 * @package com.dysen.common
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/16 - 下午4:44
 * @info
 */
public class GestureAty extends BaseActivity {

    public static final String TYPE ="type";
    public static final String TYPE_SET = "1";
    public static final String TYPE_RESET = "2";
    public static final String TYPE_UNLOCK = "3";

    GestureLockDisplayView mLockDisplayView;

    TextView tvTipHint;

    TextView tvSettingHint;

    GestureLockLayout mGestureLockLayout;

    TextView tvResetGesturePwd;
    public String type;
    private Handler mHandler = new Handler();
//    private int BACK_TIME = 3 * 60 * 1000;//3分钟
    private int BACK_TIME = 30 * 1000;//3分钟
    Long intoTime;
    private Animation shakeAnimation;

    static GestureAty aty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_gesture);
        initView();
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString(GestureAty.TYPE);
        if (type.equals(TYPE_SET) || type.equals(TYPE_RESET)) {
            tvSettingHint.setText(R.string.user_qszssmm);
            tvTipHint.setVisibility(View.VISIBLE);
            mLockDisplayView.setVisibility(View.VISIBLE);
            if (type.equals(TYPE_RESET)) {
                tvSettingHint.setText(R.string.user_czssmm);
            }
            initSetGesture();
            initSetEvents();
        } else if (type.equals(TYPE_UNLOCK)) {
            tvSettingHint.setText(R.string.user_qhdssmm);
            tvTipHint.setVisibility(View.GONE);
//            mLockDisplayView.setVisibility(View.GONE);
            initVerifyGesture();
            initVerifyEvents();
        }

    }

    /**
     * 解锁监听
     */
    private void initVerifyEvents() {
        mGestureLockLayout.setOnLockVerifyListener(new GestureLockLayout.OnLockVerifyListener() {
            public Runnable runnable;
            public Handler handler;

            @Override
            public void onGestureSelected(int id) {
                //每选中一个点时调用
            }

            @SuppressLint("StringFormatMatches")
            @Override
            public void onGestureFinished(boolean isMatched, List<Integer> answerList) {
                //将答案设置给提示view
                mLockDisplayView.setAnswer(answerList);
                //绘制手势解锁完成时调用

                if (isMatched) {

                    //密码匹配
                    SharedPreUtils.getInstance(mContext).put(AppContext.ISUNLOCK, true);
                    tvResetGesturePwd.setVisibility(View.INVISIBLE);
                    finish();

                } else {
                    //不匹配
//                    tvSettingHint.setText(getString(R.string.tip_more_chances, mGestureLockLayout.getTryTimes()));
                    tvSettingHint.setText(getString(R.string.user_ssmmcw));
                    tvSettingHint.startAnimation(shakeAnimation);
                    tvResetGesturePwd.setVisibility(View.VISIBLE);
                    resetGesture();
                }
            }

            @Override
            public void onGestureTryTimesBoundary() {

                //超出最大尝试次数时调用
                mGestureLockLayout.setTouchable(false);

                handler = new Handler();
                runnable = new Runnable() {
                    int index = 0;

                    @SuppressLint("StringFormatMatches")
                    @Override
                    public void run() {
                        long time = System.currentTimeMillis();
                        if (intoTime < time - BACK_TIME) {
                            mGestureLockLayout.setTouchable(true);
                            tvSettingHint.setText(getString(R.string.user_qhdssmm));
                            tvSettingHint.startAnimation(shakeAnimation);
                        } else {
                            //请在" + (BACK_TIME - ++index) + "S后重试
                            tvSettingHint.setText(getString(R.string.please_try_again_after_a_few_seconds, (BACK_TIME / 1000 - ++index)));
//                            tvSettingHint.startAnimation(shakeAnimation);
                            handler.postDelayed(runnable, 1000);
                        }
                    }
                };
                intoTime = System.currentTimeMillis();
                handler.post(runnable);
            }
        });
    }

    /**
     * 设置手势监听
     */
    private void initSetEvents() {
        mGestureLockLayout.setOnLockResetListener(new GestureLockLayout.OnLockResetListener() {
            @SuppressLint({"StringFormatMatches", "StringFormatInvalid"})
            @Override
            public void onConnectCountUnmatched(int connectCount, int minCount) {
                //连接数小于最小连接数时调用

                tvSettingHint.setText(getString(R.string.user_zsljsd, minCount));
                tvSettingHint.startAnimation(shakeAnimation);
                resetGesture();
            }

            @Override
            public void onFirstPasswordFinished(List<Integer> answerList) {
                //第一次绘制手势成功时调用

                tvSettingHint.setText(getString(R.string.user_qzchzmm));
                tvSettingHint.startAnimation(shakeAnimation);
                //将答案设置给提示view
                mLockDisplayView.setAnswer(answerList);

                //重置
                resetGesture();
            }

            @Override
            public void onSetPasswordFinished(boolean isMatched, List<Integer> answerList) {
                //第二次密码绘制成功时调用

                if (isMatched) {

                    //两次答案一致，保存
                    SharedPreUtils.getInstance(mContext).put(AppContext.ANSWER, answerList
                            .toString());
                    SharedPreUtils.getInstance(mContext).put(AppContext.ISUNLOCK, false);
                    SharedPreUtils.getInstance(mContext).put(AppContext.KEY_USER_LOCK, "1");
                    tvSettingHint.setText(getString(R.string.user_ssmmszcg));
                    tvSettingHint.startAnimation(shakeAnimation);

                    UIHelper.ToastMessage(GestureAty.this, getString(R.string.user_ssmmszcg));
                    finish();//密码设置成功，返回
                } else {
                    tvSettingHint.setText(getString(R.string.user_yschzbyz));
                    tvSettingHint.startAnimation(shakeAnimation);
                    resetGesture();
                }
            }
        });
    }

    /**
     * 初始化手势解锁
     */
    private void initVerifyGesture() {

        //设置提示view 选中状态的颜色
        mLockDisplayView.setDotSelectedColor(Color.parseColor("#01A0E5"));
        //设置提示view 非选中状态的颜色
        mLockDisplayView.setDotUnSelectedColor(Color.GRAY);
        //设置手势解锁模式为验证模式
        mGestureLockLayout.setMode(GestureLockLayout.VERIFY_MODE);
        //设置手势解锁每行每列点的个数
        mGestureLockLayout.setDotCount(3);
        //设置手势解锁最大尝试次数 默认 5
        mGestureLockLayout.setTryTimes(5);
        //设置手势解锁正确答案
        mGestureLockLayout.setAnswer(SharedPreUtils.getInstance(mContext).get(AppContext.ANSWER,
                ""));
        //默认解锁样式为手Q手势解锁样式
        mGestureLockLayout.setLockView(new LockViewFactory() {
            @Override
            public ILockView newLockView() {
                return new QQLockView(GestureAty.this);
            }
        });
    }

    /**
     * 初始化手势密码设置
     */
    private void initSetGesture() {
        //设置提示view 每行每列点的个数
        mLockDisplayView.setDotCount(3);
        //设置提示view 选中状态的颜色
        mLockDisplayView.setDotSelectedColor(Color.parseColor("#01A0E5"));
        //设置提示view 非选中状态的颜色
        mLockDisplayView.setDotUnSelectedColor(Color.GRAY);
        //设置手势解锁view 每行每列点的个数
        mGestureLockLayout.setDotCount(3);
        //设置手势解锁view 最少连接数
        mGestureLockLayout.setMinCount(4);
        //默认解锁样式为手Q手势解锁样式
        mGestureLockLayout.setLockView(new LockViewFactory() {
            @Override
            public ILockView newLockView() {
                return new QQLockView(GestureAty.this);
            }
        });
        //设置手势解锁view 模式为重置密码模式
        mGestureLockLayout.setMode(GestureLockLayout.RESET_MODE);
    }

    private void initView() {

        mLockDisplayView = (GestureLockDisplayView) findViewById(R.id.display_view);
        tvTipHint = (TextView) findViewById(R.id.tv_tip_hint);
        tvSettingHint = (TextView) findViewById(R.id.tv_setting_hint);
        mGestureLockLayout = (GestureLockLayout) findViewById(R.id.gesture_view);
        tvResetGesturePwd = (TextView) findViewById(R.id.tv_reset_gesture_pwd);

        tvResetGesturePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreUtils.getInstance(mContext).put(AppContext.KEY_USER_LOCK, "0");//重置手势
//                appContext.stopTimer();
                UIHelper.showGestureSet((Activity) mContext);
            }
        });

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
    }

    /**
     * 重置
     */
    private void resetGesture() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGestureLockLayout.resetGesture();
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String user_lock = SharedPreUtils.getInstance(mContext).get(AppContext.KEY_USER_LOCK, "");
        if (type.equals(TYPE_UNLOCK) && user_lock.equals("1"))
            ;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (type.equals(TYPE_UNLOCK)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
