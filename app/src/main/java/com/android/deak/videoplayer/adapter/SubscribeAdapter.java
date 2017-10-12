package com.android.deak.videoplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.application.MyApplication;
import com.android.deak.videoplayer.db.VideoChildDb;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * 已收藏adapter
 */
public class SubscribeAdapter extends BaseAdapter {
    private Context mContext;
    private List<VideoChildDb> mDataList;
    private SubscribeHolder mHolder;

    public SubscribeAdapter(Context mContext, List<VideoChildDb> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public VideoChildDb getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_subscribe_gridview, null);
            mHolder = new SubscribeHolder();
            mHolder.iv_ico = (ImageView) convertView.findViewById(R.id.iv_item_subscribe);
            mHolder.tv_currNumber = (TextView) convertView.findViewById(R.id.tv_item_subscribe_curr_number);
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_subscribe_title);
            mHolder.btn_cancel = (Button) convertView.findViewById(R.id.btn_item_subscribe_cancel);
            convertView.setTag(mHolder);
        } else {
            mHolder = (SubscribeHolder) convertView.getTag();
        }
        mHolder.tv_title.setText(mDataList.get(position).getName());
        mHolder.tv_currNumber.setText("已更新至" + mDataList.get(position).getUpInfo() + "集");
        ImageLoader.getInstance().displayImage(mDataList.get(position).getCover(), mHolder.iv_ico, MyApplication.sImageOptions);
        final String id = mDataList.get(position).getId();
        mHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DbManager db = x.getDb(MyApplication.sDaoConfig);
                    db.deleteById(VideoChildDb.class, id);
                    mDataList.remove(position);
                  notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    class SubscribeHolder {
        TextView tv_title;
        TextView tv_currNumber;
        ImageView iv_ico;
        Button btn_cancel;
    }
}
