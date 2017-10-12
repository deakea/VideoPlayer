package com.android.deak.videoplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.entity.DetailsEntity;
import com.android.deak.videoplayer.widget.AutofitTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 集数apdater
 */
public class VideoEpisodeAdapter extends BaseAdapter {
    private Context mContext;
    private List<DetailsEntity.DataBean.SeasonBean.EpisodeBriefBean> mDataList;
    private Holder mHolder;

    public VideoEpisodeAdapter(Context mContext) {
        this.mContext = mContext;
        mDataList = new ArrayList<>();
    }
    public void setList(List<DetailsEntity.DataBean.SeasonBean.EpisodeBriefBean> mDataList){
        this.mDataList = mDataList;
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public DetailsEntity.DataBean.SeasonBean.EpisodeBriefBean getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            mHolder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_episode,null);
            mHolder.mCurrNumber = (AutofitTextView) convertView.findViewById(R.id.episode_tv);
            convertView.setTag(mHolder);
        }else {
            convertView.getTag();
        }
        mHolder.mCurrNumber.setText(mDataList.get(position).getEpisode());
        return convertView;
    }
    class Holder{
        AutofitTextView mCurrNumber;
    }
}
