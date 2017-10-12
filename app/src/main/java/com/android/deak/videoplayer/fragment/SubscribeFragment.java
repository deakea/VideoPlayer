package com.android.deak.videoplayer.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.deak.videoplayer.MyVideoPlayerActivity;
import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.adapter.SubscribeAdapter;
import com.android.deak.videoplayer.application.MyApplication;
import com.android.deak.videoplayer.db.VideoChildDb;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 订阅收藏页面
 */
public class SubscribeFragment extends BaseFragment {
    private GridView mGridView;
    private List<VideoChildDb> mDataList;
    private SubscribeAdapter mAdapter;
    @Override
    public int getResourcesId() {
        return R.layout.subscribe_fragment;
    }

    @Override
    public void init() {
        mGridView = (GridView) getActivity().findViewById(R.id.gv_subscribe_fragemnt);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MyVideoPlayerActivity.class);
                intent.putExtra("id",mDataList.get(position).getId()+"");
                intent.putExtra("name",mDataList.get(position).getName());
                intent.putExtra("cover",mDataList.get(position).getCover());
                intent.putExtra("upInfo",mDataList.get(position).getUpInfo()+"");
                startActivity(intent);
            }
        });
    }

    @Override
    public void afterInit() {
        mDataList = new ArrayList<>();
        getData();

    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        DbManager db = x.getDb(MyApplication.sDaoConfig);
        try {
            if (db.selector(VideoChildDb.class).findAll() == null){
                mDataList = new ArrayList<>();
            }else{
                mDataList = db.selector(VideoChildDb.class).findAll();
                mAdapter = new SubscribeAdapter(getActivity(),mDataList);
                mGridView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
