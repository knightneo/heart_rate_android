package com.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.projectexample.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InfoAdapter extends ArrayAdapter<InfoItem> {

	public static final int FEMALE = 0;
	public static final int MALE = 1;

	private int resourceId;

	public InfoAdapter(Context context, int resource, List<InfoItem> info) {
		super(context, resource, info);
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		InfoItem infoItem = getItem(position);// 获取当前项的InfoItem实例
		View view;
		ViewHolder viewHolder;

		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.infoLabel = (TextView) view
					.findViewById(R.id.list_info_label);
			viewHolder.infoText = (TextView) view
					.findViewById(R.id.list_info_text);
			view.setTag(viewHolder);// 把ViewHolder保存在View中
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();// 重新获取ViewHolder
		}

		viewHolder.infoLabel.setText(infoItem.getInfoLabel());
		if (infoItem.getInfoLabel().equals("性别")) {
			if (infoItem.getInfoNumber() == FEMALE) {
				viewHolder.infoText.setText("男");
			} else {
				viewHolder.infoText.setText("女");
			}
		} else {
			viewHolder.infoText.setText(infoItem.getInfoNumber() + "");
		}
		return view;
	}

	class ViewHolder {
		TextView infoLabel;
		TextView infoText;
	}

}
