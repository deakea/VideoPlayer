package com.android.deak.videoplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.adapter.FragmentsPagerAdaper;
import com.android.deak.videoplayer.data.DataCaChe;
import com.android.deak.videoplayer.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private TabLayout mTab;
    private NoScrollViewPager mViewPager;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    private EditText mEtSearch;
    private ImageView mIvSearch;
    private TextView mTvCancel;
    private LinearLayout mLayoutTab;
    private InputMethodManager mImm;
    private SearchFragment mSearchFragment;
    private boolean isFirst;

    @Override
    public int getResourcesId() {
        return R.layout.home_fragment;
    }

    @Override
    public void init() {
        mEtSearch = (EditText) getActivity().findViewById(R.id.edt_search);
        mIvSearch = (ImageView) getActivity().findViewById(R.id.iv_home_fragment_search);
        mTvCancel = (TextView) getActivity().findViewById(R.id.tv_home_fragment_search_cancel);
        mLayoutTab = (LinearLayout) getActivity().findViewById(R.id.ll_home_fragment_tab);
        mViewPager = (NoScrollViewPager) getActivity().findViewById(R.id.fragment_pager);
        mTab = (TabLayout) getActivity().findViewById(R.id.home_fragment_tab);
        mSearchFragment = new SearchFragment();
        isFirst = true;
        initTab();
    }

    //全部为空 喜剧科幻 恐怖 剧情 魔幻 罪案 冒险 动作 悬疑
    private void initTab() {
        mTitles.add("全部内容");
        mTitles.add("喜剧科幻");
        mTitles.add("剧情");
        mTitles.add("魔幻");
        mTitles.add("罪案");
        mTitles.add("冒险");
        mTitles.add("动作");
        mTitles.add("悬疑");

        mTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < mTitles.size(); i++) {
            mTab.addTab(mTab.newTab().setText(mTitles.get(i)));
            if (i == 0) {
                mFragments.add(new PagerFragment(""));
            } else {
                mFragments.add(new PagerFragment(mTitles.get(i)));
            }
        }
        FragmentsPagerAdaper mAdapter = new FragmentsPagerAdaper(getFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    public void afterInit() {
        mEtSearch.setOnClickListener(this);
        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_search:
                DataCaChe.isSearch = true;
                if (isFirst) {
                    mIvSearch.setVisibility(View.GONE);
                    mTvCancel.setVisibility(View.VISIBLE);
                    mLayoutTab.setVisibility(View.GONE);
                    getFragmentManager().beginTransaction().add(R.id.fl_home_search, mSearchFragment).commitAllowingStateLoss();
                    isFirst = false;
                }
                mTvCancel.setOnClickListener(this);
                mEtSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().equals("")) {
                            Message message = new Message();
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("name", s.toString());
                            message.setData(bundle);
                            mSearchFragment.handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 2;
                            mSearchFragment.handler.sendMessage(message);
                        }
                    }
                });

                break;
            case R.id.tv_home_fragment_search_cancel:
                getFragmentManager().beginTransaction().remove(mSearchFragment).commitAllowingStateLoss();
                mIvSearch.setVisibility(View.VISIBLE);
                mTvCancel.setVisibility(View.GONE);
                mLayoutTab.setVisibility(View.VISIBLE);
                mEtSearch.setText("");
                mImm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
                isFirst = true;
                DataCaChe.isSearch = false;
                break;
        }
    }
}
