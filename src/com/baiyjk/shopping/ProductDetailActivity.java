package com.baiyjk.shopping;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baiyjk.shopping.adapter.ProductDetaiViewPagerAdapter;
import com.baiyjk.shopping.http.HttpFactory;
import com.baiyjk.shopping.model.Cart;
import com.baiyjk.shopping.sqlite.MySqLiteHelper;

public class ProductDetailActivity extends Activity{
	private String productUrl;
	private String productDetailJson;
	private final String TAG = "product detail page";
	private TextView titleTextView;
	private TextView nameTextView;
	private TextView descTextView;
	private TextView priceTextView;
	private Button addToCartButton;
	private Button addToWishButton;
	private Button shareButton;
	
	private Context context;
	private LinearLayout dotContainer;
	private View dot;
	private int currentIndex = 0;
	private ArrayList<View> dotsList = new ArrayList<View>();
	private int productId;
	private TextView myCartNumber;
	private int userId = 999;//未登录用户默认ID
	private Cart myCart;
	private JSONObject retJsonObject;//加入购物车response
	private final int MSG_SUCCESS = 1;
	private final int MSG_FAILURE = 0;
	private Button backButton;
	private Button cartButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);
		
		Intent intent = getIntent();
		productUrl = intent.getStringExtra("url");
		String regex = "\\d+";//获取商品Id
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(productUrl);
		while(m.find()){
			productId = Integer.parseInt(m.group());
		}
		Log.d(TAG, "" + productId);
		context = this;
		initView();
	}

	private void initView(){
		dotContainer = (LinearLayout)findViewById(R.id.product_image_dot_container);
		dot = (View)findViewById(R.id.dot);
		
		cartButton = (Button)findViewById(R.id.product_detail_cart);
		titleTextView = (TextView)findViewById(R.id.product_detail_titlebar);
		
		nameTextView = (TextView)findViewById(R.id.product_detail_name);
		descTextView = (TextView)findViewById(R.id.product_detail_desc);
		priceTextView = (TextView)findViewById(R.id.product_detail_price);
		
		addToCartButton = (Button)findViewById(R.id.add_to_cart);
		addToWishButton = (Button)findViewById(R.id.add_to_wish);
		shareButton = (Button)findViewById(R.id.product_detail_share);
		backButton = (Button)findViewById(R.id.product_detail_back);
		
		backButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//查看购物车 
		cartButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
//				intent.putExtra("url", ((TextView)(v.findViewById(R.id.list_product_url))).getText().toString());
				intent.setClass(context, ShoppingcartActivity.class);
				context.startActivity(intent);
			}
		});
		
		myCartNumber = (TextView)findViewById(R.id.product_detail_cart_number);
		//头部显示购物车商品数量。如果是已登录用户，更新UserId；
		myCart = new Cart(userId);
		myCart.setDbHelper(new MySqLiteHelper(context));
//		int size = myCart.getCartSize(userId);
//		if (size > 0)
//			myCartNumber.setText("" + size);
		
//		myCart = myCart.getCart(userId);
//		Log.d(TAG, "用户Id：" + myCart.getUserId());
//		Log.d(TAG, "商品：" + myCart.getProducts());
		
