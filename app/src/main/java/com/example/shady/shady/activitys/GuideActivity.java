package com.example.shady.shady.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shady.shady.R;
import com.example.shady.shady.utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by shady on 2015/9/12.
 */
public class GuideActivity extends AppCompatActivity {
    private static final int[] mImageIds =
            new int[] { R.drawable.num1, R.drawable.num2, R.drawable.num3 };
    private ArrayList<ImageView> mImageView;
    private ImageView image;
    private ViewPager viewPager;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_pager);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        button= (Button) findViewById(R.id.button);
        initView();
        viewPager.setAdapter(new GuideAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position==mImageView.size() - 1){
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            PrefUtils.setBoolean(GuideActivity.this, "is_show", true);
                            Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        mImageView = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++) {
            image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);
            mImageView.add(image);
        }
    }

    /**
     * ViewPagerÊÊÅäÆ÷
     */
    class GuideAdapter extends PagerAdapter {

        @Override public int getCount() {
            return mImageIds.length;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageView.get(position));
            return mImageView.get(position);
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
