package com.baiyjk.shopping;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import com.baiyjk.shopping.MyRelativeLayout.OnScrollListener;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.TextureView;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CategoryActivity extends Activity implements OnGestureListener,
	OnTouchListener, OnItemClickListener {
	private ListView listView1, listView2;
	private TextView titlebar;
	private RelativeLayout relativeLayout;
	private MyRelativeLayout listViewContainer1, listViewContainer2;
	private Drawable divider;
	
	private static final String TAG = "category";
    
    private static final int SPEED = 30;  
    private boolean bIsScrolling = false;  
    private int iLimited = 0;  
    private int mScroll = 0;  
    private View mClickedView = null;
    private boolean isListViewTouch = false;
    private GestureDetector gestureDetector;
    private boolean hasMaskedLevel1 = false;
	private DisplayMetrics dm;
	private GestureDetector mGestureDetector;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		
		initView();
		
	}
	
	
	private void initView(){
//		titlebar = (TextView)findViewById(R.id.titlebar);
//		titlebar.setText("全部分类");
		
		//绑定XML中的ListView，作为Item的容器 
		relativeLayout =(RelativeLayout)findViewById(R.id.category_layout);
		listView1 = (ListView)findViewById(R.id.category_level1_listview);
		listView2 = (ListView)findViewById(R.id.category_level2_listview);
		listViewContainer1 = (MyRelativeLayout)findViewById(R.id.myRelativeLayout1);
		listViewContainer2 = (MyRelativeLayout)findViewById(R.id.myRelativeLayout2);
		divider = listView1.getDivider();
		dm = getResources().getDisplayMetrics();
		
		mGestureDetector = new GestureDetector(this);  
        mGestureDetector.setIsLongpressEnabled(false);
        context = this;
        
		//生成动态数组，并且转载数据  
	    String[] data = {"聪慧孕婴", "营养佳品", "中西药品", "隐形眼镜", "医疗器械"};
	    ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>(); 
	    for(int i=0; i < data.length; i++)  
	    {  
	        HashMap<String, Object> map = new HashMap<String, Object>();
	        map.put("ItemId", 1);
	        map.put("ItemImage", R.drawable.baby);
	        map.put("ItemTitle", data[i]);
	        map.put("ItemText", data[i]);  
	        mylist.add(map);
	    }
	    
	    SimpleAdapter adapter = new SimpleAdapter(this, mylist, 
	    		R.layout.category_listitem, 
	    		new String[] {"ItemId", "ItemImage", "ItemTitle", "ItemText"}, 
	    		new int[] {R.id.item_id, R.id.item_image, R.id.item_name, R.id.item_desc});
	    
	    listView1.setAdapter(adapter);
	    
	  //生成动态数组，并且转载数据  
	    String[] data2 = {"宝宝营养", "孕产妇护理", "宝宝护理", "口腔护理", "孕产妇营养", "科学备孕", "产后调理", "孕产妇美肤","item3","item4","item5","item6","item7","item8"};
	    ArrayList<HashMap<String, Object>> mylist2 = new ArrayList<HashMap<String, Object>>(); 
	    for(int i=0; i < data2.length; i++)  
	    {  
	        HashMap<String, Object> map2 = new HashMap<String, Object>();
	        map2.put("ItemId", 1);
	        map2.put("ItemName", data2[i]); 
	        mylist2.add(map2);  
	    }
	    
	    SimpleAdapter adapter2 = new SimpleAdapter(this, mylist2, 
	    		R.layout.category_level2_listitem, 
	    		new String[] {"ItemId", "ItemName"}, 
	    		new int[] {R.id.level2_item_id, R.id.level2_item_name});
	    
	    listView2.setAdapter(adapter2);
	    
	    //一级分类点击事件
	    
	    listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.e("click item", "click item" + arg2);
				//此处初始化二级分类列表
				if (!hasMaskedLevel1) {
					displayLayout(0);
				}
				
			}
		});
	    
	    //第一级分类监听
	    listViewContainer1.setOnScrollListener(new OnScrollListener(){  
            @Override  
            public void doOnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
                onScroll(distanceX);
            }
              
            @Override  
            public void doOnRelease(){
                onRelease(); 
            }  
        });
	    
	    //第二级分类监听
	    listViewContainer2.setOnScrollListener(new OnScrollListener(){  
            @Override  
            public void doOnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
                onScroll(distanceX);
            }
              
            @Override  
            
            public void doOnRelease(){
                onRelease(); 
            }  
        });
	    //第二级分类点击启动商品列表页
	    listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(context, ProductsListActivity.class);
				startActivity(intent);
				
			}
		});
	}
	/**
	 * 渲染分类Layout.数据从数据库读取，包括分类ID，分类名，分类说明，分类图片，母分类ID。
	 * @param categoryId
	 */
	private void renderView(int categoryId, int listviewId){
		//TODO 根据分类&ListViewId渲染Layout
		
	}
	
    private void displayLayout(float distanceX){  
        DisplayMetrics dm = getResources().getDisplayMetrics();   
          
        // 滑出二级分类页面  
        RelativeLayout.LayoutParams lp = (LayoutParams) listViewContainer2.getLayoutParams();  
        lp.leftMargin = -dm.widthPixels + 200; 
        listViewContainer2.setLayoutParams(lp);  
        Log.d(TAG, "listview 2.margin = " + lp.leftMargin);
    }
    
    
	private void rollLayout(float margin){  
        RelativeLayout.LayoutParams lp1 = (LayoutParams) listViewContainer1.getLayoutParams();
        RelativeLayout.LayoutParams lp2 = (LayoutParams) listViewContainer2.getLayoutParams();
        
               
        int minMargin = -dm.widthPixels + 200; 
        
        Log.d(TAG, "the width:" + lp1.width + ";" + lp2.width);//这里怎么都等于2？？？
        Log.d(TAG, "the min leftMargin:" + minMargin); //=-520
        
        if ((lp2.leftMargin + margin) < minMargin) {
        		lp2.leftMargin = minMargin;
        		
        		hasMaskedLevel1 = true;
        		listView1.setDivider(null);
		}else{
			lp2.leftMargin += margin;
		}
//        lp.rightMargin -= margin;
        listViewContainer2.setLayoutParams(lp2);
    }
	
	private void onScroll(float distanceX){  // 向左滑动distanceX为正
        bIsScrolling = true;      
        
        RelativeLayout.LayoutParams lp2 = (LayoutParams) listViewContainer2.getLayoutParams();
        
        Log.d(TAG, "lp2.leftMargin = " + lp2.leftMargin);
        Log.d(TAG, "将要移动距离 = " + distanceX);
        
        if(distanceX > 0){ // 向左移动  
            if(hasMaskedLevel1){ // 第一级分类已经遮盖到最多
            		Log.d(TAG, "最左极限啦，啥也不做，return;");
                return;
            }
            
        }else if(distanceX < 0){  // 向右移动  
        		if(!hasMaskedLevel1){ // 第一级分类完全展示，没有任何遮盖
        			Log.d(TAG, "最右看不见啦，啥也不做，return;");
                return;
            }
        }  
  
        if(distanceX != 0){
            rollLayout(-distanceX);  
        }  
    }  
      
    private void onRelease(){         
        RelativeLayout.LayoutParams lp2 = (LayoutParams) listViewContainer2.getLayoutParams();        
        int minMargin = -dm.widthPixels + 200;//第一级分类完全遮盖住时第二级分类的leftMargin值 = -520
        
        if(lp2.leftMargin >= minMargin/2){ // 第二级分类显示不到一半，缩回去。  
            new SlideMenu().execute(Math.abs(lp2.leftMargin), SPEED);
            Log.d(TAG, "第二级分类缩回去，lp2.leftMargin=" + lp2.leftMargin);
            listView1.setDivider(divider);
            hasMaskedLevel1 = false;
        }else{  //否则展开
            new SlideMenu().execute(lp2.leftMargin - minMargin, -SPEED);
            Log.d(TAG, "第二级分类完全展开，lp2.leftMargin=" + lp2.leftMargin);
            listView1.setDivider(null);
            hasMaskedLevel1 = true;
        }  
    } 
	
	/**
	 * 
	 * @author lyy
	 * http://blog.csdn.net/qingye_love/article/details/8776650
	 *  左、右菜单滑出 
	 * 
	 *  params[0]: 滑动距离 
	 *  params[1]: 滑动速度,带方向 
	 */ 
	public class SlideMenu extends AsyncTask<Integer, Integer, Void>{
		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			if(params.length != 2){  
                Log.e(TAG, "error, params must have 2!");  
            }  
  
            int times = params[0] / Math.abs(params[1]);  
            if(params[0] % Math.abs(params[1]) != 0){  
                times ++;  
            }
              
            for(int i = 0; i < times; i++){  
                this.publishProgress(params[0], params[1], i+1);  
            }  
			return null;
		}
		
		@Override  
        protected void onProgressUpdate(Integer... values) {  
            if(values.length != 3){  
                Log.e(TAG, "error, values must have 3!");  
            }  
  
            int distance = Math.abs(values[1]) * values[2];  
            int delta = values[0] - distance;  
  
            int leftMargin = 0;  
            if(values[1] < 0){ // 左移  
                leftMargin = (delta > 0 ? values[1] : -(Math.abs(values[1]) - Math.abs(delta)));  
            }else{
                leftMargin = (delta > 0 ? values[1] : (Math.abs(values[1]) - Math.abs(delta)));  
            }  
              
            rollLayout(leftMargin);  
        }
		
	}

	
	///////////////////// ListView.onItemClick ///////////////////////
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	//////////////////////////////onTouch ///////////////////////////////  
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		mClickedView = v;  
        
        if(MotionEvent.ACTION_UP == event.getAction() && bIsScrolling){  
            onRelease();
        }  
          
        return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		bIsScrolling = true; 
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		onScroll(distanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
