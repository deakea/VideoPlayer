package com.android.deak.videoplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.application.MyApplication;
import com.android.deak.videoplayer.entity.AllEntity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 *GridView item
 */
public class MyGridViewAdapter extends BaseAdapter {
    private List<AllEntity.DataBean.ResultsBean> mDataList;
    private Context mContext;
    private Holder holder;

    public MyGridViewAdapter( Context mContext) {
        this.mContext = mContext;
        mDataList = new ArrayList<>();
    }
    public void setList(List<AllEntity.DataBean.ResultsBean> mDataList){
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public AllEntity.DataBean.ResultsBean getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview,null);
            holder.iv_ico = (ImageView) convertView.findViewById(R.id.iv_item_gridview);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_gridview_title);
            holder.tv_currNumber = (TextView) convertView.findViewById(R.id.tv_item_gridview_curr_number);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        AllEntity.DataBean.ResultsBean entity = mDataList.get(position);
        ImageLoader.getInstance().displayImage(entity.getVerticalUrl()//封面
                , holder.iv_ico
                , MyApplication.sImageOptions//加载图片的选项、配置文件
        );
        holder.tv_title.setText(entity.getTitle());
        holder.tv_currNumber.setText("更新至第"+entity.getUpInfo()+"集");
        return convertView;
    }
    class Holder{
        TextView tv_title;
        TextView tv_currNumber;
        ImageView iv_ico;
    }
}
