package com.baiyjk.shopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPagerProductDetailImageItem extends FrameLayout{

	private ImageView hImageView1;
	private ImageView hImageView2;
	private TextView hTextView;

	public ViewPagerProductDetailImageItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initView(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.viewpager_product_detail_image, null);
		hImageView1 = (ImageView)view.findViewById(R.id.product_detail_imageview_1);
		hImageView2 = (ImageView)view.findViewById(R.id.product_detail_imageview_2);
		hTextView = (TextView)view.findViewById(R.id.product_detail_image_textview);
		addView(view);
	}
}
