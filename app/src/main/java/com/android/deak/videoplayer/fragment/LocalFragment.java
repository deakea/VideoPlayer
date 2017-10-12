package com.android.deak.videoplayer.fragment;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.deak.videoplayer.MyPlayerActivity;
import com.android.deak.videoplayer.R;
import com.android.deak.videoplayer.adapter.LocalVideoAdapter;
import com.android.deak.videoplayer.entity.VideoEntity;
import com.android.deak.videoplayer.manager.VideoManager;

import java.util.List;

/**
 * 本地视频
 */
public class LocalFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvDel;
    private TextView mTvScan;
    private TextView mTvCheckAll;
    private TextView mTvNoContent;
    private GridView mGridView;
    private List<VideoEntity> mDataList;

    @Override
    public int getResourcesId() {
        return R.layout.local_fragment;
    }

    @Override
    public void init() {
        mTvDel = (TextView) getActivity().findViewById(R.id.tv_local_delete);
        mTvScan = (TextView) getActivity().findViewById(R.id.tv_local_scan);
        mTvCheckAll = (TextView) getActivity().findViewById(R.id.tv_local_check_all);
        mTvNoContent = (TextView) getActivity().findViewById(R.id.tv_local_no_content);
        mGridView = (GridView) getActivity().findViewById(R.id.gv_local_fragemnt);
    }

    @Override
    public void afterInit() {
        mTvScan.setOnClickListener(this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MyPlayerActivity.class);
                intent.putExtra("path",mDataList.get(position).getPath());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_local_scan:
                getData();
                break;
        }
    }

    private void getData() {
        VideoManager videoManager = new VideoManager(getActivity());
         mDataList = videoManager.searchVideo();
        if (mDataList.size() > 0){
            mTvNoContent.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            LocalVideoAdapter mAdapter = new LocalVideoAdapter(mDataList, getActivity());
            mGridView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else {
            mTvNoContent.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
        }
    }
}
