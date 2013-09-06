package com.simtice.cnbeta.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
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
		
//		String text = "<p><strong>感谢<a href=\"http://www.zoopda.com/\" target=\"_blank\">煮机网</a>的投递</strong><br/></p><p>之前，煮机在许多文章里提到了这样一个看法：小米3很可能在今年与魅族MX3的比拼中，落下阵来，原因有三，第一，小米扩展产品线，既增加了红米，又有小米电视，没有多少底蕴的小米，在研发、设计、技术上的积累不够，分散精力开发新产品的话，米3很可能无法延续米2的辉煌，即无法做到机器各方面都比较均衡，成为一部成熟的高性价比机器。</p>";
		
//		 Spanned text = Html.fromHtml(list.get(arg0).getSummary());
//		 Spanned text1 = Html.fromHtml(text);
		 String text = CommonUtil.parseHtml(list.get(arg0).getSummary());
		// String text = CommonUtil.parseHtml(list.get(arg0).getSummary());
//		String summary = list.get(arg0).getSummary();
//		System.out.println(summary);
		
//		String temp = summary.substring(0, 50);
		holder.summary.setText(text);

		return arg1;
	}

	private static class ViewHolder {
		TextView title;
		TextView summary;
		TextView time;
		TextView comm;
	}

}
