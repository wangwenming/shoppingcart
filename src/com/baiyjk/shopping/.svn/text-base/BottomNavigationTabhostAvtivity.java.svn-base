package com.baiyjk.shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class BottomNavigationTabhostAvtivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_navigation_tabhost);
		
		TabHost tabHost = (TabHost)findViewById(R.id.myTabHost);
		tabHost.setup();
		
		tabHost.addTab(tabHost.newTabSpec("tag1").setIndicator("home").setContent(R.id.view2));
		tabHost.addTab(tabHost.newTabSpec("tag2").setIndicator("cate").setContent(R.id.view2));
		tabHost.addTab(tabHost.newTabSpec("tag3").setIndicator("account").setContent(R.id.view2));
		tabHost.addTab(tabHost.newTabSpec("tag4").setIndicator("cart").setContent(R.id.view2));
		
		
	}

}
