package com.example.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.ble.BluetoothLeService;
import com.example.projectexample.R;
import com.example.projectexample.R.id;
import com.example.projectexample.R.layout;
import com.example.utils.DBUtil;
import com.example.utils.SlidingActivity;
import com.example.utils.SlidingMenu;
import com.xikang.rr.RRInterval;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends SlidingActivity {

	public static final String SPNAME = "deviceAddress";
	public static final int MODE = Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;

	private final static String TAG = HomeActivity.class.getSimpleName();

	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

	private TextView mConnectionState;
	private TextView mDataField;
	private String mDeviceName;
	private String mDeviceAddress;
	private ExpandableListView mGattServicesList;
	private com.example.ble.BluetoothLeService mBluetoothLeService;
	private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	private boolean mConnected = false;
	private BluetoothGattCharacteristic mNotifyCharacteristic;
	private RRInterval mRRInterval = new RRInterval();
	private TextView textHR;

	private LinearLayout connected;
	private LinearLayout disconnected;

	// chart
	private final int POINT_SET_SIZE = 1000;
	private final String CHART_TITLE = "Hart Rate";

	private GraphicalView chart;
	private FrameLayout layoutChart;
	private Context context;
	private Handler handler;
	private TimerTask task;
	private Timer timer;

	private XYMultipleSeriesRenderer renderer;
	private XYMultipleSeriesDataset dataset;
	private XYSeries series;

	int[] xv = new int[POINT_SET_SIZE];
	int[] yv = new int[POINT_SET_SIZE];

	private LinearLayout buttonHome;

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, HomeActivity.class);
		context.startActivity(intent);
	}

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);

		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	// Handles various events fired by the Service.
	// ACTION_GATT_CONNECTED: connected to a GATT server.
	// ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
	// ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
	// ACTION_DATA_AVAILABLE: received data from the device. This can be a
	// result of read
	// or notification operations.
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

				// test
				// testIntent(intent);

				mConnected = true;
				updateConnectionState(R.string.connected);
				invalidateOptionsMenu();
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				mConnected = false;
				updateConnectionState(R.string.disconnected);
				invalidateOptionsMenu();
				clearUI();
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				// Show all the supported services and characteristics on the
				// user interface.
				// displayGattServices(mBluetoothLeService
				// .getSupportedGattServices());
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

				// chart

				// Toast.makeText(HomeActivity.this,
				// intent.getStringExtra(BluetoothLeService.EXTRA_HR),
				// Toast.LENGTH_SHORT).show();

				update(intent);
				// int[] data =
				// intent.getIntArrayExtra(BluetoothLeService.EXTRA_DATA);
				// updateChart(data);
			}
		}
	};

	// 这一段可以删掉了
	// If a given GATT characteristic is selected, check for supported features.
	// This sample
	// demonstrates 'Read' and 'Notify' features. See
	// http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for
	// the complete
	// list of supported characteristic features.
	private final ExpandableListView.OnChildClickListener servicesListClickListner = new ExpandableListView.OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			if (mGattCharacteristics != null) {
				final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition)
						.get(childPosition);
				final int charaProp = characteristic.getProperties();
				if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
					// If there is an active notification on a characteristic,
					// clear
					// it first so it doesn't update the data field on the user
					// interface.
					if (mNotifyCharacteristic != null) {
						mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
						mNotifyCharacteristic = null;
					}
					mBluetoothLeService.readCharacteristic(characteristic);
				}
				if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
					mNotifyCharacteristic = characteristic;
					mBluetoothLeService.setCharacteristicNotification(characteristic, true);
				}
				return true;
			}
			return false;
		}
	};

	private void clearUI() {
		mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
		mDataField.setText(R.string.no_data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mMenu = (SlidingMenu) findViewById(R.id.home_page);
		setPage(SlidingMenu.HOME_PAGE);

		// buttonHome = (LinearLayout) findViewById(R.id.button_home);
		// buttonHome.setBackgroundResource(R.drawable.menu_background_press);

		// BLE
		final Intent intent = getIntent();
		// testConnection(intent);

		mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
		mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

		connected = (LinearLayout) findViewById(R.id.connected_layout);
		disconnected = (LinearLayout) findViewById(R.id.disconnected_layout);

		// Sets up UI references.
		// ((TextView)
		// findViewById(R.id.device_address)).setText(mDeviceAddress);
		mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
		mGattServicesList.setOnChildClickListener(servicesListClickListner);
		mConnectionState = (TextView) findViewById(R.id.connection_state);
		mDataField = (TextView) findViewById(R.id.data_value);

		textHR = (TextView) findViewById(R.id.text_view_heart_rate);

		// getActionBar().setTitle(mDeviceName);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);

		// 这里是启动Service的操作，要修改
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
		// startService(gattServiceIntent);
		// chart
		setChart();

		if (mDeviceAddress == null) {

			connected.setVisibility(View.GONE);
			disconnected.setVisibility(View.VISIBLE);
			
			//BluetoothActivity.actionStart(this);
			//finish();
			

		} else {
			
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mDeviceAddress != null) {
			registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
			if (mBluetoothLeService != null) {
				final boolean result = mBluetoothLeService.connect(mDeviceAddress);
				Log.d(TAG, "Connect request result=" + result);
			}
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mDeviceAddress != null) {
			unregisterReceiver(mGattUpdateReceiver);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDeviceAddress != null) {
			// 解绑
			unbindService(mServiceConnection);
			mBluetoothLeService = null;
		}
	}

	private void updateConnectionState(final int resourceId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mConnectionState.setText(resourceId);
			}
		});
	}

	private void displayData(String data) {
		if (data != null) {
			mDataField.setText(data);
		}
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	// chart
	protected void setChart() {

		layoutChart = (FrameLayout) findViewById(R.id.layout_chart);

		series = new XYSeries(CHART_TITLE);

		dataset = new XYMultipleSeriesDataset();

		dataset.addSeries(series);

		int color = Color.WHITE;
		PointStyle style = PointStyle.CIRCLE;
		renderer = buildRenderer(color, style, true);

		setChartSetting(renderer, CHART_TITLE, "Time", "Heart Rate", 0, POINT_SET_SIZE, -100, 200, Color.WHITE,
				Color.WHITE);

		context = getApplicationContext();
		chart = ChartFactory.getLineChartView(context, dataset, renderer);

		layoutChart.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		/*
		 * // �����Handlerʵ����������Timerʵ��,��ɶ�ʱ����ͼ��Ĺ��� handler = new
		 * Handler() {
		 * 
		 * @Override public void handleMessage(Message msg) { // ˢ��ͼ��
		 * updateChart(); super.handleMessage(msg); } };
		 * 
		 * // Timer task = new TimerTask() {
		 * 
		 * @Override public void run() { Message message = new Message();
		 * message.what = 1; handler.sendMessage(message); } };
		 * timer.schedule(task, 40, 40);
		 */
	}

	private XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(color);
		r.setPointStyle(style);
		r.setFillPoints(fill);
		r.setLineWidth(10);
		renderer.addSeriesRenderer(r);

		return renderer;
	}

	private void setChartSetting(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle,
			double xMin, double xMax, double yMin, double yMax, int axesColor, int labelColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelColor);
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.GRAY);
		renderer.setBackgroundColor(Color.BLUE);
		renderer.setXLabels(10);
		renderer.setYLabels(5);
		renderer.setXTitle("Time");
		renderer.setYTitle("Heart Rate");
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setPointSize((float) 2);
		renderer.setShowLegend(false);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(false);
	}

	private void updateChart(int y[]) {

		int count = y.length;
		int addX[] = new int[count];
		int addY[] = y;

		dataset.removeSeries(series);

		// heart rate chart
		// situation that the data is in one screen
		int length = series.getItemCount();
		if (length < POINT_SET_SIZE) {
			// situation that the data is in one screen
			for (int i = 0; i < count; i++) {
				addX[i] = length + i + 1;
				series.add(addX[i], addY[i]);
			}

		} else {
			// situation that all the points have to be repainted
			length = POINT_SET_SIZE;
			for (int i = count; i < length; i++) {
				xv[i] = (int) (series.getX(i)) - count;
				yv[i] = (int) (series.getY(i));
			}
			series.clear();
			for (int i = count; i < length; i++) {
				series.add(xv[i], yv[i]);
			}
			for (int i = 0; i < count; i++) {
				addX[i] = length - count + i;
				series.add(addX[i], addY[i]);
			}
		}
		dataset.addSeries(series);

		chart.invalidate();
	}

	private void update(Intent intent) {
		int[] data = intent.getIntArrayExtra(BluetoothLeService.EXTRA_DATA);
		updateChart(data);
		getHR(data);

		/*
		 * for (int i = 0; i < 20; i++) { int[] temp ={data[i]}; }
		 */
	}

	private int[] getData() {
		return yv;
	}

	private float getRate(int[] data) {
		float[] rate = new float[data.length];
		float result = 0;
		for (int i : data) {
			rate[i] = mRRInterval.getRR_Value();
			result += rate[i];
		}
		result /= data.length;
		return result;
	}

	private void getHR(int[] data) {

		for (int i : data) {
			mRRInterval.setEcgValue(i);
			if (mRRInterval.getHrState()) {
				String hr = mRRInterval.getRR_Value() + "";
				textHR.setText("心率: " + hr + " dmp");
			}
		}

	}

	public void save(View view) {
		// 保存当前心电心率
		DBUtil databaseHelper = new DBUtil(this);
		databaseHelper.open();
		int[] data = getData();
		float hr = getRate(data);
		databaseHelper.addHistory(hr, data);
		databaseHelper.close();
		Toast.makeText(HomeActivity.this, "已保存", Toast.LENGTH_SHORT).show();
	}

	public void testConnection(Intent intent) {
		Toast.makeText(this, "Device:" + intent.getStringExtra(HomeActivity.EXTRAS_DEVICE_NAME), Toast.LENGTH_SHORT)
				.show();
		Toast.makeText(this, "Address:" + intent.getStringExtra(HomeActivity.EXTRAS_DEVICE_ADDRESS), Toast.LENGTH_SHORT)
				.show();
	}

	public void testIntent(Intent intent) {
		Toast.makeText(this, "Device:" + intent.getAction(), Toast.LENGTH_SHORT).show();
		Toast.makeText(this, "Address:" + intent.getStringExtra(BluetoothLeService.EXTRA_DATA), Toast.LENGTH_SHORT)
				.show();
	}

	private String getAddress() {
		SharedPreferences sp = getSharedPreferences(SPNAME, MODE);
		String address = sp.getString("address", null);
		return address;
	}
	
	public void connect(View view){
		BluetoothActivity.actionStart(this);
		finish();
	}
}
