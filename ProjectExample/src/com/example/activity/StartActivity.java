package com.example.activity;

import com.example.projectexample.R;
import com.example.projectexample.R.layout;
import com.example.utils.BaseActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 应用启动界面
 * 
 * @author 吴彧
 * 
 */

public class StartActivity extends BaseActivity {

	boolean isFirstIn = false;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final long SPLASH_DELAY_MILLIS = 1000;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		final View view = View.inflate(this, R.layout.activity_start, null);
		setContentView(view);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				init();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}
		});

	}

	private void init() {

		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		if (!isFirstIn) {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}
	}

	private void goHome() {
		HomeActivity.actionStart(StartActivity.this);
		finish();
	}

	private void goGuide() {
		GuideActivity.actionStart(StartActivity.this);
		finish();
	}

}
