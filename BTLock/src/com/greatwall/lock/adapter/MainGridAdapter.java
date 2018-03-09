package com.greatwall.lock.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo4.R;
import com.greatwall.lock.DeviceMode;

public class MainGridAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<DeviceMode> list = new ArrayList<DeviceMode>();
	private LayoutInflater inflater;
	
	public MainGridAdapter(Context context, ArrayList<DeviceMode> list){
		this.context = context;
		this.list = list;
	};
	
	public void addDevice(DeviceMode device){
		list.add(device);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView != null ) {
			viewHolder = (ViewHolder) convertView.getTag();
		}else{
			viewHolder = new ViewHolder();
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_main_grid, null);
			viewHolder.keyIcon = (ImageView) convertView.findViewById(R.id.item_mg_iv);
			viewHolder.keyName = (TextView) convertView.findViewById(R.id.item_mg_name);
			convertView.setTag(viewHolder);
		}
		String type = list.get(position).getType();
		if (type.equals("01")) {
			viewHolder.keyIcon.setImageResource(R.drawable.icon_key_blue);
		}else if (type.equals("02")){
			viewHolder.keyIcon.setImageResource(R.drawable.icon_key_red);
		}else if (type.equals("03")){
			viewHolder.keyIcon.setImageResource(R.drawable.icon_key_green);
		}
		viewHolder.keyName.setText(list.get(position).getName());
		return convertView;
	}
	
	private class ViewHolder{
		ImageView keyIcon;
		TextView keyName;
	} 

}
