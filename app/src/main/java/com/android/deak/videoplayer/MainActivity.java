package com.android.deak.videoplayer;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.android.deak.videoplayer.fragment.HomeFragment;
import com.android.deak.videoplayer.fragment.LocalFragment;
import com.android.deak.videoplayer.fragment.MenuFragment;
import com.android.deak.videoplayer.fragment.MineFragment;
import com.android.deak.videoplayer.fragment.SubscribeFragment;
import com.android.deak.videoplayer.manager.SystemBarTintManager;
import com.android.deak.videoplayer.utils.SystemBarUtils;
import com.android.deak.videoplayer.widget.PullLayout;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.Vitamio;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, PullLayout.IPullListener {
    private List<Fragment> mFragments = new ArrayList<>();
    private RadioGroup mRadio;
    private ImageView mMenu;
    private int mCurrPosition = 0;
    private boolean isFirst = true;
    private PullLayout mPullLayout;
    private int mCurrId = R.id.radio_home;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setTintColor(Color.TRANSPARENT);
            SystemBarUtils.setMiuiStatusBarDarkMode(this,true);
            SystemBarUtils.setMeizuStatusBarDarkIcon(this,true);
            getWindow().getDecorView().setPadding(0, 0, 0, 0);
        }

        init();
    }

    private void init() {
        mMenu = (ImageView) findViewById(R.id.iv_main_activity_menu);
        mPullLayout = (PullLayout) findViewById(R.id.pulllayout);
        mPullLayout.addOnPullListener(this);
        mFragments.add(new HomeFragment());
        mFragments.add(new SubscribeFragment());
        mFragments.add(new LocalFragment());
        mFragments.add(new MineFragment());
        mRadio = (RadioGroup) findViewById(R.id.main_activity_radiogroup);
        mRadio.setOnCheckedChangeListener(this);
        initMenu();
    }

    private void initMenu() {
        MenuFragment menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.menu_framelayout, menuFragment).commitAllowingStateLoss();
        mMenu.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirst) {
            mRadio.check(R.id.radio_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main_activity, mFragments.get(0)).commitAllowingStateLoss();
            isFirst = false;
        } else {
            mRadio.check(mCurrId);
            switchFragment(mFragments.get(mCurrPosition), mFragments.get(mCurrPosition));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_home:
                switchFragment(mFragments.get(mCurrPosition), mFragments.get(0));
                mCurrPosition = 0;
                mCurrId = R.id.radio_home;
                break;
            case R.id.radio_subscribe:
                switchFragment(mFragments.get(mCurrPosition), mFragments.get(1));
                mCurrPosition = 1;
                mCurrId = R.id.radio_subscribe;
                break;
            case R.id.radio_local:
                switchFragment(mFragments.get(mCurrPosition), mFragments.get(2));
                mCurrPosition = 2;
                mCurrId = R.id.radio_local;
                break;
            case R.id.radio_mine:
                switchFragment(mFragments.get(mCurrPosition), mFragments.get(3));
                mCurrPosition = 3;
                mCurrId = R.id.radio_mine;
                break;
        }
    }


    public void switchFragment(Fragment from, Fragment to) {
        if (from == to) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (from.isAdded()) {
            fragmentTransaction.hide(from);
        }
        if (!to.isAdded()) {
            fragmentTransaction.add(R.id.fl_main_activity, to);
        }
        fragmentTransaction.show(to).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_main_activity_menu:
                if (!mPullLayout.isOpen) {
                    mPullLayout.open();
                } else {
                    mPullLayout.closed();
                }
                break;
        }
    }

    @Override
    public void onExpanded() {

    }

    @Override
    public void onPullChange(int cur, int max) {

    }

    @Override
    public void onCollapsed() {

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
