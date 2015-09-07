package com.example.utils;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("BaseActivity", getClass().getSimpleName());
		ActivityManager.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.removeActivity(this);
	}

}
