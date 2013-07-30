package com.baiyjk.shopping.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.baiyjk.shopping.ViewPagerHomeAdsItem;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 首页广告导航图的适配器
 * @author lyy
 *
 */
public class MyViewPagerAdapter extends PagerAdapter{

    private Context mContext;  
    private ArrayList<ImageView> images; 
    private HashMap<Integer, ViewPagerHomeAdsItem> mHashMap;  
      
    public MyViewPagerAdapter(Context context, ArrayList<ImageView> images) {  
        this.mContext = context;  
        this.images = images;
        mHashMap = new HashMap<Integer, ViewPagerHomeAdsItem>();
    }   
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.images.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object; 
	}
	@Override  
    public void destroyItem(ViewGroup container, int position, Object object) {  
		container.removeView(images.get(position)); 
    } 
	
    @Override 
    public Object instantiateItem(ViewGroup container, int position){
    		ImageView view = images.get(position);
    		view.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("ads click", "ads click+");
					
				}});
    		container.addView(view);
    		
    		return images.get(position);
    }

}
