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
     * @param deltaY         ��ֱ�����˲ʱƫ���� / �仯�� dx   ������ͷ����Ϊ-, �ײ���ͷ����Ϊ+
     * @param scrollX
     * @param scrollY        ��ֱ�����ƫ���� / �仯��
     * @param scrollRangeX
     * @param scrollRangeY   ��ֱ���򻬶��ķ�Χ
     * @param maxOverScrollX
     * @param maxOverScrollY ��ֱ������󻬶���Χ
     * @param isTouchEvent   �Ƿ�����ָ��������, trueΪ��ָ, falseΪ����
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        Log.d(TAG, "deltaY: " + deltaY + " scrollY: " + scrollY + " scrollRangeY: " + scrollRangeY
                + " maxOverScrollY: " + maxOverScrollY + " isTouchEvent: " + isTouchEvent);
        //��ָ���� ����������
        if (isTouchEvent && deltaY < 0) {
            // ��������˲ʱ�仯���ľ���ֵ����Header, �Ϳ���ʵ�ַŴ�Ч��
            if (mImage.getHeight()<=drawableHeight){
                int newHeight= (int) (mImage.getHeight()+Math.abs(deltaY / 3.0f));
                // �߶Ȳ�����ͼƬ���߶�ʱ,��������Ч
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
                // ִ�лص�����, ��ʽһ: ���Զ���\ֵ����
                // �ӵ�ǰ�߶�mImage.getHeight(), ִ�ж�����ԭʼ�߶�mOriginalHeight
                final int startHeight = mImage.getHeight();
                final int endHeight = mOriginalHeight;
                //ִ�лص���������ʽ�����Զ���Animation
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
