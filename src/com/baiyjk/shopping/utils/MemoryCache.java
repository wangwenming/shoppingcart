package com.baiyjk.shopping.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

public class MemoryCache {
	private static final String TAG = "MemoryCache";
	// 放入缓存时是个同步操作
	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	private static Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private long size = 0;// current allocated size
	// 缓存只能占用的最大堆内存
	private long limit = 10000000;// max memory in bytes

	public MemoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	public void setLimit(long new_limit) {
		limit = new_limit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	/**
	 * 根据key从缓存中取Bitmap
	 * @param id
	 * @return Bitmap
	 */
	public Bitmap get(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			Log.d("MemoryCache", "memory中找到");
			return cache.get(id);
		} catch (NullPointerException ex) {
			return null;
		}
	}

	public void put(String id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			Log.d("MemoryCache", "放入memory中，大小：" + getSizeInBytes(bitmap) + " 字节" );
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 * 
	 */
	private void checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			// 先遍历最近最少使用的元素
			Log.d("MemoryCache", "占用内存超出最大限度");
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}

	public void clear() {
		cache.clear();
		size = 0;
		Log.d(TAG, "清空图片缓存");
	}

	/**
	 * 图片占用的内存
	 * 
	 * @param bitmap
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		
	    
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        } else {
            return bitmap.getByteCount();
	        
	    }
//		return bitmap.getRowBytes() * bitmap.getHeight();
	}
	/* 如果使用SoftReference... 不需要自己控制内存大小
	private Map<String, SoftReference<Bitmap>> cache = Collections  
            .synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());  
  
    public Bitmap get(String id) {  
        if (!cache.containsKey(id))  
            return null;  
        SoftReference<Bitmap> ref = cache.get(id);  
        return ref.get();  
    }  
  
    public void put(String id, Bitmap bitmap) {  
        cache.put(id, new SoftReference<Bitmap>(bitmap));  
    }  
  
    public void clear() {  
        cache.clear();  
    }
    */ 
}
