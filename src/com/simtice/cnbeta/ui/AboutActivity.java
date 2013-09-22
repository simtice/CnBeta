package com.simtice.cnbeta.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.simtice.cnbeta.R;

public class AboutActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Sherlock___Theme_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);
		TextView text = (TextView) this.findViewById(R.id.email_text);
		SpannableString msp = new SpannableString(text.getText());
		msp.setSpan(new URLSpan("mailto:simtice@gmail.com"), 0, text.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		text.setText(msp);  
		text.setMovementMethod(LinkMovementMethod.getInstance()); 
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==android.R.id.home){
			finish();
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
}
