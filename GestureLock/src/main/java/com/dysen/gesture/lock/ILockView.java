package com.dysen.gesture.lock;

import android.view.View;

/**
 * @package com.dysen.gesturelock.activity
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/9 - 上午9:47
 * @info    接口，自定义手势解锁样式需实现
 */
public interface ILockView {

    //手势状态
    int NO_FINGER = 0;
    int FINGER_TOUCH = 1;
    int FINGER_UP_MATCHED = 2;
    int FINGER_UP_UN_MATCHED = 3;

    /**
     * 获取View
     *
     * @return
     */
    View getView();

    /**
     * 手指没触摸之前，初始状态
     */
    void onNoFinger();

    /**
     * 手指触摸，按下状态
     */
    void onFingerTouch();

    /**
     * 手指抬起，手势密码匹配状态
     */
    void onFingerUpMatched();

    /**
     * 手指抬起，手势密码不匹配状态
     */
    void onFingerUpUnmatched();
}