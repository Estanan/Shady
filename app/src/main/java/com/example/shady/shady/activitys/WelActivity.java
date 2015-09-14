package com.example.shady.shady.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.shady.shady.R;
import com.example.shady.shady.utils.PrefUtils;

/**
 * Created by shady on 2015/9/12.
 */
public class WelActivity extends Activity{
    private static final long SPLASH_DELAY_MILLIS = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welactivity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, SPLASH_DELAY_MILLIS);

    }

    private void startAnimation(){
        //inflate һ������
        final View view = View.inflate(this, R.layout.welactivity, null);

        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(3000);         //���ý���ʱ��
        aa.setFillAfter(true);          //���ֶ���״̬
        view.startAnimation(aa);          //��ʼһ������

        setContentView(view);
        //���ö����������������������ʱ�������µ�Activity
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startMainActivity();
            }
        });
        jump();
    }

    private void jump() {
        boolean isSet = PrefUtils.getBoolean(this, "is_show", false);
        if (!isSet){
            startActivity(new Intent(this,GuideActivity.class));
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
