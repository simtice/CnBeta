package com.simtice.cnbeta.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simtice.cnbeta.R;

public class MainDrawerAdapter extends BaseAdapter {
	private List<String> listTag;
	private List<String> objects;
	private Context context;
	private int selectedPosition;

	public MainDrawerAdapter(Context context, List<String> objects, List<String> tags) {
		this.listTag = tags;
		this.objects = objects;
		this.context = context;
	}

	@Override
	public boolean isEnabled(int position) {
		// 如果是TAG，则该项不可选
		if (listTag.contains(getItem(position))) {
			return false;
		}
		return super.isEnabled(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (listTag.contains(this.objects.get(position))) {
			view = LayoutInflater.from(context).inflate(R.layout.main_list_item_tag, null);
		} else {
			view = LayoutInflater.from(context).inflate(R.layout.main_list_item, null);
		}

		TextView textView = (TextView) view.findViewById(R.id.tv_main_item_tag);
		textView.setText(this.objects.get(position));

		if (selectedPosition == position) {
			textView.setTextColor(context.getResources().getColor(R.color.abs__background_holo_light));
			// view.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.));
			view.setBackgroundColor(context.getResources().getColor(R.color.holo_blue));
		} else {
			textView.setTextColor(context.getResources().getColor(R.color.gray_text));
			view.setBackgroundColor(Color.TRANSPARENT);
		}
		return view;
	}

	public void setNoBackground() {

	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
