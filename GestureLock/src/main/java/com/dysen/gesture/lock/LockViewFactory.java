package com.dysen.gesture.lock;

/**
 * @package com.dysen.gesturelock.activity
 * @email dy.sen@qq.com
 * created by dysen on 2018/7/9 - 上午9:07
 * @info    LockViewFactory 启发自 ThreadPoolExecutor 中的 ThreadFactory
 */
public interface LockViewFactory {

    /**
     * 创建 LockView,必须是 newInstance 不能复用一个对象
     * @return
     */
    ILockView newLockView();
}
