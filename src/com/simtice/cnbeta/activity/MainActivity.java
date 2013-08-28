package com.simtice.cnbeta.activity;

import com.simtice.cnbeta.R;
import com.simtice.cnbeta.R.id;
import com.simtice.cnbeta.R.layout;
import com.simtice.cnbeta.R.menu;
import com.simtice.cnbeta.util.HttpUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView tv = (TextView) findViewById(R.id.text);
		String source = "<p><strong>感谢<a href=\"http://www.apppoo.com/news/2013/08/27/25100.html\" target=\"_blank\">安卓应用谱</a>的投递</strong><br/></p><p>继多个版本的概念图，和多个浑水摸;鱼、以假乱真的“真机图”之后，我们总算可以见到三星Galaxy Note 3的部分真身了。这些图片来自Sonny Dickson，这货曾经曝光过iPhone 5C和iPhone 5S的相关照片，其爆料有一定可信度。</p>";
		String source1 = "<p>现在科技业界的曝光度越来越高,现在大家不仅得知下周三星将在柏林举行的IFA中发布Note 3, 此外三星明年的主打旗舰S5也有更多规格流出:<strong>16MP摄像头外加光学稳定成像技术(OIS)</strong></p>";

		Spanned text = Html.fromHtml(source1);
		tv.setText(text);
		tv.setText("什么情况");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void test(View v) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpUtil util = new HttpUtil();
				util.urlConn();
			}

		}).start();
		;
	}

}
