package com.example.shady.shady.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shady.shady.R;
import com.example.shady.shady.adapter.StaggeredAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shady on 2015/9/19.
 */
public class ImageFragment extends Fragment{
    private RecyclerView recyclerView;
    private List<String> mDatas;
    private StaggeredAdapter mAdapter;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.imagefragment, container, false);
        initDatas();
        initViews();
        mAdapter=new StaggeredAdapter(this.getActivity(),mDatas);
        recyclerView.setAdapter(mAdapter);
        //设置RecycleView的布局文件
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new StaggeredAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mAdapter.deleteData(position);
            }
        });
        return view;
    }

    private void initViews() {
        recyclerView= (RecyclerView) view.findViewById(R.id.id_recyclerView);
    }

    private void initDatas() {
        mDatas=new ArrayList<String>();
        for (int i='A';i<='z';i++){
            mDatas.add(""+(char)i);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
