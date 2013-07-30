package com.baiyjk.shopping;

import android.R.integer;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup{
	private RadioGroup radioGroup;
	private LocalActivityManager manager;
	private LinearLayout container;
	private Context context;
	private TextView titlebar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main);
		
		
		manager = getLocalActivityManager();
		container = (LinearLayout)findViewById(R.id.container);
		radioGroup = (RadioGroup)findViewById(R.id.tab_group);
		context = this;
		
		container.removeAllViews();
		container.addView(manager.startActivity(
				"PAGE_1", 
				new Intent(this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				switch (checkedId) {
				case R.id.main_tab_1://首页
					container.removeAllViews();
					titlebar = (TextView)findViewById(R.id.titlebar);
					((View)titlebar.getParent()).setVisibility(View.VISIBLE);
					container.addView(manager.startActivity(
							"PAGE_1", 
							new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView());
					
					titlebar.setText("百洋健康网");
					Log.e("click tab", "click tab home");
					break;
				case R.id.main_tab_2:
					container.removeAllViews();
					titlebar = (TextView)findViewById(R.id.titlebar);
					((View)titlebar.getParent()).setVisibility(View.VISIBLE);
					container.addView(manager.startActivity(
							"PAGE_2", 
							new Intent(context, CategoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView());
					
					titlebar.setText("全部分类");
					Log.e("click tab", "click tab category");
					break;
				case R.id.main_tab_3:
					container.removeAllViews();

					titlebar = (TextView)findViewById(R.id.titlebar);
					((View)titlebar.getParent()).setVisibility(View.VISIBLE);
					container.addView(manager.startActivity(
							"PAGE_3", 
							new Intent(context, AccountActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView());
					
					titlebar.setText("我的账户");
					Log.e("click tab", "click tab account");
					break;

				case R.id.main_tab_4:
					container.removeAllViews();
					titlebar = (TextView)findViewById(R.id.titlebar);
					((View)titlebar.getParent()).setVisibility(View.GONE);
//					titlebar.setText("我的购物车");
					container.addView(manager.startActivity(
							"PAGE_4", 
							new Intent(context, ShoppingcartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView());
					
					Log.e("click tab", "click tab cart");
					break;
				default:
					container.removeAllViews();
					Log.e("click tab", "click tab" + checkedId);
					break;
				}
			}
		});
	}

}
