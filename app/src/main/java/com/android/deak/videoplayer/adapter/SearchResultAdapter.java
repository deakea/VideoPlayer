package com.android.deak.videoplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.application.MyApplication;
import com.android.deak.videoplayer.entity.SearchResultEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果adapter
 */
public class SearchResultAdapter extends BaseAdapter {
    private List<SearchResultEntity.DataBean.ResultsBean> mDataList;
    private Context mContext;
    private Holder mHolder;

    public SearchResultAdapter(Context mContext) {
        this.mContext = mContext;
        mDataList = new ArrayList<>();
    }

    public void setList(List<SearchResultEntity.DataBean.ResultsBean> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public SearchResultEntity.DataBean.ResultsBean getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_search, null);
            mHolder = new Holder();
            mHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_item_listview_search_title);
            mHolder.mTvGrade = (TextView) convertView.findViewById(R.id.tv_item_listview_search_grade);
            mHolder.mTvStatus = (TextView) convertView.findViewById(R.id.tv_item_listview_search_status);
            mHolder.mTvNumber = (TextView) convertView.findViewById(R.id.tv_item_listview_search_upinfo);
            mHolder.mTvBrief = (TextView) convertView.findViewById(R.id.tv_item_listview_search_brief);
            mHolder.mIvCover = (ImageView) convertView.findViewById(R.id.iv_item_search_rerult);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        mHolder.mTvTitle.setText(mDataList.get(position).getTitle());
        mHolder.mTvGrade.setText(mDataList.get(position).getScore() + "");
        if (mDataList.get(position).isFinish()) {
            mHolder.mTvStatus.setText("已完结");
            mHolder.mTvStatus.setTextColor(0xFF61EA6B);
            mHolder.mTvNumber.setText("共"+mDataList.get(position).getUpInfo()+"集");
        }else {
            mHolder.mTvStatus.setText("连载中");
            mHolder.mTvStatus.setTextColor(0xFFB126D4);
            mHolder.mTvNumber.setText("已更新至"+mDataList.get(position).getUpInfo()+"集");
        }
        mHolder.mTvBrief.setText(mDataList.get(position).getBrief());
        ImageLoader.getInstance().displayImage(mDataList.get(position).getVerticalUrl()
                ,mHolder.mIvCover
                , MyApplication.sImageOptions);
        return convertView;
    }

    class Holder {
        TextView mTvTitle;
        TextView mTvGrade;
        TextView mTvStatus;
        TextView mTvBrief;
        TextView mTvNumber;
        ImageView mIvCover;
    }
}
