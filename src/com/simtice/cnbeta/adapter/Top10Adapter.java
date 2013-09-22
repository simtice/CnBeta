package com.simtice.cnbeta.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simtice.cnbeta.R;
import com.simtice.cnbeta.bean.Top10;

/*
 *热门评论Adapter 
 */
public class Top10Adapter extends BaseAdapter {
	private List<Top10> lists;
	private LayoutInflater inflater;

	public Top10Adapter(Context context, List<Top10> lists) {
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
			convertView = inflater.inflate(R.layout.top10_item, null);
			holder = new ViewHolder();
			holder.comment = (TextView) convertView.findViewById(R.id.tv_top_com);
			holder.count = (TextView) convertView.findViewById(R.id.tv_top_count);
			holder.title = (TextView) convertView.findViewById(R.id.tv_top10_title);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.comment.setText("评论: "+lists.get(position).getCmtnum());
		holder.count.setText("点击: "+lists.get(position).getCounter());
		holder.title.setText(lists.get(position).getTitle());
		holder.title.getPaint().setFakeBoldText(true);
		return convertView;
	}

	private class ViewHolder {
		private TextView title;
		private TextView comment;
		private TextView count;
	}
}
