package com.simtice.cnbeta.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simtice.cnbeta.R;
import com.simtice.cnbeta.bean.HMComment;

/*
 *热门评论Adapter 
 */
public class HMAdapter extends BaseAdapter {
	private List<HMComment> lists;
	private LayoutInflater inflater;

	public HMAdapter(Context context, List<HMComment> lists) {
		inflater = LayoutInflater.from(context);
		this.lists = lists;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.hmcom_item, null);
			holder = new ViewHolder();
			holder.comment = (TextView) convertView.findViewById(R.id.tv_hm_item_comment);
			holder.name = (TextView) convertView.findViewById(R.id.tv_hm_item_name);
			holder.title = (TextView) convertView.findViewById(R.id.tv_hm_item_title);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.comment.setText(lists.get(position).getComment());
		String name = lists.get(position).getName();
		if(name.equals("")){
			name = "匿名人士";
		}
		holder.name.setText(name);
		holder.title.setText(lists.get(position).getTitle());
		return convertView;
	}

	private class ViewHolder {
		private TextView title;
		private TextView comment;
		private TextView name;
	}
}
