package com.baiyjk.shopping;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.baiyjk.shopping.adapter.MyViewPagerAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class HomeActivity extends Activity {

	private ViewPager mViewPager;   
    private MyViewPagerAdapter mViewPagerAdapter;   
    private int[] imageIds;
    private ArrayList<ImageView> images;
    private ArrayList<View> dots;
    private ScheduledExecutorService scheduledExecutorService;
    private int currentItem; //
    private int oldPosition = 0;//
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		setupViews(); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onStart(){
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 2, TimeUnit.SECONDS);
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	private void setupViews(){
		this.imageIds = new int[]{
				R.drawable.viewpager_1,
				R.drawable.viewpager_2,
				R.drawable.viewpager_3
		};
		this.images = new ArrayList<ImageView>();
		this.dots = new ArrayList<View>();
		for(int i = 0; i < this.imageIds.length; i++){
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(this.imageIds[i]);
			this.images.add(imageView);
		}
		this.dots.add(findViewById(R.id.dot_0));
		this.dots.add(findViewById(R.id.dot_1));
		this.dots.add(findViewById(R.id.dot_2));
		
        mViewPager = (ViewPager)findViewById(R.id.homeAdViewPager);  
        mViewPagerAdapter = new MyViewPagerAdapter(this, images);  
        mViewPager.setAdapter(mViewPagerAdapter);
        
        mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				dots.get(oldPosition).setBackgroundResource(R.drawable.dot_unselected);
				dots.get(position).setBackgroundResource(R.drawable.dot_selected);
				
				oldPosition = position;
				currentItem = position;
			}
        	
        });
    } 
	
	private class ViewPagerTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			currentItem = (currentItem + 1) % imageIds.length;
			handler.obtainMessage().sendToTarget();
		}
		
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			mViewPager.setCurrentItem(currentItem);
		}
	};
}
