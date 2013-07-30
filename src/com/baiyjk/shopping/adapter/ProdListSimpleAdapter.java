package com.baiyjk.shopping.adapter;

import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baiyjk.shopping.ProductDetailActivity;
import com.baiyjk.shopping.R;
import com.baiyjk.shopping.R.id;
import com.baiyjk.shopping.utils.ImageLoader;

public class ProdListSimpleAdapter extends SimpleAdapter{

	private LayoutInflater layoutinflater;
	private int resource;
	private View myView;
	private Context context;
	private List<Map<String, Object>> data;
	private final String TAG = "CustomSimpleAdapter";
	private String productID;
	private String productUrl;

	public ProdListSimpleAdapter(Context context,
			List<Map<String, Object>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
//		Log.d(TAG, "" + resource);
		this.resource = resource;
		this.context = context;
		this.data = data;
		this.layoutinflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
//		Log.d(TAG, "" + this.resource);
		myView = layoutinflater.inflate(this.resource, null);
		ImageView imageView = (ImageView) myView.findViewById(R.id.list_product_image);
		TextView nameView = (TextView) myView.findViewById(R.id.list_product_name);
		TextView urlView = (TextView) myView.findViewById(R.id.list_product_url);
		TextView infoView = (TextView) myView.findViewById(R.id.list_product_info);
		TextView priceView = (TextView) myView.findViewById(R.id.list_product_price);
		
		ImageLoader imageLoader = new ImageLoader(context);  		
		imageLoader.DisplayImage((String)data.get(position).get("image"), imageView); 
//		this.productID = data.get(position).get("id").toString();
		this.productUrl = data.get(position).get("url").toString();
		nameView.setText(data.get(position).get("name").toString());
		urlView.setText(this.productUrl);
		infoView.setText(data.get(position).get("info").toString());
		priceView.setText(data.get(position).get("price").toString());
		
		myView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//这里需要用到商品详情页的URL
				Log.d(TAG, "点击商品：" + ((TextView)(v.findViewById(R.id.list_product_url))).getText().toString());
//				Intent intent = new Intent(context, ProductDetailActivity.class);
				Intent intent = new Intent();
				intent.putExtra("url", ((TextView)(v.findViewById(R.id.list_product_url))).getText().toString());
				intent.setClass(context, ProductDetailActivity.class);
				context.startActivity(intent);
			}
		});
		return myView;
		
	}

}
