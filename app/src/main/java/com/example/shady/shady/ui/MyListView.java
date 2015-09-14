package com.example.shady.shady.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by shady on 2015/9/13.
 */
public class MyListView extends ListView {
    private static final String TAG = "MyListView";
    private int mOriginalHeight;
    private int drawableHeight;
    private ImageView mImage;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param deltaX
     * @param deltaY         竖直方向的瞬时偏移量 / 变化量 dx   顶部到头下拉为-, 底部到头上拉为+
     * @param scrollX
     * @param scrollY        竖直方向的偏移量 / 变化量
     * @param scrollRangeX
     * @param scrollRangeY   竖直方向滑动的范围
     * @param maxOverScrollX
     * @param maxOverScrollY 竖直方向最大滑动范围
     * @param isTouchEvent   是否是手指触摸滑动, true为手指, false为惯性
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        Log.d(TAG, "deltaY: " + deltaY + " scrollY: " + scrollY + " scrollRangeY: " + scrollRangeY
                + " maxOverScrollY: " + maxOverScrollY + " isTouchEvent: " + isTouchEvent);
        //手指拉动 并且是下啦
        if (isTouchEvent && deltaY < 0) {
            // 把拉动的瞬时变化量的绝对值交给Header, 就可以实现放大效果
            if (mImage.getHeight()<=drawableHeight){
                int newHeight= (int) (mImage.getHeight()+Math.abs(deltaY / 3.0f));
                // 高度不超出图片最大高度时,才让其生效
                mImage.getLayoutParams().height=newHeight;
                mImage.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // 执行回弹动画, 方式一: 属性动画\值动画
                // 从当前高度mImage.getHeight(), 执行动画到原始高度mOriginalHeight
                final int startHeight = mImage.getHeight();
                final int endHeight = mOriginalHeight;
                //执行回弹动画，方式二：自定义Animation
                ResetAnimation animation = new ResetAnimation(mImage, startHeight, endHeight);
                startAnimation(animation);
                break;
            case MotionEvent.ACTION_DOWN:
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
