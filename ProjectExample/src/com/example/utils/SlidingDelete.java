package com.example.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class SlidingDelete extends HorizontalScrollView {
	
	//item���߿�Ŀ��
	private int mEdgePadding;
	
	// item�Ŀ��
	private int mItemWidth;
	// dp
	private int mDeleteLeftPadding;
	// ɾ����ť�Ŀ��
	private int mDeleteWidth;
	private int mHalfDeleteWidth;

	private boolean isOpen;
	private boolean once;

	public SlidingDelete(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}
