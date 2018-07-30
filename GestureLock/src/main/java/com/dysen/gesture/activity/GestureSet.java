package com.dysen.gesture.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dysen.gesture.R;
import com.dysen.gesture.base.AppContext;
import com.dysen.gesture.base.BaseActivity;
import com.dysen.gesture.ui.UIHelper;
import com.dysen.gesture.utils.SharedPreUtils;
import com.dysen.gesture.views.SwitchView;
import com.dysen.nice_spinner.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * xiezuofei
 * 2016-10-08 11:10
 * 793169940@qq.com
 */
public class GestureSet extends BaseActivity implements View.OnClickListener {

    private int[] totalTimes;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static GestureSet activity;
    private Activity context;

    RelativeLayout rl_czssmm;

    LinearLayout ll_czssmm;

    SwitchView view_switch_kqssmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.user_gesture_set);

        context = this;
        activity = this;

        initView();
    }

    private void initView() {

        rl_czssmm = (RelativeLayout) findViewById(R.id.rl_czssmm);
        ll_czssmm = (LinearLayout) findViewById(R.id.ll_czssmm);
        view_switch_kqssmm = (SwitchView) findViewById(R.id.view_switch_kqssmm);
        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        rl_czssmm.setOnClickListener(this);
        String user_lock = SharedPreUtils.getInstance(this).get(AppContext.KEY_USER_LOCK, "");
        ;
        if (user_lock.equals("0") || user_lock.equals("")) {
            if (view_switch_kqssmm.isOpened()) {
                view_switch_kqssmm.toggleSwitch(false);
                ll_czssmm.setVisibility(View.GONE);
            }
        } else {
            if (!view_switch_kqssmm.isOpened()) {
                view_switch_kqssmm.toggleSwitch(true);
                ll_czssmm.setVisibility(View.VISIBLE);
            }
        }
        view_switch_kqssmm.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                SharedPreUtils.getInstance(GestureSet.this).put(AppContext.KEY_USER_LOCK, "1");
                ll_czssmm.setVisibility(View.VISIBLE);
                view_switch_kqssmm.toggleSwitch(true);

            }

            @Override
            public void toggleToOff(View view) {
                SharedPreUtils.getInstance(GestureSet.this).put(AppContext.KEY_USER_LOCK, "0");
                ll_czssmm.setVisibility(View.GONE);
                view_switch_kqssmm.toggleSwitch(false);
            }
        });
        final List<Long> dataset = new LinkedList<>(Arrays.asList(3l, 5l, 15l, 30l));
//        final List<String> dataset = Arrays.asList("One", "Two", "Three", "Four", "Five");
        niceSpinner.attachDataSource(dataset);
        int selectId = SharedPreUtils.getInstance(GestureSet.this).get(SharedPreUtils.SELECT_ID, 0);
        niceSpinner.setSelectedIndex(selectId);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                long time = dataset.get(i);
                Toast.makeText(GestureSet.this, l+"your select item :"+time, Toast
                        .LENGTH_LONG).show();
                SharedPreUtils.getInstance(GestureSet.this).put(AppContext.T_TIME_OUT, time);
                SharedPreUtils.getInstance(GestureSet.this).put(SharedPreUtils.SELECT_ID, i);
                AppContext.getInstance().setmTotalTime(time);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_czssmm) {

            UIHelper.showGesture(context, GestureAty.TYPE_RESET);
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
