package com.android.deak.videoplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.entity.KeyWordEntity;
import com.android.deak.videoplayer.widget.AutofitTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 关键词Adapter
 */
public class KeyWordAdapter extends BaseAdapter {
    private List<String> mDataList;
    private Context mContext;
    private Holder holder;

    public KeyWordAdapter(Context mContext) {
        this.mContext = mContext;
        mDataList = new ArrayList<>();
    }
    public void setList(List<String> mDataList){
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_keyword,null);
            holder = new Holder();
            holder.mKeyWord = (AutofitTextView) convertView.findViewById(R.id.tv_search_keyword);
            convertView.setTag(holder);
        }else{
            convertView.getTag();
        }
        holder.mKeyWord.setText(mDataList.get(position));
        return convertView;
    }
    class Holder{
        AutofitTextView mKeyWord;
    }
}
