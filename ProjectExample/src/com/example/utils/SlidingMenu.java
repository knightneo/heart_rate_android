package com.example.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.activity.DeviceScanActivity;
import com.example.activity.HistoryActivity;
import com.example.activity.HomeActivity;
import com.example.activity.SettingActivity;
import com.example.projectexample.R;
import com.example.projectexample.R.styleable;

public class SlidingMenu extends HorizontalScrollView {

	public static final int HOME_PAGE = 0;
	public static final int DEVICE_PAGE = 1;
	public static final int SETTING_PAGE = 2;
	public static final int HISTORY_PAGE = 3;

	// 屏幕的宽度
	private int mScreenWidth;
	// dp
	private int mMenuRightPadding;
	// 菜单的宽度
	private int mMenuWidth;
	private int mHalfMenuWidth;

	private boolean isOpen;
	private boolean once;

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				// 默认50
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));
				break;
			}
		}
		a.recycle();
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!once) {
			// 获取存放菜单的LinearLayout
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			// 获取item图标
			ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);
			// 获取item文字
			ViewGroup content = (ViewGroup) wrapper.getChildAt(1);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			menu.getLayoutParams().width = mMenuWidth;
			content.getLayoutParams().width = mScreenWidth;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();

			if (isOpen) {// 打开时
				if (scrollX > (mMenuWidth * 0.2)) {// 隐藏
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = false;
				} else {// 完全显示
					this.smoothScrollTo(0, 0);
					isOpen = true;
				}
			} else {// 隐藏时
				if (scrollX < (mMenuWidth * 0.8)) {// 完全显示
					this.smoothScrollTo(0, 0);
					isOpen = true;
				}else{// 隐藏
					this.smoothScrollTo(mMenuWidth, 0);
					isOpen = false;
				}
			}
			/*
			if (scrollX > mHalfMenuWidth) {// 隐藏
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {// 完全显示
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			*/
			return true;
		}
		return super.onTouchEvent(ev);
	}

	// 打开菜单
	public void openMenu() {
		if (isOpen) {
			return;
		}
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	// 关闭菜单
	public void closeMenu() {
		if (isOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}

	// 重置菜单
	public void initMenu() {
		this.setScrollX(mMenuWidth);
		isOpen = false;
	}

	// 切换菜单状态
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	// 跳转到home
	public void home(Context context, int page) {
		switch (page) {
		case HOME_PAGE:
			closeMenu();
			break;
		case DEVICE_PAGE:
			HomeActivity.actionStart(context);
			// toggle();
			((Activity) context).finish();
		case SETTING_PAGE:
			HomeActivity.actionStart(context);
			((Activity) context).finish();
			break;
		case HISTORY_PAGE:
			HomeActivity.actionStart(context);
			((Activity) context).finish();
			break;
		}
	}

	// 跳转到device_scan
	public void scan(Context context, int page) {
		switch (page) {
		case HOME_PAGE:
			DeviceScanActivity.actionStart(context);
			((Activity) context).finish();
			break;
		case DEVICE_PAGE:
			toggle();
		case SETTING_PAGE:
			DeviceScanActivity.actionStart(context);
			((Activity) context).finish();
			break;
		case HISTORY_PAGE:
			DeviceScanActivity.actionStart(context);
			((Activity) context).finish();
			break;
		}
	}

	// 跳转到setting
	public void setting(Context context, int page) {
		switch (page) {
		case HOME_PAGE:
			SettingActivity.actionStart(context);
			((Activity) context).finish();
			break;
		case DEVICE_PAGE:
			SettingActivity.actionStart(context);
			((Activity) context).finish();
		case SETTING_PAGE:
			closeMenu();
			break;
		case HISTORY_PAGE:
			SettingActivity.actionStart(context);
			((Activity) context).finish();
			break;
		}
	}

	// 跳转到history
	public void history(Context context, int page) {
		switch (page) {
		case HOME_PAGE:
			HistoryActivity.actionStart(context);
			// toggle();
			((Activity) context).finish();
			break;
		case DEVICE_PAGE:
			HistoryActivity.actionStart(context);
			// toggle();
			((Activity) context).finish();
		case SETTING_PAGE:
			HistoryActivity.actionStart(context);
			((Activity) context).finish();
			break;
		case HISTORY_PAGE:
			closeMenu();
			break;
		}
	}

	// 退出
	public void exit() {
		ActivityManager.finishAll();
	}

}
