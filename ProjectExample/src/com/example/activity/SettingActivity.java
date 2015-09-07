package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.projectexample.R;
import com.example.projectexample.R.id;
import com.example.projectexample.R.layout;
import com.example.utils.InfoAdapter;
import com.example.utils.InfoItem;
import com.example.utils.SlidingActivity;
import com.example.utils.SlidingMenu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends SlidingActivity {

	public static final String SPNAME = "setting";
	public static final int MODE = Context.MODE_WORLD_READABLE
			+ Context.MODE_WORLD_WRITEABLE;
	
	private TextView name, gender, age, height, weight;
	private LinearLayout buttonSetting;

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, SettingActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mMenu = (SlidingMenu) findViewById(R.id.setting_page);
		setPage(SlidingMenu.SETTING_PAGE);
		//buttonSetting = (LinearLayout) findViewById(R.id.button_setting);
		//buttonSetting.setBackgroundResource(R.drawable.setting_background);
		
		name = (TextView) findViewById(R.id.text_view_setting_name);
		gender = (TextView) findViewById(R.id.text_view_setting_gender);
		age = (TextView) findViewById(R.id.text_view_setting_age);
		height = (TextView) findViewById(R.id.text_view_setting_height);
		weight = (TextView) findViewById(R.id.text_view_setting_weight);
		
		loadSP();
	}

	public void setName(View view) {
		final EditText et = new EditText(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("姓名").setIcon(R.drawable.ic_launcher)
				.setView(et).setNegativeButton("取消", null)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 保存姓名
						String str = et.getText().toString();
						test(str);
						saveSP("name",str);
						loadSP();
					}
				}).show();
	}

	public void setGender(View view) {
		final String[] items = { "女", "男" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// get item
				test(items[which]);
				saveSP("gender",items[which]);
				loadSP();
			}
		});
		builder.setCancelable(true);
		builder.create().show();

	}

	public void setAge(View view) {
		final String[] items = new String[100];
		for (int i = 1; i <= 100; i++) {
			items[i - 1] = i + "";
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// get item
				test(items[which]);
				saveSP("age",items[which]);
				loadSP();
			}
		});
		builder.setCancelable(true);
		builder.create().show();
	}

	public void setHeight(View view) {
		final String[] items = new String[100];
		for (int i = 111; i <= 210; i++) {
			items[i - 111] = i + "";
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// get item
				test(items[which]);
				saveSP("height",items[which]);
				loadSP();
			}
		});
		builder.setTitle("单位:厘米").setCancelable(true);
		builder.create().show();
	}

	public void setWeight(View view) {
		final String[] items = new String[120];
		for (int i = 21; i <= 140; i++) {
			items[i - 21] = i + "";
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// get item
				test(items[which]);
				saveSP("weight",items[which]);
				loadSP();
			}
		});
		builder.setTitle("单位:千克").setCancelable(true);
		builder.create().show();
	}
	
	private void saveSP(String key, String value){
		SharedPreferences sp = getSharedPreferences(SPNAME,MODE);
		//用SP编辑器来读写内容
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	private void loadSP(){
		SharedPreferences sp = getSharedPreferences(SPNAME,MODE);
		String name = sp.getString("name", "姓名");
		String gender = sp.getString("gender", "性别");
		String age = sp.getString("age", "年龄");
		String height = sp.getString("height", "身高");
		String weight = sp.getString("weight", "体重");
		
		this.name.setText(name);
		this.gender.setText(gender);
		this.age.setText(age);
		this.height.setText(height);
		this.weight.setText(weight);
	}
	
	private void test(String input){
		Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
	}
}
