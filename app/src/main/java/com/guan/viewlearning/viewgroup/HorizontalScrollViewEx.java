package com.guan.viewlearning.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class HorizontalScrollViewEx extends ViewGroup {
    public HorizontalScrollViewEx(Context context) {
        super(context);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

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

        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) { //容器使用的宽高均为wrapcontent

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
}
