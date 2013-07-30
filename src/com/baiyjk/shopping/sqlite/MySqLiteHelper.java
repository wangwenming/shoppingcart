package com.baiyjk.shopping.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqLiteHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "byjk.db"; //数据库名称
	private static final int DATABASE_VERSION = 1;//数据库版本

	private static final String SQL_CREATE_CART =
		    "CREATE TABLE CART (USER_ID INTEGER,  PRODUCT_ID INTEGER, PRODUCT_NUMBER INTEGER )";
	private Context context;
	private SQLiteDatabase db;
	private CursorFactory factory;
	
	public MySqLiteHelper(Context context, String dbName, CursorFactory factory,
			int version) {
		super(context, dbName, factory, version);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.factory = factory;
	}
	
	public MySqLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_CART);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SQLiteDatabase getWritableDatabase(){
		// TODO create or open a writable database
		db = super.getWritableDatabase();
		if (db == null) {
//			return context.openOrCreateDatabase(DATABASE_NAME , Context.MODE_PRIVATE , null );
			return SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, factory);
		}
		return db;
	}
	
	@Override
	public SQLiteDatabase getReadableDatabase(){
		db = super.getReadableDatabase();
		if (db == null) {
//			return context.openOrCreateDatabase(DATABASE_NAME , Context.MODE_PRIVATE , null );
			return SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, factory);
		}
		return db;
	}
}
