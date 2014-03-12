package com.simtice.cnbeta.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.bean.NewsList;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.PreferencesUtil;

public class NewsListAdapter extends ImageLoadAdapter {

	private List<NewsList> list = null;
	private LayoutInflater inflater;
	private Context context;

	public NewsListAdapter(List<NewsList> list, Context context, PullToRefreshListView listview) {
		super(context, listview);
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.getLoader().setImageScaleType(ScaleType.FIT_CENTER);
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub

		if (!list.get(position).getTheme().equals("x")) {
			return 1;
		} else
			return 2;
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
		final int type = getItemViewType(arg0);
		if (arg1 == null) {
			switch (type) {
			case 1:
				arg1 = inflater.inflate(R.layout.news_list_item, null);
				break;
			case 2:
				arg1 = inflater.inflate(R.layout.news_list_item1, null);
				break;
			}
			
			holder = new ViewHolder();
			holder.title = (TextView) arg1.findViewById(R.id.tv_newsitem_title);
			holder.time = (TextView) arg1.findViewById(R.id.tv_newsitem_time);
			holder.comm = (TextView) arg1.findViewById(R.id.tv_newsitem_com);
			holder.image = (ImageView) arg1.findViewById(R.id.iv_newsitem_image);
			holder.summary = (TextView) arg1.findViewById(R.id.tv_newsitem_summary);
			arg1.setTag(holder);

		} else {
			holder = (ViewHolder) arg1.getTag();
		}

		String title = list.get(arg0).getTitle();
		String time = "  " + list.get(arg0).getPubtime();
		int comNum = list.get(arg0).getCmtnum();
		String str = "";
		if (comNum == 0) {
			str = "暂无评论";
		} else {
			str = String.valueOf(comNum);
		}

		holder.title.setText(title);
		holder.title.getPaint().setFakeBoldText(true);
		holder.time.setText(time);
		holder.comm.setText("  " + str);
		holder.summary.setText(CommonUtil.parseHtml(list.get(arg0).getSummary()));

		switch (type) {
		case 1:
			if (PreferencesUtil.isNoShowPicIn2G(this.context)) {// 2G/3G下不现实图片
				if (CommonUtil.isWifiConnect(this.context)) {// WIFI下显示图片
					holder.summary.setVisibility(View.GONE);
					holder.image.setVisibility(View.VISIBLE);
					this.getLoader().setImageScaleType(ScaleType.CENTER_CROP);
					this.loadImage(list.get(arg0).getTheme(), holder.image, this.getDefaultBitmap());

				} else {
					holder.summary.setVisibility(View.VISIBLE);
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.summary.setVisibility(View.GONE);
				holder.image.setVisibility(View.VISIBLE);
				this.getLoader().setImageScaleType(ScaleType.CENTER_CROP);
				this.loadImage(list.get(arg0).getTheme(), holder.image, this.getDefaultBitmap());
			}

			break;
		case 2:
			if (PreferencesUtil.isNoShowPicIn2G(this.context)) {// 2G/3G下不现实图片
				if (CommonUtil.isWifiConnect(this.context)) {// WIFI下显示图片
				// holder2.summary.setVisibility(View.GONE);
					holder.image.setVisibility(View.VISIBLE);
					this.getLoader().setImageScaleType(ScaleType.FIT_CENTER);
					this.loadImage(list.get(arg0).getTopicLogo(), holder.image, this.getDefaultBitmap());

				} else {
					// holder2.summary.setVisibility(View.VISIBLE);
					holder.image.setVisibility(View.GONE);
				}
			} else {
				// holder2.summary.setVisibility(View.GONE);
				holder.image.setVisibility(View.VISIBLE);
				this.getLoader().setImageScaleType(ScaleType.FIT_CENTER);
				this.loadImage(list.get(arg0).getTopicLogo(), holder.image, this.getDefaultBitmap());
			}

			break;
		}

		return arg1;
	}

	private static class ViewHolder {
		TextView title;
		TextView summary;
		TextView time;
		TextView comm;
		ImageView image;
	}

}
