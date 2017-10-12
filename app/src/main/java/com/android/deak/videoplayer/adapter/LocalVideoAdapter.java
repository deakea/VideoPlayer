package com.android.deak.videoplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.application.MyApplication;
import com.android.deak.videoplayer.entity.VideoEntity;
import com.android.deak.videoplayer.utils.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 本地视频结果adapter
 */
public class LocalVideoAdapter extends BaseAdapter {
    private List<VideoEntity> mDataList;
    private Context mContext;
    private VideoHolder mHolder;

    public LocalVideoAdapter(List<VideoEntity> mDataList, Context mContext) {
        this.mDataList = mDataList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public VideoEntity getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_local_video,null);
            mHolder = new VideoHolder();
            mHolder.mTvTiele = (TextView) convertView.findViewById(R.id.tv_item_local_name);
            mHolder.mTvTime = (TextView) convertView.findViewById(R.id.tv_item_local_time);
            mHolder.mIvAlbum = (ImageView) convertView.findViewById(R.id.iv_item_local_album);
            convertView.setTag(mHolder);
        }else {
            mHolder = (VideoHolder) convertView.getTag();
        }
       mHolder.mIvAlbum.setImageBitmap(mDataList.get(position).getBitmap());
        mHolder.mTvTiele.setText(mDataList.get(position).getTitle());
        mHolder.mTvTime.setText(CommonUtils.formatDuration(mDataList.get(position).getDuration()));
        return convertView;
    }
    class VideoHolder{
        ImageView mIvAlbum;
        TextView mTvTiele;
        TextView mTvTime;
    }
}
