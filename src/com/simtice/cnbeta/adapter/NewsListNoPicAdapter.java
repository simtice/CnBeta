package com.simtice.cnbeta.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simtice.cnbeta.R;
import com.simtice.cnbeta.bean.NewsList;
import com.simtice.cnbeta.util.CommonUtil;

/**
 * 无图模式
 * 
 * @author simtice
 * 
 */
public class NewsListNoPicAdapter extends BaseAdapter {

	private List<NewsList> list = null;
	private LayoutInflater inflater;

	public NewsListNoPicAdapter(List<NewsList> list, Context context) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.news_list_item_nopic, null);
			holder = new ViewHolder();
			holder.summary = (TextView) arg1.findViewById(R.id.tv_newsitem_summary);
			holder.title = (TextView) arg1.findViewById(R.id.tv_newsitem_title);
			holder.time = (TextView) arg1.findViewById(R.id.tv_newsitem_time);
			holder.comm = (TextView) arg1.findViewById(R.id.tv_newsitem_com);
			arg1.setTag(holder);

		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		String title = list.get(arg0).getTitle();
		int comNum = list.get(arg0).getCmtnum();
		String str = "";
		if (comNum == 0) {
			str = "暂无评论";
		} else {
			str = String.valueOf(comNum);
		}

		holder.title.setText(title);
		holder.title.getPaint().setFakeBoldText(true);
		holder.time.setText("  " + list.get(arg0).getPubtime());
		holder.comm.setText("  " + str);
		holder.summary.setText(CommonUtil.parseHtml(list.get(arg0).getSummary()));

		return arg1;
	}

	private static class ViewHolder {
		TextView title;
		TextView summary;
		TextView time;
		TextView comm;
	}

}
