package com.baiyjk.shopping.adapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import com.baiyjk.shopping.utils.ImageLoader;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ProductDetaiViewPagerAdapter extends PagerAdapter{

	private Context mContext;
	private ArrayList<String> imagesUrl;
//	private HashMap<Integer, ViewPagerProductDetailImageItem> mHashMap;
	private final String TAG = "Product detail adapter";

	public ProductDetaiViewPagerAdapter(Context context, ArrayList<String> imagesUrl) {  
        this.mContext = context;  
        this.imagesUrl = imagesUrl;
//        mHashMap = new HashMap<Integer, ViewPagerProductDetailImageItem>();
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imagesUrl.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

	@Override  
    public void destroyItem(ViewGroup container, int position, Object object) {  
		container.removeView((View)object); 
    } 
	
    @Override 
    public Object instantiateItem(ViewGroup container, int position){
//    		Log.d(TAG, "instantiate item");
    		ImageView iv = new ImageView(mContext); 
//    		ImageView imageView1 = (ImageView)container.findViewById(R.id.product_detail_imageview_1);
//    		ImageView imageView2 = (ImageView)container.findViewById(R.id.product_detail_imageview_2);
        try {
        		ImageLoader imageLoader = new ImageLoader(mContext);
        		Log.d("Product detail adapter", imagesUrl.get(position));
//			imageLoader.DisplayImage(imagesUrl.get(position), imageView1);
//			imageLoader.DisplayImage(imagesUrl.get(position), imageView2);
        		imageLoader.DisplayImage(imagesUrl.get(position), iv);
        } catch (OutOfMemoryError e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        
        
    		iv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e(TAG, "product detail image click");
					//点击图片查看大图...
				}});
    		
    		((ViewPager)container).addView(iv, 0); 
    		return iv;
    }
	

}
