package com.baiyjk.shopping;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout{
	private OnScrollListener mOnScrollListenerCallback = null;
	private GestureDetector mGestureDetector;
	private boolean bLockScrollX = false;
	private boolean bTouchIntercept = false;
	private boolean hTouch = false;//横向滑动
	private final int MIN_TOUCH = 25;
	
	public MyRelativeLayout(Context context) {
		this(context, null, 0);
	}
	
	public MyRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mGestureDetector = new GestureDetector(new MyGestureListener());  
	}
	
	
	/** 
     *  设置滚动监听接口 
     *  @param l 
     */  
    public void setOnScrollListener(OnScrollListener l){  
        mOnScrollListenerCallback = l;  
    }  
    
    /* 
     * Progress: 
     * 1. 重载dispatchTouchEvent，将消息传递给GestureDetector; 
     * 2. 重载手势中onDown 和 onScroll两个函数; 
     * 3. 在onDown中，默认对水平滚动方向加锁; 
     * 4. 在onScroll中，判断e1与e2的水平方向与垂直方向距离: 
     *    a. 如果垂直方向大，则表明是上下滚动，且返回false表明当前手势不用拦截； 
     *    b. 如果水平方向大，则表明是左右滚动，且返回true表明当前手势需要拦截； 
     * 5. 重载onInterceptTouchEvent，如果手势返回为true，则onInterceptTouchEvent也返回true； 
     * 6. 如果要拦截手势消息，则需要重载onTouchEvent，或子视图中重载这个函数，来处理这条消息； 
     * 7. 如果自己处理，则对水平方向滚动去锁(表明当前用户想左右滚动)； 
     *  
     *                                     ---------- 
     *  ----------------------      ------>| onDown | 
     *  |                    |      |      ---------- 
     *  | dispatchTouchEvent | <----                         ------ false: 上下滚动 
     *  |                    |      |      ------------     / 
     *  ----------------------      ------>| onScroll | ---- 
     *            |                        ------------     \ 
     *            |                                          ------ true : 左右滚动 
     *            |   intercept = true   ---------------- 
     *            |----------------------| onTouchEvent | 
     *            |                      ---------------- 
     * ------------------------- 
     * |                       | 
     * | onInterceptTouchEvent | 
     * |                       | 
     * ------------------------- 
     * 
     */  
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        bTouchIntercept = mGestureDetector.onTouchEvent(ev);  
          
        if(MotionEvent.ACTION_UP == ev.getAction() && !bLockScrollX){  
            if(mOnScrollListenerCallback != null){  
                mOnScrollListenerCallback.doOnRelease();  
            }  
        }  
          
        return super.dispatchTouchEvent(ev);  
    }  
      
    // viewgroup.onInterceptTouchEvent  
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        super.onInterceptTouchEvent(ev); 
        return bTouchIntercept;  
    }  
  
    // view.onTouchEvent  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        bLockScrollX = false;  
        return super.onTouchEvent(event);  
    } 
    
	/**
	 * 自定义手势监听
	 * @author lyy
	 */
	public class MyGestureListener extends SimpleOnGestureListener{
		@Override  
        public boolean onDown(MotionEvent e) {  
            bLockScrollX = true;  
            return super.onDown(e);  
        }  
  
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
            if(!bLockScrollX){  
                if(mOnScrollListenerCallback != null){  
                    mOnScrollListenerCallback.doOnScroll(e1, e2, distanceX, distanceY);  
                }  
            }
            if(Math.abs(e1.getY() - e2.getY()) > Math.abs(e1.getX() - e2.getX())){  
                return false;  //上下
            }else{  
                return true;  //左右
            }  
        }
	}
	
	public interface OnScrollListener {  
        void doOnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);  
        void doOnRelease();  
    } 

}
