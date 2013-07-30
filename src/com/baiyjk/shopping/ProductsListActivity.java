package com.baiyjk.shopping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baiyjk.shopping.adapter.ProdListSimpleAdapter;
import com.baiyjk.shopping.http.HttpFactory;

public class ProductsListActivity extends ListActivity{
	private TextView mTv;
	private Context context;
	private final String TAG = "products_list";
	private String productsJsonString;
	private Map<String, Object> map;
	private Button backButton;
	private Button filterButton;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products_list);
		
		initView();
	}
	
	private void initView(){
		mTv = (TextView)findViewById(R.id.productlistloading);
		backButton = (Button) findViewById(R.id.product_list_back);
		filterButton = (Button)findViewById(R.id.product_list_filter);
		
		//后退按钮
		backButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		context = this;
		new Thread(new Runnable() {
            
            @Override  
            public void run() {
                final String strContext = productsJsonString = HttpFactory.getHttp().getUrlContext("/conghuiyunying/sort-1059/?format=true", context);  
                runOnUiThread(new Runnable() {                    
                    @Override  
                    public void run() {  
                        if(strContext!=null){ 
                            mTv.setVisibility(View.GONE);
//                            SimpleAdapter adapter = new SimpleAdapter(context, getData(), R.layout.product_item, 
//                            		new String[]{"id","name", "info", "price","image"},
//                            		new int[] {R.id.list_product_id, R.id.list_product_name, R.id.list_product_info, R.id.list_product_price, R.id.list_product_image}
//                            );
                            ProdListSimpleAdapter adapter = new ProdListSimpleAdapter(context, getData(), R.layout.product_item, 
                            		new String[]{"url","name", "info", "price","image"},
                            		new int[] {R.id.list_product_url, R.id.list_product_name, R.id.list_product_info, R.id.list_product_price, R.id.list_product_image}
                            );
                            
                            setListAdapter(adapter);
//                            Log.d(TAG, strContext);
                        }else  
                            mTv.setText("加载失败......");  
                    }
                });  
            }  
        }).start(); 
		
	}
	
	private List<Map<String, Object>> getData(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		try{
			JSONArray productsJasonArray = new JSONArray(productsJsonString);
			for (int i = 0; i < productsJasonArray.length(); i++) {
//				Log.d(TAG, productsJasonArray.getJSONObject(i).toString());
				JSONObject productJsonObeject = new JSONObject(productsJasonArray.getJSONObject(i).toString());
				map = new HashMap<String, Object>();
				String urlString = productJsonObeject.get("main_first_category_seo_name").toString() + "/" + productJsonObeject.get("product_id") + ".html";
				map.put("url", urlString);
//				map.put("id", productJsonObeject.get("product_id"));
				map.put("name", productJsonObeject.get("product_name"));
				map.put("info", productJsonObeject.get("product_name_desc"));
				map.put("price", "￥" + productJsonObeject.get("price"));
				map.put("image", productJsonObeject.get("small_image"));
				
				list.add(map);
			}
		}catch (JSONException e) {
			// TODO: handle exception
		}
	    
		
		
		return list;
	}
}
