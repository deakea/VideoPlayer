package com.android.deak.videoplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.deak.videoplayer.MyVideoPlayerActivity;
import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.adapter.KeyWordAdapter;
import com.android.deak.videoplayer.adapter.SearchResultAdapter;
import com.android.deak.videoplayer.entity.KeyWordEntity;
import com.android.deak.videoplayer.entity.SearchResultEntity;
import com.android.deak.videoplayer.utils.HttpUrlUtils;
import com.android.deak.videoplayer.widget.MyCardGridView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * 搜索
 */
public class SearchFragment extends BaseFragment implements AbsListView.OnScrollListener {
    private MyCardGridView mGvKeyWord;
    private List<String> mDataList;
    private KeyWordAdapter mKeyWordAdapter;
    private String name;
    private int mCurrPage = 1;
    private LinearLayout mLayoutKeyWord;
    private ListView mLvResult;
    private SearchResultAdapter mResultAdapter;
    private SearchResultEntity entity;
    private List<SearchResultEntity.DataBean.ResultsBean> mResultDataList;
    private boolean isClear;
    private TextView mTvHead;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mLayoutKeyWord.setVisibility(View.GONE);
                    mLvResult.setVisibility(View.VISIBLE);
                    isClear = true;
                    mCurrPage = 1;
                    name = msg.getData().getString("name");
                    getSearchResult(name);
                    break;
                case 2:
                    mLayoutKeyWord.setVisibility(View.VISIBLE);
                    mLvResult.setVisibility(View.GONE);
                    mTvHead.setText(R.string.keyword);
                    break;
            }
        }
    };

    @Override
    public int getResourcesId() {
        return R.layout.search_fragment;
    }

    @Override
    public void init() {
        mGvKeyWord = (MyCardGridView) getActivity().findViewById(R.id.gv_search_fragment_keyword);
        mLayoutKeyWord = (LinearLayout) getActivity().findViewById(R.id.ll_search_fragment_keyword);
        mLvResult = (ListView) getActivity().findViewById(R.id.lv_search_result);
        mTvHead = (TextView) getActivity().findViewById(R.id.tv_search_fragment_head);
    }
    @Override
    public void afterInit() {
        mKeyWordAdapter = new KeyWordAdapter(getActivity());
        mGvKeyWord.setAdapter(mKeyWordAdapter);
        mResultAdapter = new SearchResultAdapter(getActivity());
        mLvResult.setAdapter(mResultAdapter);
        mLvResult.setOnScrollListener(this);
        mGvKeyWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("name",mDataList.get(position));
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
        mLvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MyVideoPlayerActivity.class);
                intent.putExtra("id",mResultDataList.get(position).getId() + "");
                startActivity(intent);
            }
        });
        getKeyWordData();
    }

    private void getKeyWordData() {
        RequestParams params = new RequestParams(HttpUrlUtils.hotWord);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                KeyWordEntity entity = HttpUrlUtils.parseJason(result, KeyWordEntity.class);
                mDataList = entity.getData().getWordList();
                mKeyWordAdapter.setList(mDataList);
                mKeyWordAdapter.notifyDataSetChanged();
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
    public void getSearchResult(String name1) {
        mTvHead.setText("正在努力搜索中");
        RequestParams params = new RequestParams(HttpUrlUtils.searchResult);
        params.addBodyParameter("name", name1);
        params.addBodyParameter("rows", "9");
        params.addBodyParameter("page", mCurrPage + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                entity = HttpUrlUtils.parseJason(result, SearchResultEntity.class);
                if (entity.getData().getTotal() > 0) {
                    mTvHead.setText("总共搜索结果为：" + entity.getData().getTotal() + "条");
                    if (mResultDataList == null) {
                        mResultDataList = entity.getData().getResults();
                    } else {
                        if (isClear){
                            mResultDataList.clear();
                        }
                        mResultDataList.addAll(entity.getData().getResults());
                    }
                } else {
                    mTvHead.setText("没有相关结果~~");
                    mResultDataList.clear();
                }
                mResultAdapter.setList(mResultDataList);
                mResultAdapter.notifyDataSetChanged();
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滚动到底部
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                ) {
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                mCurrPage++;
                isClear = false;
                getSearchResult(name);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
