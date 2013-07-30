package com.baiyjk.shopping.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileCache {
	private File cacheDir;

	private static final String CACHDIR = "ImgCach";
    private static final String WHOLESALE_CONV = ".cach";
                                                            
    private static final int MB = 1024*1024;
    private static final int CACHE_SIZE = 10;
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
    
    public FileCache() {
        //清理文件缓存
        removeCache(getDirectory());
    }
//	public FileCache(Context context) {
//		// 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
//		// 没有SD卡就放在系统的缓存目录中
//		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
//			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "LazyList");
//			Log.d("FileCache", "new File in sd card: " + cacheDir);
//		
//		} else
//			cacheDir = context.getCacheDir();Log.d("FileCache", "get cache directory on the filesystem : " + cacheDir);
//		if (!cacheDir.exists()){
//			boolean success = cacheDir.mkdirs();//此方法创建文件的目录是？
//			Log.d("FileCache", "mkdir: " + success);
//		}
//	}

	//创建File,内容为空。将url的hashCode作为缓存的文件名
//	public File getFile(String url) {
//		String filename = String.valueOf(url.hashCode());
//		File f = new File(cacheDir, filename);
//		return f;
//
//	}
	
	/** 从缓存中获取图片 **/
    public Bitmap getImage(final String url) {    
        final String path = getDirectory() + "/" + convertUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            if (bmp == null) {
                file.delete();Log.d("FileCache", "从文件缓存中取图失败");
            } else {
                updateFileTime(path);
                Log.d("FileCache", "从文件缓存中取图");
                return bmp;
            }
        }
        return null;
    }
                                                                
    /** 将图片存入文件缓存 **/
    public void saveBitmap(Bitmap bm, String url) {
        if (bm == null) {
            return;
        }
        //判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            //SD空间不足
//            return;
        		removeCache(getDirectory());Log.d("FileCache", "放图到文件缓存中空间不够，清理空间");
        }
        String filename = convertUrlToFileName(url);
        String dir = getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();
        File file = new File(dir +"/" + filename);
        try {
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            Log.d("FileCache", "放图到文件缓存中");
            outStream.close();
        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }
    }

    
    
    /**
     * 计算存储目录下的文件大小，
     * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
     * 那么删除40%最近没有被使用的文件
     */
    private boolean removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }
        if (!android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return false;
        }
                                                            
        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }
                                                            
        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }
                                                            
        if (freeSpaceOnSd() <= CACHE_SIZE) {
            return false;
        }
                                                                    
        return true;
    }
                                                                
    /** 修改文件的最后修改时间 **/
    public void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
        Log.d("FileCache", "修改文件的最后修改时间" + path);
        
    }
                                                                
    /** 计算sdcard上的剩余空间 **/
    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    } 
                                                                
    /** 将url转成文件名 **/
    private String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1] + WHOLESALE_CONV;
    }
                                                                
    /** 获得缓存目录 **/
    private String getDirectory() {
        String dir = getSDPath() + "/" + CACHDIR;
        Log.d("FileCache", "文件的缓存目录" + dir);
        return dir;
    }
                                                                
    /** 取SD卡路径 **/
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();  //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    } 
                                                            
    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
