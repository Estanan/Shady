package com.example.shady.shady.activitys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shady.shady.AccessTokenKeeper;
import com.example.shady.shady.R;
import com.example.shady.shady.fragments.HomeFragment;
import com.example.shady.shady.Constants;
import com.example.shady.shady.fragments.ImageFragment;
import com.example.shady.shady.fragments.MapFragment;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayList<String> menuLists;
    private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private LinearLayout mlinearlayout;
    private Fragment homeFragment;
    private Fragment imageFragment;
    private Fragment mapFragment;
    private Button mCurrentClickedButton;
    private LoginoutButton mLoginoutBtnDefault;
    FragmentManager fm = getFragmentManager();
    /**
     * 登陆认证对应的listener
     */
    private AuthListener mLoginListener = new AuthListener();
    /**
     * 登出操作对应的listener
     */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();
    private AuthInfo mAuthInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = (String) getTitle();

        mlinearlayout = (LinearLayout) findViewById(R.id.mlinearlayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        menuLists = new ArrayList<String>();
        //创建授权认证信息
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mLoginoutBtnDefault = (LoginoutButton) findViewById(R.id.login_out_button_default);
        mLoginoutBtnDefault.setWeiboAuthInfo(mAuthInfo, mLoginListener);
        mLoginoutBtnDefault.setLogoutListener(mLogoutListener);
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

        /**
         * 注销按钮：该按钮未做任何封装，直接调用对应 API 接口
         */
        final Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutAPI(MainActivity.this, Constants.APP_KEY,
                        AccessTokenKeeper.readAccessToken(MainActivity.this)).logout(mLogoutListener);
            }
        });
        mLoginoutBtnDefault.setExternalOnClickListener(mButtonClickListener);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mCurrentClickedButton != null) {
            if (mCurrentClickedButton instanceof LoginButton) {
                ((LoginButton) mCurrentClickedButton).onActivityResult(requestCode, resultCode, data);
            } else if (mCurrentClickedButton instanceof LoginoutButton) {
                ((LoginoutButton) mCurrentClickedButton).onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);

                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(MainActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    }

    /**
     * 请注意：为每个 Button 设置一个额外的 Listener 只是为了记录当前点击的
     * 是哪一个 Button，用于在 {@link #onActivityResult} 函数中进行区分。
     * 通常情况下，我们的应用不需要定义该 Listener。
     */
    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                mCurrentClickedButton = (Button) v;
            }
        }
    };

    private void initFragments() {
        homeFragment = new HomeFragment();
        imageFragment = new ImageFragment();
        mapFragment=new MapFragment();
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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
        switch (position) {
            case 0:
                fm.beginTransaction().replace(R.id.fragment_layout, homeFragment)
                        .commit();
                break;
            case 2:
                fm.beginTransaction().replace(R.id.fragment_layout, imageFragment).commit();
                break;

            case 1:
                fm.beginTransaction().replace(R.id.fragment_layout, mapFragment).commit();
                break;
        }
        mDrawerLayout.closeDrawer(mlinearlayout);
    }
}
