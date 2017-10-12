package com.android.deak.videoplayer.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.deak.videoplayer.MyVideoPlayerActivity;
import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.adapter.MyGridViewAdapter;
import com.android.deak.videoplayer.entity.AllEntity;
import com.android.deak.videoplayer.utils.HttpUrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 *剧集分类显示
 */
@SuppressLint("ValidFragment")
public class PagerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private View mView;
    private GridView mGridView;
    private SwipeRefreshLayout mRefresh;
    private List<AllEntity.DataBean.ResultsBean> mDataList;
    private AllEntity entity;
    private MyGridViewAdapter mAdapter;
    private String mTag;

    private int mCurrPage = 1;
    private boolean isClear = true;
    private boolean isFirst = true;

    @SuppressLint("ValidFragment")
    public PagerFragment(String mTag) {
        this.mTag = mTag;
    }
    public PagerFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.pager_fragment, container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                onLoading();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (getActivity() == null || getActivity().isFinishing()) return;
                onLoadFinish();
            }
        }.execute();
    }

    private void onLoading() {
        mGridView = (GridView) mView.findViewById(R.id.gv_pager_fragemnt);
        mGridView.setOnScrollListener(this);
        mAdapter = new MyGridViewAdapter(getActivity());
        mRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.pager_fragment_refresh);
        mRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        mRefresh.setOnRefreshListener(this);
        mGridView.setOnItemClickListener(this);
    }

    private void onLoadFinish() {
        mGridView.setAdapter(mAdapter);
        onRefresh();
    }

    public void getData() {
        mRefresh.setRefreshing(true);
        RequestParams params = new RequestParams(HttpUrlUtils.seasonList);
        params.addBodyParameter("cat", mTag);
        params.addBodyParameter("rows", "9");
        params.addBodyParameter("page", mCurrPage + "");
        params.addBodyParameter("sort", "");
        params.addBodyParameter("isFinish", "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (!isFirst) {
                    entity = HttpUrlUtils.parseJason(result, AllEntity.class);
                    if (mDataList == null) {
                        mDataList = entity.getData().getResults();
                    } else {
                        if (isClear) {
                            mDataList.clear();
                        }
                        mDataList.addAll(entity.getData().getResults());
                    }
                    mAdapter.setList(mDataList);
                    mAdapter.notifyDataSetChanged();
                    mRefresh.setRefreshing(false);
                }else {
                    entity = HttpUrlUtils.parseJason(result, AllEntity.class);
                    if (mDataList == null) {
                        mDataList = entity.getData().getResults();
                    } else {
                        if (isClear) {
                            mDataList.clear();
                        }
                        mDataList.addAll(entity.getData().getResults());
                    }
                    mAdapter.setList(mDataList);
                    mAdapter.notifyDataSetChanged();
                    mRefresh.setRefreshing(false);
                }
                isFirst = false;
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void onRefresh() {
        mCurrPage = 1;
        isClear = true;
        getData();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滚动到底部
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                ) {
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                mCurrPage++;
                isClear = false;
                getData();
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), MyVideoPlayerActivity.class);
        intent.putExtra("id",mDataList.get(position).getId()+"");
        intent.putExtra("name",mDataList.get(position).getTitle());
        intent.putExtra("cover",mDataList.get(position).getVerticalUrl());
        intent.putExtra("upInfo",mDataList.get(position).getUpInfo()+"");
        startActivity(intent);
    }
}
