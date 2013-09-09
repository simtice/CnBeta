package com.simtice.cnbeta.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simtice.cnbeta.R;
import com.simtice.cnbeta.bean.CommentLists;

public class CommentAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CommentLists> list;
	public CommentAdapter(Context context, List<CommentLists> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.commentlist_item, null);
			holder = new ViewHolder();
			holder.against = (TextView) convertView.findViewById(R.id.tv_comment_against);
			holder.name = (TextView) convertView.findViewById(R.id.tv_comment_name);
			holder.comment = (TextView) convertView.findViewById(R.id.tv_comment_comment);
			holder.date = (TextView) convertView.findViewById(R.id.tv_comment_date);
			holder.support = (TextView) convertView.findViewById(R.id.tv_comment_support);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		String name = list.get(position).getName();
		if(name.equals(""));
			name = "匿名人士";
		holder.name.setText(name);
		holder.against.setText(" " + list.get(position).getAgainst());
		holder.support.setText(" " + list.get(position).getSupport());
		holder.comment.setText(list.get(position).getComment());
		holder.date.setText(list.get(position).getDate());

		return convertView;

	}

	private static class ViewHolder {
		private TextView name;
		private TextView date;
		private TextView comment;
		private TextView support;
		private TextView against;
	}

}
