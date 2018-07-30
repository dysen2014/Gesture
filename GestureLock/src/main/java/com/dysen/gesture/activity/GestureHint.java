package com.dysen.gesture.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dysen.gesture.R;
import com.dysen.gesture.base.BaseActivity;
import com.dysen.gesture.ui.UIHelper;

/**
 * xiezuofei
 * 2016-10-08 11:10
 * 793169940@qq.com
 */
public class GestureHint extends BaseActivity implements View.OnClickListener {
    public static GestureHint activity;
    private Activity context;

    Button bnt_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.user_gesture_hint);

        activity = this;
        context = this;

        initView();
    }

    private void initView() {

        bnt_commit = (Button) findViewById(R.id.bnt_commit);

        tvTitle.setText(R.string.user_ssmmsz);

        bnt_commit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bnt_commit) {
            UIHelper.showGesture(context, GestureAty.TYPE_SET);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
