package com.example.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.projectexample.R;
import com.example.utils.DBUtil;
import com.example.utils.History;
import com.example.utils.SlidingActivity;
import com.example.utils.SlidingMenu;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends SlidingActivity {

	private ListView listHistory;
	private Button btnManage;
	private Button btnSelectAll;
	private Button btnSelectRemaining;
	private Button btnDelete;
	private Button btnCancle;
	private TextView tvTitle;
	private ProgressBar progressBar;
	private Handler mHandler;

	private RelativeLayout layoutBlock;
	private LinearLayout buttonsDisplay;
	private LinearLayout buttonsManage;
	private boolean inManageMode;

	private List<History> historyData;
	HistoryDisplayAdapter adapterDisplay;
	HistoryManageAdapter adapterManage;
	DBUtil databaseHelper;
	private List<Boolean> itemSelected;
	private LinearLayout buttonHistory;

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, HistoryActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		mMenu = (SlidingMenu) findViewById(R.id.history_page);
		setPage(SlidingMenu.HISTORY_PAGE);
		//buttonHistory = (LinearLayout) findViewById(R.id.button_history);
		//buttonHistory.setBackgroundResource(R.drawable.menu_background_press);

		// Block Start:控件变量的初始化
		listHistory = (ListView) this.findViewById(R.id.list_history);
		btnManage = (Button) this.findViewById(R.id.btn_manage_history);
		btnSelectAll = (Button) this.findViewById(R.id.btn_select_all);
		btnSelectRemaining = (Button) this
				.findViewById(R.id.btn_select_remaining);
		btnDelete = (Button) this.findViewById(R.id.btn_delete_history);
		btnCancle = (Button) this.findViewById(R.id.btn_cancle);
		tvTitle = (TextView) this.findViewById(R.id.tv_title);
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar1);
		layoutBlock = (RelativeLayout) this.findViewById(R.id.layout_block);

		mHandler = new Handler();

		buttonsDisplay = (LinearLayout) this
				.findViewById(R.id.layout_button_group_display);
		buttonsManage = (LinearLayout) this
				.findViewById(R.id.layout_button_group_manage);
		// Block End

		// 从数据库中读取历史数据 ,HistoryData中的History包含时间和数据(字节数组)
		DBUtil databaseHelper = new DBUtil(this);
		databaseHelper.open();
		historyData = databaseHelper.getHistory();
		databaseHelper.close();

		tvTitle.setText("历史记录");
		inManageMode = false;
		// Adapter的设置以及部分按钮的响应设置
		adapterDisplay = new HistoryDisplayAdapter(historyData,
				getLayoutInflater());
		adapterManage = new HistoryManageAdapter(historyData,
				getLayoutInflater());
		listHistory.setAdapter(adapterDisplay);
		btnManage.setOnClickListener(manageBtnListener);
		btnDelete.setOnClickListener(deleteButtonListener);
		btnCancle.setOnClickListener(cancleButtonListener);
		btnSelectAll.setOnClickListener(selectAllButtonListener);
		btnSelectRemaining.setOnClickListener(selectRemainingButtonListener);

		// ListView响应设置
		listHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (inManageMode) {
					return;
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						HistoryActivity.this);
				builder.setTitle("历史心率");
				builder.setMessage(Float.toString(historyData.get(arg2)
						.getHeartRate()));
				builder.setNegativeButton(R.string.app_name,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				builder.create().show();
			}
		});
	}

	private class HistoryDisplayAdapter extends BaseAdapter {
		private List<History> data;
		private LayoutInflater inflater;
		ViewHolder holder;

		private void refreshData(List<History> data) {
			this.data = data;
			this.notifyDataSetChanged();
		}

		private class ViewHolder {
			TextView tvDate;
		}

		public HistoryDisplayAdapter(List<History> historyData,
				LayoutInflater inflater) {
			this.data = historyData;
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_history_display,
						parent, false);
				holder = new ViewHolder();
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			SimpleDateFormat sd = new SimpleDateFormat("yyyy-M-dd hh:mm");
			String date = sd.format(data.get(position).getTime());
			holder.tvDate.setText(date);
			// holder.tvDate.setOnClickListener(new ListenerWithPosS(position));
			return convertView;
		}

	}

	private class HistoryManageAdapter extends BaseAdapter {
		private List<History> data;
		private LayoutInflater inflater;
		private ViewHolder holder;
		// public List<Boolean> itemSelected;
		private int mCurrentPos;

		private void refreshData(List<History> data) {
			this.data = data;
			this.notifyDataSetChanged();
		}

		private class ViewHolder {
			CheckBox cbSelected;
			// TextView tvDate;
		}

		public HistoryManageAdapter(List<History> data, LayoutInflater inflater) {
			this.data = data;
			this.inflater = inflater;
			if (itemSelected == null) {
				itemSelected = new ArrayList<Boolean>(data.size());
				for (int i = 0; i < data.size(); i++) {
					itemSelected.add(false);
				}
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			mCurrentPos = position;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_history_manage,
						parent, false);
				holder = new ViewHolder();
				holder.cbSelected = (CheckBox) convertView
						.findViewById(R.id.cb_selected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-M-dd hh:mm");
			String date = sd.format(data.get(position).getTime());
			// holder.tvDate.setText(date);
			holder.cbSelected.setText(date);
			holder.cbSelected.setChecked(itemSelected.get(position));
			convertView.setClickable(true);
			convertView.setFocusable(true);
			holder.cbSelected.setOnClickListener(new ListenerWithPos(position));
			return convertView;
		}
	}

	private void changeToManageMode() {
		listHistory.setAdapter(adapterManage);
		tvTitle.setText("记录管理");
		buttonsManage.setVisibility(View.VISIBLE);
		buttonsDisplay.setVisibility(View.INVISIBLE);
		inManageMode = true;

	}

	private void changeToDisplayMode() {
		listHistory.setAdapter(adapterDisplay);
		tvTitle.setText("历史记录");
		buttonsManage.setVisibility(View.INVISIBLE);
		buttonsDisplay.setVisibility(View.VISIBLE);
		inManageMode = false;
	}

	private OnClickListener manageBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			changeToManageMode();
		}
	};
	private OnClickListener cancleButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for (int i = 0; i < itemSelected.size(); i++) {
				itemSelected.set(i, false);
			}
			changeToDisplayMode();
		}
	};
	private OnClickListener deleteButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			progressBar.setVisibility(View.VISIBLE);
			layoutBlock.setVisibility(View.VISIBLE);
			layoutBlock.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return true;
				}

			});
			layoutBlock.setFocusable(true);
			progressBar.bringToFront();
			layoutBlock.bringToFront();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					deleteHistory(itemSelected);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressBar.setVisibility(View.GONE);
							layoutBlock.setVisibility(View.GONE);
						}

					});
				}
			}).start();
		}
	};
	private OnClickListener selectAllButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for (int i = 0; i < itemSelected.size(); i++) {
				itemSelected.set(i, true);
			}
			adapterManage.notifyDataSetChanged();
		}
	};

	private OnClickListener selectRemainingButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for (int i = 0; i < itemSelected.size(); i++) {
				boolean temp = itemSelected.get(i);
				itemSelected.set(i, itemSelected.get(i) ? false : true);
			}
			adapterManage.notifyDataSetChanged();
		}
	};

	private void deleteHistory(List<Boolean> itemSelected) {
		if (databaseHelper == null) {
			databaseHelper = new DBUtil(HistoryActivity.this);
		}
		if (!databaseHelper.isOpen()) {
			databaseHelper.open();
		}
		for (int i = 0; i < itemSelected.size(); i++) {
			if (itemSelected.get(i)) {
				databaseHelper.deleteHistory(historyData.get(i).getTime());
			}
		}
		for (int i = 0; i < itemSelected.size(); i++) {
			if (itemSelected.get(i)) {
				itemSelected.remove(i);
				i--;
			}
		}
		historyData = databaseHelper.getHistory();
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				adapterManage.refreshData(historyData);
				adapterDisplay.refreshData(historyData);
			}

		});
		databaseHelper.close();
	}

	private class ListenerWithPos implements OnClickListener {
		private int position;

		public ListenerWithPos(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (itemSelected.get(position)) {
				itemSelected.set(position, false);
			} else {
				itemSelected.set(position, true);
			}
		}

	}

	private class ListenerWithPosS implements OnClickListener {
		private int position;

		public ListenerWithPosS(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(
					HistoryActivity.this);
			builder.setTitle("历史心率");
			builder.setMessage(Float.toString(historyData.get(position)
					.getHeartRate()));
			builder.setNegativeButton(R.string.app_name,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			builder.create().show();
		}

	}
}