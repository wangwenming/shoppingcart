package com.baiyjk.shopping.model;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baiyjk.shopping.sqlite.MySqLiteHelper;

public class Cart {
	private int userId;
	private Map<Integer, Integer> products;
	private int productNumber;
	private MySqLiteHelper dbHelper;
	private int productId;

	private final String USER_ID = "USER_ID";
	private final String PRODUCT_ID = "PRODUCT_ID";
	private final String PRODUCT_NUMBER = "PRODUCT_NUMBER";
	private final String TABLE_NAME = "CART";

	public Cart (int userId){
		this.userId = userId;
	}
	
	public Cart(int userId, int productId, int productNumber) {
		this.userId = userId;
		this.productId = productId;
		this.productNumber = productNumber;
	}

	public Cart(int userId, Map<Integer, Integer> products) {
		this.userId = userId;
		this.products = products;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public Map<Integer, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<Integer, Integer> products) {
		this.products = products;
	}

	public int getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(int productNumber) {
		this.productNumber = productNumber;
	}

	public MySqLiteHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(MySqLiteHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	/**
	 * 加入购物车
	 * 
	 * @param cartItem
	 */
	public int addToCart(Cart cartItem) {
		Cart oldCartItem = getCartItem(cartItem.getUserId(), cartItem.getProductId());
		if (oldCartItem != null) {
			cartItem.setProductNumber(oldCartItem.getProductNumber() + cartItem.getProductNumber());
			updateItem(cartItem);
		} else {
			insertItem(cartItem);
		}
		return getCartSize(cartItem.getUserId());
	}

	/**
	 * 
	 * @param userId
	 * @param productId
	 * @param productNumber
	 * @return
	 */
	public int insertItem(Cart cartItem) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(USER_ID, cartItem.getUserId());
		values.put(PRODUCT_ID, cartItem.getProductId());
		values.put(PRODUCT_NUMBER, cartItem.getProductNumber());

		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection
		return 1;
	}

	/**
	 * get all products of someone's cart
	 * 
	 * @param userId
	 * @return
	 */
	public Cart getCart(int userId) {

		Map<Integer, Integer> cartMap = new HashMap<Integer, Integer>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, new String[] { USER_ID, PRODUCT_ID,
						PRODUCT_NUMBER }, USER_ID + "=?",
						new String[] { String.valueOf(userId) }, null, null,
						null, null);
		if (cursor.moveToFirst()){
			do {
				cartMap.put(cursor.getInt(1), cursor.getInt(2));
			} while (cursor.moveToNext());
			return new Cart(userId, cartMap);
			
		}
		 return null;
		
	}

	/**
	 * 获取用户购物车中某个商品的数量
	 * 
	 * @param userId
	 * @param productId
	 * @return
	 */
	public Cart getCartItem(int userId, int productId) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(
				TABLE_NAME,
				new String[] { USER_ID, PRODUCT_ID, PRODUCT_NUMBER },
				USER_ID + "=? AND " + PRODUCT_ID + "=?",
				new String[] { String.valueOf(userId),
						String.valueOf(productId) }, null, null, null, "1");
		if (cursor.moveToFirst()) {
			return new Cart(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
		}
		return null;
	}

	/**
	 * 更新用户购物车某个商品的数量
	 * 
	 * @param cartItem
	 * @return
	 */
	public int updateItem(Cart cartItem) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(PRODUCT_NUMBER, cartItem.getProductNumber());

		String[] selectArgs = { String.valueOf(cartItem.getUserId()),
				String.valueOf(cartItem.getProductId()) };
		return db.update(TABLE_NAME, values, USER_ID + "=? AND " + PRODUCT_ID
				+ "=?", selectArgs);
	}

	/**
	 * 删除用户购物车中某个商品
	 * 
	 * @param userId
	 * @param productId
	 * @return
	 */
	public int deleteCartItem(int userId, int productId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] selectArgs = { String.valueOf(userId),
				String.valueOf(productId) };
		return db.delete(TABLE_NAME, USER_ID + "=? AND " + PRODUCT_ID + "=?",
				selectArgs);
	}

	/**
	 * 清空用户的购物车
	 * 
	 * @param userId
	 * @return
	 */
	public int clearCart(int userId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String[] selectArgs = { String.valueOf(userId) };
		return db.delete(TABLE_NAME, USER_ID + "=?", selectArgs);
	}

	public int getCartSize(int userId) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME,
				new String[] { "sum(" + PRODUCT_NUMBER +")" }, USER_ID + "=?",
				new String[] { String.valueOf(userId) }, null, null, null, "1");
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
			// return Integer.parseInt(cursor.getString(0));
		}
		return 0;
	}
}
