package com.example.wyb.work1;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by wyb on 2018/4/21.
 */


public class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    //声明一个布局
    private GridLayoutManager mGridLayoutManager;
    //
    //当前页，从0开始    private int currentPage = 0;
    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;



    public EndLessOnScrollListener(GridLayoutManager mGridLayoutManager) {
        this.mGridLayoutManager = mGridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mGridLayoutManager.getItemCount();
        firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();
        if (loading) {
            //Log.d("wnwn","firstVisibleItem: " +firstVisibleItem);
            //Log.d("wnwn","totalPageCount:" +totalItemCount);
            //Log.d("wnwn", "visibleItemCount:" + visibleItemCount)；

            if (totalItemCount > previousTotal) {
                //说明数据已经加载结束
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        //这里需要好好理解
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
           // currentPage++;
            //onLoadMore(currentPage);
            loading = true;
        }

    }
}