package com.example.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class SlidingDelete extends HorizontalScrollView {
	
	//item到边框的宽度
	private int mEdgePadding;
	
	// item的宽度
	private int mItemWidth;
	// dp
	private int mDeleteLeftPadding;
	// 删除按钮的宽度
	private int mDeleteWidth;
	private int mHalfDeleteWidth;

	private boolean isOpen;
	private boolean once;

	public SlidingDelete(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
