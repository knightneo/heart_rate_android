package com.example.utils;


import com.example.projectexample.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SlidingActivity extends BaseActivity{
	
	public static final String SPNAME = "setting";
	public static final int MODE = Context.MODE_WORLD_READABLE
			+ Context.MODE_WORLD_WRITEABLE;
	
	
	protected SlidingMenu mMenu;
	private int page;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}

	protected void setPage(int page){
		this.page = page;
	}
	
	public void toggleMenu(View view){
		mMenu.toggle();
	}
	
	public void home(View view){
		mMenu.home(this,page);
	}
	
	public void scan(View view){
		mMenu.scan(this, page);
	}
	
	public void setting(View view){
		mMenu.setting(this, page);
	}
	
	public void history(View view){
		mMenu.history(this, page);
	}
	public void exit(View view){
		mMenu.exit();
	}
}
