package com.example.activity;

import com.example.projectexample.R;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import com.example.ble.SampleGattAttributes;
import com.example.utils.ActivityManager;
import com.example.utils.BaseActivity;
import com.example.utils.SlidingActivity;
import com.example.utils.SlidingMenu;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends SlidingActivity {
	
	private ListView serviceList;
	private LinearLayout buttonBluetooth;

	public static void actionStart(Context context) {
		Intent intent = new Intent(context, DeviceScanActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// SlidingMenu
		setContentView(R.layout.activity_device_scan);
		mMenu = (SlidingMenu) findViewById(R.id.device_page);
		setPage(SlidingMenu.DEVICE_PAGE);
		serviceList = (ListView) findViewById(R.id.gatt_services_list);
		//buttonBluetooth = (LinearLayout) findViewById(R.id.button_bluetooh);
		//buttonBluetooth.setBackgroundResource(R.drawable.menu_background_press);

	}
	
	public void connect(View view){
		BluetoothActivity.actionStart(this);
	}
}