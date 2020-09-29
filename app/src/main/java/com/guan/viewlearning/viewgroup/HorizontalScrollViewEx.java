package com.guan.viewlearning.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class HorizontalScrollViewEx extends ViewGroup {

    public static final String TAG = "HorizontalScrollViewEx";

    public HorizontalScrollViewEx(Context context) {
        super(context);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childLeft =0 ;
        final int childCount = getChildCount();

        mChildrenSize = childCount;

        for (int i =0 ;i< childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();
                mChildWidth = childWidth;

                //放置childview 的位置
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // call super
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = 0;
        int measureHeight = 0;

        int childCount = getChildCount();

        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        if (childCount ==0 ) {
            setMeasuredDimension(0,0);

        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) { //容器使用的宽高均为wrap_content

            final View childview = getChildAt(0);

            //这里要放置所有子视图，因此该ViewGroup的
            // 测量宽度：childCount * childview_width
            // 测量高度：第一个视图的高度
            measuredWidth = childview.getMeasuredWidth() * childCount;
            measureHeight = childview.getMeasuredHeight();
            setMeasuredDimension(measuredWidth,measureHeight);

        } else if (heightMode == MeasureSpec.AT_MOST) {
            final View childveiw = getChildAt(0);
            measureHeight = childveiw.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize,childveiw.getMeasuredHeight());

        } else if (widthMode == MeasureSpec.AT_MOST) {
            final View childview = getChildAt(0);
            measuredWidth = childview.getMeasuredWidth()*childCount;
            setMeasuredDimension(measuredWidth,heightSpaceSize);
        }





    }



    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private int mLastX=0;
    private int mLastY=0;

    private void init(){
        if (mScroller == null ){
            mScroller = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean intercepted = false;

        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                intercepted = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();;
                    intercepted = true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)){
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                intercepted = false;
                break;
            }
            default:
                break;
        }
        Log.d(TAG, "onInterceptTouchEvent: " + intercepted);
        mLastX = x;
        mLastY = y;
        mLastXIntercept =x;
        mLastYIntercept =y;

        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :{
                if (mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE :{
                int deltaX = x - mLastX;
                int delatY = y - mLastY;

                scrollBy(-deltaX,0);
                break;
            }
            case MotionEvent.ACTION_UP:{
                int scrollX = getScrollX();
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();

                if (Math.abs(xVelocity) >=50) {
                    mChildIndex =xVelocity > 0 ?mChildIndex-1:mChildIndex +1;
                } else {
                    mChildIndex =  (scrollX + mChildWidth /2 ) / mChildWidth;
                }

                mChildIndex = Math.max(0,Math.min(mChildIndex,mChildrenSize -1));

                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy(dx,0);
                mVelocityTracker.clear();
                break;
            }
            default :
                break;
        }
        mLastX = x;
        mLastY =y;
        return  true;

    }

    void smoothScrollBy(int dx,int y){
        mScroller.startScroll(getScrollX(),0,dx,0,500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {

            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }
}
