package com.example.shady.shady.activitys;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.R.anim;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.shady.shady.R;
import com.example.shady.shady.fragments.HomeFragment;
import com.example.shady.shady.utils.CircleImageView;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayList<String> menuLists;
    private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private LinearLayout mlinearlayout;
    private Fragment homeFragment;
    FragmentManager fm = getFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = (String) getTitle();

        mlinearlayout= (LinearLayout) findViewById(R.id.mlinearlayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        menuLists = new ArrayList<String>();
//        for (int i = 0; i < 5; i++)
//            menuLists.add("shadyApp" + i);
        menuLists.add("首页");
        menuLists.add("地图");
        menuLists.add("图片");
        menuLists.add("视频");
        menuLists.add("关于我们");
        initFragments();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menuLists);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
               R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Shady");
                invalidateOptionsMenu(); // Call onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initFragments() {
        homeFragment=new HomeFragment();
        fm.beginTransaction().replace(R.id.fragment_layout, homeFragment)
                .commit();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(mlinearlayout);
//        menu.findItem(R.id.action_websearch).setVisible(!isDrawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()) {
//            case R.id.action_websearch:
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri uri = Uri.parse("http://www.baidu.com");
//                intent.setData(uri);
//                startActivity(intent);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        Fragment contentFragment = new ContentFragment();
        switch (position){
            case 0:
                fm.beginTransaction().replace(R.id.fragment_layout, homeFragment)
                        .commit();
                break;
            case 1:
                break;
        }
        mDrawerLayout.closeDrawer(mlinearlayout);
    }
}