//		productImageContainer = (LinearLayout)findViewById(R.id.product_image_container);
		
		new Thread(new Runnable() {
		            
	        @Override  
	        public void run() {
		        	productDetailJson = HttpFactory.getHttp().getUrlContext("/" + productUrl + "/?format=true", context);
//		        	productDetailJson = HttpFactory.getHttp().getUrlContext("/");
//		        	Log.d(TAG, productDetailJson);
                runOnUiThread(new Runnable() {                    
                    private ViewPager mViewPager;
					private ProductDetaiViewPagerAdapter mViewPagerAdapter;

					@Override  
                    public void run() { 
						Log.d(TAG, "in run");
                    		JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(productDetailJson);
								titleTextView.setText(jsonObject.get("name").toString());
								nameTextView.setText(jsonObject.get("name").toString());
								descTextView.setText(jsonObject.get("name_desc").toString());
								priceTextView.setText(jsonObject.get("price").toString());
								//获取商品的多张图片URL；
								JSONArray allImages = new JSONArray(jsonObject.get("allImageUrl").toString());
								ArrayList<String> allImagesUrl = new ArrayList<String>();
								for (int i = 0; i < allImages.length(); i++) {
									JSONObject imageJsonObject = new JSONObject(allImages.get(i).toString());
									allImagesUrl.add(imageJsonObject.getString("mini_url"));
								}
								
								//将图片URL转换成对应的ImageView	
								mViewPager = (ViewPager)findViewById(R.id.product_detail_images_viewpager);  
						        mViewPagerAdapter = new ProductDetaiViewPagerAdapter(context, allImagesUrl);  
						        mViewPager.setAdapter(mViewPagerAdapter);
						        
						        //图片下方的圆点，切换图片对应的圆点高亮
						        LayoutParams lp = dot.getLayoutParams();
						        dotsList.add(dot);//xml文件中高亮的那个
						        for (int i = 0; i < allImages.length() - 1; i++) {
									View dotItem = new View(context, null);
									dotItem.setLayoutParams(lp);
									dotItem.setBackgroundResource(R.drawable.dot_normal);

									dotContainer.addView(dotItem);
									dotsList.add(dotItem);
								}
						        
						        //viewPager的图片滑动时，对应的dot要高亮（不同图片）
						        mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

									@Override
									public void onPageScrollStateChanged(
											int arg0) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onPageScrolled(int arg0,
											float arg1, int arg2) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onPageSelected(int position) {
										// TODO Auto-generated method stub
										dotsList.get(currentIndex).setBackgroundResource(R.drawable.dot_normal);
										dotsList.get(position).setBackgroundResource(R.drawable.dot_focused);
										currentIndex = position;
									}});
								 
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                    		
                    }
		        });
	        }
		}).start();
		
		//加入购物车。用户ID暂时用999。数量为1。
		addToCartButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
//				int userId = 999;
				int productNumber = 1;
				
//				Cart cartItem = new Cart(userId, productId, productNumber);
//				cartItem.setDbHelper(new MySqLiteHelper(context));
				myCart.setProductId(productId);
				myCart.setProductNumber(productNumber);
				int cartSize = myCart.addToCart(myCart);

				Log.d(TAG, "" + cartSize);
				Toast.makeText(context,"成功放入购物车", Toast.LENGTH_SHORT).show();
				if (cartSize > 0) {
					myCartNumber.setText("" + cartSize);
					//and 设置带气泡背景图
				}
				
				*/
				
				
				//以上是保存到本地数据库，下面是通过cookie保存到服务器端
				new Thread(new Runnable() {
					@Override  
		            public void run() {
		            		String addCartUrl = "/addCart.do?qty=1&flag=false&productId=" + productId;
						
						//url = "/ajax/getCart.do" 包括content,qty两个字段；
						String ret = HttpFactory.getHttp().getUrlContext(addCartUrl, context);
						Log.d(TAG, ret);
						
						try {
							retJsonObject = new JSONObject(ret);
//							Log.d(TAG, retJsonObject.getString("qty"));//购物车商品数量
//							Log.d(TAG, retJsonObject.getString("amount"));//购物车合计金额
//							mHandler.obtainMessage(MSG_SUCCESS,bm).sendToTarget();
							
							myCartNumber.post(new Runnable() {//另外一种更简洁的发送消息给ui线程的方法。  
				                  
				                @Override  
				                public void run() {//run()方法会在ui线程执行  
				                		try {
											myCartNumber.setText("" + retJsonObject.getString("qty"));
											Toast.makeText(context, "成功放入购物车", Toast.LENGTH_SHORT).show();
											//and 设置带气泡背景图
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
				                }  
				            });
							//以上通过post方式调用隐式handler
							//下面方法调用new Handler()方式。亦可。
//							mHandler.obtainMessage(MSG_SUCCESS, "" + retJsonObject.getString("qty")).sendToTarget();
//							Toast.makeText(context, "成功放入购物车", Toast.LENGTH_SHORT).show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
				}).start();
				
				
			}
		});
		
		addToWishButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addToWish();
			}
		});
		
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				share();
			}
		});
	}
	
	/**
	 * 商品加入收藏
	 * @return
	 */
	protected boolean addToWish(){
		return false;
	}
	
	/**
	 * 分享商品
	 * 分享内容链接已经能取到，商品名和简要描述木有传过来
	 */
	protected void share(){
		
	}
	
	private Handler mHandler = new Handler() {  
        

		public void handleMessage (Message msg) {//此方法在ui线程运行  
            switch(msg.what) {
            case MSG_SUCCESS:  
				myCartNumber.setText(msg.obj.toString());  
                break;  
  
            case MSG_FAILURE :  
                Toast.makeText(context, "放入购物车失败", Toast.LENGTH_SHORT).show();  
                break;  
            }  
        }  
    };
}
