package com.example.wyb.work1.MainScreen.NewsAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wyb.work1.R;

import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;

/**
 * Created by wyb on 2018/3/22.
已经弃用
 */



public class NewsNoPhotoAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



    //判断点击事件，
    TextView item_title;
    TextView item_time;
    View mHeaderView;
    View mFooterView;

    OnMyItemClickers myItemClickers;
    public NewsNoPhotoAdapterHolder(View itemview,OnMyItemClickers listener)
    {
        super(itemview);
        //如果是headerview或者是footerview直接返回


        this.myItemClickers=listener;
        //为标签添加点击事件
        itemview.setOnClickListener(this);
        item_title=(TextView)itemview.findViewById(R.id.newsTitle);
        item_time=(TextView)itemview.findViewById(R.id.newsTime);
    }

    @Override
    public void onClick(View v) {
        myItemClickers.onItemClick(v,getPosition());
    }
}
