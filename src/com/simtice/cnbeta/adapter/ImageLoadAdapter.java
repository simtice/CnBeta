package com.simtice.cnbeta.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.simtice.cnbeta.R;
import com.simtice.cnbeta.util.CommonUtil;
import com.simtice.cnbeta.util.imagecache.ImageAsynLoader;
import com.simtice.cnbeta.util.imagecache.ImageUtil;

/**
 * 带有图片加载器的适配器
 * 
 * @author xxf
 * 
 */
public class ImageLoadAdapter extends BaseAdapter {
	private ImageAsynLoader loader;
	private Bitmap defaultBitmap;
	private ListView listView;
	private PullToRefreshListView pf;

	public Bitmap getDefaultBitmap() {
		return defaultBitmap;
	}

	public ImageAsynLoader getLoader() {
		return loader;
	}

	// 当前是否正在滑动
	private boolean isFling;

	public void setFling(boolean isFling) {
		this.isFling = isFling;
	}

	public ImageLoadAdapter(Context context, ListView listview) {
		loader = new ImageAsynLoader(context);
		loader.isSaveThumb(true);
		loader.createFileCacheDir();
		loader.setDecodeSize(CommonUtil.getScreenWidth(context), 200);
		defaultBitmap = ImageUtil.readBitmap(context, R.drawable.aa_photo_empty);
		this.listView = listview;
		this.listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_FLING) {
					setFling(true);
				} else {
					setFling(false);
					notifyDataSetChanged();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
	}
	public ImageLoadAdapter(Context context, PullToRefreshListView listview) {
		loader = new ImageAsynLoader(context);
		loader.isSaveThumb(true);
		loader.createFileCacheDir();
		loader.setDecodeSize(CommonUtil.getScreenWidth(context), 200);
		defaultBitmap = ImageUtil.readBitmap(context, R.drawable.aa_photo_empty);
		this.pf = listview;
		this.pf.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_FLING) {
					setFling(true);
				} else {
					setFling(false);
					notifyDataSetChanged();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
		return null;
	}

	public void loadImage(String url, ImageView imageView, Bitmap defaultBitmap) {
		if (isFling) {// 当前正在滑动，只从缓存读取图片
			loader.displayImage(url, imageView, true, defaultBitmap);
		} else {
			loader.displayImage(url, imageView, false, defaultBitmap);
		}
	}

}
