package com.greatwall.lock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "demo4.db";
	private static final int DATABASE_VERSION = 1;
	public static final String QueryAll = "SELECT name, password, type, device_name, device_address FROM keystore";

	private String create_table_keystore = "CREATE TABLE IF NOT EXISTS keystore "
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "name TEXT, password TEXT, type TEXT, device_name TEXT, device_address TEXT);";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(create_table_keystore);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void insert(String name, String password, String type, String deviceName,
			String deviceAddress) {
		ContentValues cv = new ContentValues();

		cv.put("name", name);
		cv.put("password", password);
		cv.put("type", type);
		cv.put("device_name", deviceName);
		cv.put("device_address", deviceAddress);

		getWritableDatabase().insert("keystore", "name", cv);
	}
	
	public Cursor query(String sql){
		return getReadableDatabase().rawQuery(sql, null);
	}
	
	public int delete(String name){
		return getWritableDatabase().delete("keystore", "name=?", new String[]{name});
	}
	
	public String getName(Cursor c){
		return c.getString(0);
	}
	
	public String getPassword(Cursor c){
		return c.getString(1);
	} 

	public String getType(Cursor c){
		return c.getString(2);
	}

	public String getDeviceName(Cursor c) {
		// TODO Auto-generated method stub
		return c.getString(3);
	}
	
	public String getDeviceAddress(Cursor c) {
		// TODO Auto-generated method stub
		return c.getString(4);
	}

	public void modify(String defName, String name, String password, String type) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("password", password);
		values.put("type", type);
		getWritableDatabase().update("keystore", values, "name=?", new String[]{defName});
	}
}
