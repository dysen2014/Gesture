package com.dysen.gesture

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dysen.gesture.activity.GestureAty
import com.dysen.gesture.activity.GestureHint
import com.dysen.gesture.base.AppContext
import com.dysen.gesture.base.BaseActivity
import com.dysen.gesture.ui.UIHelper
import com.dysen.gesture.utils.SharedPreUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseSetContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        val user_lock = SharedPreUtils.getInstance(mContext).get(AppContext.KEY_USER_LOCK, "")

        if ("1".equals(user_lock))
        //手势密码
            UIHelper.showGesture(this, GestureAty.TYPE_UNLOCK)

        tv_gesture_config.setOnClickListener {
            if ("".equals(user_lock)) {
                UIHelper.showGestureHint(this)
            } else {
                UIHelper.showGestureSet(this)
            }
        }
    }

    fun transAty(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }
}
