package com.greatwall.lock;

import com.example.demo4.R;
import com.greatwall.lock.db.DbHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ModifyKey extends Activity implements OnClickListener,OnItemSelectedListener{

	private Context context;
	private EditText etName;
	private EditText etPassword;
	private Spinner spType;
	private String[] types = new String[]{"01","02","03"}; 
	private String type;
	private Button btnCancel;
	private Button btnCommit;
	private DbHelper mDbHelper;
	private String defName;
	private String defType;
	private String defPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}

	private void initData() {
		context = this;
		mDbHelper = new DbHelper(this);
		defName = getIntent().getStringExtra("name");
		defPassword = getIntent().getStringExtra("password");
		defType = getIntent().getStringExtra("type");
		int type = typeConvert(defType);
		etName.setHint(defName);
		etPassword.setHint(defPassword);
		spType.setSelection(type);
	}

	private int typeConvert(String defType) {
		if (defType.equals("01")) {
			return 0;
		}else if (defType.equals("02")) {
			return 1;
		}else if (defType.equals("03")) {
			return 2;
		}
		return 0;
	}

	private void initListener() {
		spType.setOnItemSelectedListener(this);
		btnCancel.setOnClickListener(this);
		btnCommit.setOnClickListener(this);
	}

	private void initView() {
		setContentView(R.layout.modify_key);
		etName = (EditText) findViewById(R.id.modify_key_name);
		etPassword = (EditText) findViewById(R.id.modify_key_password);
		spType = (Spinner) findViewById(R.id.modify_key_type);
		btnCancel = (Button) findViewById(R.id.modify_key_cancel_btn);
		btnCommit = (Button) findViewById(R.id.modify_key_commit_btn);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		type = types[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		type = types[0];
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify_key_cancel_btn:
			finish();
			break;
		case R.id.modify_key_commit_btn:
			String name = etName.getText().toString();
			if (!checkName(name)) {
				Toast.makeText(context, R.string.toast_name_modify_failed, 0).show();
				return;
			}
			if (name.equals("")) {
				name = defName;
			}
			String password = etPassword.getText().toString();
			if (password.equals("")) {
				password = defPassword;
			}
//			Intent data = new Intent();
//			data.putExtra("name", etName.getText().toString());
//			data.putExtra("password", etPassword.getText().toString());
//			data.putExtra("type", type);
			mDbHelper.modify(defName, name, password, type);
			setResult(101);
			finish();
			break;

		default:
			break;
		}
	}
	
	protected boolean checkName(String name) {
		// TODO Auto-generated method stub
    	String sql = "select * from keystore where name=?";
		Cursor cursor = mDbHelper.getReadableDatabase().rawQuery(sql, new String[]{name});
		if (cursor.moveToNext()) {
			return false;
		}
		return true;
	}
	
}
