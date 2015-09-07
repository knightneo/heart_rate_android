package com.example.activity;

import com.example.projectexample.R;
import com.example.utils.BaseActivity;
import com.example.utils.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends BaseActivity implements OnPageChangeListener{

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private ImageView[] dots;
	private int currentIndex;
	
	public static void actionStart(Context context){
		Intent intent = new Intent(context, GuideActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViews();
		initDots();	
	}

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		views.add(inflater.inflate(R.layout.guide_pic1, null));
		views.add(inflater.inflate(R.layout.guide_pic2, null));
		views.add(inflater.inflate(R.layout.guide_pic3, null));
		views.add(inflater.inflate(R.layout.guide_pic4_start, null));

		vpAdapter = new ViewPagerAdapter(views, this);		
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(this);
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		dots = new ImageView[views.size()];

		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);
		}
		currentIndex = 0;
		dots[currentIndex].setEnabled(false);
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position) {
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);
		currentIndex = position;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentDot(arg0);
	}

}
