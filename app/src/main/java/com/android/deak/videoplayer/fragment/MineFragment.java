package com.android.deak.videoplayer.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.android.deak.videoplayer.R;

/**
 * 个人中心
 */
public class MineFragment extends  BaseFragment implements View.OnClickListener {
    private LinearLayout mHistory;
    private LinearLayout mLocal;
    private LinearLayout mAccount;
    private LinearLayout mSetting;
    @Override
    public int getResourcesId() {
        return R.layout.mine_fragment;
    }

    @Override
    public void init() {
        mHistory = (LinearLayout) getActivity().findViewById(R.id.ll_mine_fragment_history);
        mAccount = (LinearLayout) getActivity().findViewById(R.id.ll_mine_fragment_localVideo);
        mLocal = (LinearLayout) getActivity().findViewById(R.id.ll_mine_fragment_account);
        mSetting = (LinearLayout) getActivity().findViewById(R.id.ll_mine_fragment_setting);
        mHistory.setOnClickListener(this);
        mAccount.setOnClickListener(this);
        mLocal.setOnClickListener(this);
        mSetting.setOnClickListener(this);
    }

    @Override
    public void afterInit() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_mine_fragment_history:
                break;
            case R.id.ll_mine_fragment_localVideo:
                break;
            case R.id.ll_mine_fragment_account:
                break;
            case R.id.ll_mine_fragment_setting:
                break;
        }
    }
}
