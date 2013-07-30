package com.baiyjk.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPagerHomeAdsItem extends FrameLayout{
	private ImageView hImageView;
	private String hText;
	private TextView hTextView;
	
	public ViewPagerHomeAdsItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	private void initView(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.viewpager_homeadsitem, null);
		hImageView = (ImageView)view.findViewById(R.id.homeads_imageview);
		hTextView = (TextView)view.findViewById(R.id.homeads_textview);
		addView(view);
	}
}
