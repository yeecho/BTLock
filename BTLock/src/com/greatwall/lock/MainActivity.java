package com.greatwall.lock;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo4.R;
import com.greatwall.lock.adapter.MainGridAdapter;
import com.greatwall.lock.db.DbHelper;
import com.greatwall.lock.utils.Utils;

//@SuppressLint("ServiceCast")
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {

	private static int SENSOR_SHAKE = 1;
	private Context context;
	private GridView gv;
	private ImageButton ib;
	private MainGridAdapter gridAdapter;
	private ArrayList<DeviceMode> list = new ArrayList<DeviceMode>();
	private DbHelper dbHelper;
	private int key_position;
	private AlertDialog dialog;
	private MyHandler mHandler = new MyHandler();
	protected boolean vFlag = true;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
		initData();
		Log.d("yuanye", "^^^^:"+(51 ^ 49));
	}

	private void initData() {
		context = this;
		dbHelper = new DbHelper(this);
		list = getKeysFromDB();
		gridAdapter = new MainGridAdapter(this, list);
		gv.setAdapter(gridAdapter);
	}

	private ArrayList<DeviceMode> getKeysFromDB() {
		ArrayList<DeviceMode> list = new ArrayList<DeviceMode>();
		Cursor result = dbHelper.query(DbHelper.QueryAll);
		result.moveToFirst();
		while (!result.isAfterLast()) {
			DeviceMode device = new DeviceMode();
			device.setName(dbHelper.getName(result));
			device.setPassword(dbHelper.getPassword(result));
			device.setType(dbHelper.getType(result));
			device.setDeviceName(dbHelper.getDeviceName(result));
			device.setDeviceAddress(dbHelper.getDeviceAddress(result));
			list.add(device);
			result.moveToNext();
		}
		result.close();

		return list;
	}

	private void initListener() {
		gv.setOnItemClickListener(this);
		gv.setOnItemLongClickListener(this);
		ib.setOnClickListener(this);
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		gv = (GridView) findViewById(R.id.main_gv);
		ib = (ImageButton) findViewById(R.id.main_ib);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_ib:
			try {
				Intent intent = new Intent(context, DeviceScanActivity.class);
				startActivityForResult(intent, 100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.dlg_mk_info:
			dialog.dismiss();
			showInfo();
			break;
		case R.id.dlg_mk_modify:
			modify();
			break;
		case R.id.dlg_mk_delete:
			delete();
			break;

		default:
			break;
		}
	}

	private void showInfo() {
		AlertDialog.Builder bd = new AlertDialog.Builder(context);
		bd.setTitle(R.string.key_information);
		String name = list.get(key_position).getName();
		String password = list.get(key_position).getPassword();
		String type = list.get(key_position).getType();
		if (type.equals("01")) {
//			type = getResources().getStringArray(R.array.type)[0];
			type = getResources().getString(R.string.office);
		}else if (type.equals("02")) {
//			type = getResources().getStringArray(R.array.type)[1];
			type = getResources().getString(R.string.meeting_room);
		}else if (type.equals("03")) {
//			type = getResources().getStringArray(R.array.type)[2];
			type = getResources().getString(R.string.dormitory_building);
		}
		String deviceName = list.get(key_position).getDeviceName();
		String deviceAddress = list.get(key_position).getDeviceAddress();
		String info = getResources().getString(R.string.name)+"："+name+"\n"
				+getResources().getString(R.string.type)+"： "+type+"\n"
				+getResources().getString(R.string.device_name)+"： "+deviceName+"\n"
				+getResources().getString(R.string.mac_add)+"： "+deviceAddress+"\n";
		bd.setMessage(info);
		bd.setPositiveButton(R.string.confirm, null);
		bd.show();
	}

	private void delete() {
		String name = list.get(key_position).getName();
		dbHelper.delete(name);
		dialog.dismiss();
		list = getKeysFromDB();
		gridAdapter = new MainGridAdapter(context, list);
		gv.setAdapter(gridAdapter);
	}

	private void modify() {
		dialog.dismiss();
		Intent intent = new Intent(context, ModifyKey.class);
		intent.putExtra("name", list.get(key_position).getName());
		intent.putExtra("password", list.get(key_position).getPassword());
		intent.putExtra("type", list.get(key_position).getType());
		startActivityForResult(intent, 101);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 100) {
			String name = data.getExtras().getString("name");
			String password = data.getExtras().getString("password");
			String type = data.getExtras().getString("type");
			String deviceName = data.getExtras().getString("device_name");
			String deviceAddress = data.getExtras().getString("device_address");
			dbHelper.insert(name, password, type, deviceName, deviceAddress);
			DeviceMode device = new DeviceMode(name, password, type,
					deviceName, deviceAddress);
			gridAdapter.addDevice(device);
			Toast.makeText(context, getResources().getString(R.string.toast_key) 
					+ name + getResources().getString(R.string.toast_added), Toast.LENGTH_SHORT)
					.show();
		} else if (resultCode == 101) {
			list = getKeysFromDB();
			gridAdapter = new MainGridAdapter(context, list);
			gv.setAdapter(gridAdapter);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, DeviceControlActivity.class);
		intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME,
				list.get(position).getDeviceName());
		intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS,
				list.get(position).getDeviceAddress());
		intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_PASSWORD,
				list.get(position).getPassword());
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		key_position = position;
		AlertDialog.Builder bd = new AlertDialog.Builder(context);
		dialog = bd.create();
		View v = LayoutInflater.from(context).inflate(
				R.layout.dialog_modifykey, null);
		TextView tvInfo = (TextView) v.findViewById(R.id.dlg_mk_info);
		TextView tvModify = (TextView) v.findViewById(R.id.dlg_mk_modify);
		TextView tvDelete = (TextView) v.findViewById(R.id.dlg_mk_delete);
		tvInfo.setOnClickListener(this);
		tvModify.setOnClickListener(this);
		tvDelete.setOnClickListener(this);
		dialog.setView(v);
		dialog.show();
		return true;
	}

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				break;

			default:
				break;
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_btn, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Message msg = new Message();
			msg.what = SENSOR_SHAKE;
			mHandler.sendMessage(msg);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
