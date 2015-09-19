package com.example.shady.shady.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shady.shady.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shady on 2015/9/19.
 */
public class StaggeredAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private LayoutInflater mInflater;
    private Context mContext;
    protected List<String> mDatas;
    private List<Integer> mHeights;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }
    public StaggeredAdapter(Context context, List<String> datas){
        this.mContext=context;
        this.mDatas=datas;
        mInflater=LayoutInflater.from(context);
        mHeights=new ArrayList<Integer>();
        for (int i=0;i<mDatas.size();i++){
            mHeights.add((int) (100+Math.random()*300));
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.item_single_textview,viewGroup,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        ViewGroup.LayoutParams lp=myViewHolder.itemView.getLayoutParams();
        lp.height=mHeights.get(i);
        myViewHolder.itemView.setLayoutParams(lp);
        myViewHolder.tv.setText(mDatas.get(i));
        setUpItemEvent(myViewHolder);
    }

    protected void setUpItemEvent(final MyViewHolder myViewHolder) {
        if (mOnItemClickListener!=null){
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition=myViewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(myViewHolder.itemView,layoutPosition);
                }
            });
            //longclick
            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition=myViewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(myViewHolder.itemView, layoutPosition);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(int pos){
        mDatas.add(pos,"Insert One");
        notifyItemInserted(pos);
    }

    public void deleteData(int pos){
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView tv;
    public MyViewHolder(View itemView) {
        super(itemView);
        tv= (TextView) itemView.findViewById(R.id.tv);
    }
}
