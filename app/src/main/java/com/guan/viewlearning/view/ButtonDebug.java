package com.guan.viewlearning.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

public class ButtonDebug extends Button {


    String TAG = "ButtonDebug";

    public ButtonDebug(Context context) {
        super(context);
    }

    public ButtonDebug(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG,"width = " + widthSize + ", height ="+heightSize);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        Log.d(TAG,"measuredWidth = " + measuredWidth + ", measuredHeight ="+measuredHeight);
    }
}
