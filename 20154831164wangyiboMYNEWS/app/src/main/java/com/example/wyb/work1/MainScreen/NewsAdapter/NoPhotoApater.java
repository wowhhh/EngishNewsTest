package com.example.wyb.work1.MainScreen.NewsAdapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wyb.work1.R;

import java.util.ArrayList;

import com.example.wyb.work1.MainScreen.News.NewsNoPhoto;
import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;

/**
 * Created by wyb on 2018/3/22.
 */


//传入值，新闻标题，连接，新闻时间 为recycleView加载数据

public class NoPhotoApater extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //******加载新闻所用********
    private ArrayList<NewsNoPhoto> newsNoPhotos=new ArrayList<>();//用来存储新闻列表的，所有新闻内容
    private Context context;
    private OnMyItemClickers onMyItemClickers;  //点击时间
    //******加载新闻所用********

    //******设置header和footer所用
    public static  final  int TYPE_HEADER=0;//说明带header的
    public static  final  int TYPE_FOOTER=1;//说明带footer的
    public static  final  int TYPE_NORMAL=2;//说明不带header和footer的
    private View mHeaderView;
    private View mFooterView;

    //******设置header和footer所用



    //******初始化newsNoPhotos,构造函数******
    public  NoPhotoApater(ArrayList<NewsNoPhoto> newsNoPhotos)
    {
        this.newsNoPhotos=newsNoPhotos;

    }


    //newHolder里面的函数，用来确定加载的view

    /*******使用老版本的hoder使用的加载数据的方法
    @Override
        public NewsNoPhotoAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         //加载过程中 如果是 HeaderView FooterView 直接返回
        if(mHeaderView!=null&&viewType==TYPE_HEADER)
        {
            return new NewsNoPhotoAdapterHolder(mHeaderView,onMyItemClickers);
        }
        if(mFooterView!=null&&viewType==TYPE_FOOTER)
        {
            return  new NewsNoPhotoAdapterHolder(mFooterView,onMyItemClickers);
        }



           //加载新闻单个项目，news_no_photo_items用来显示单个的新闻内容  ,
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_no_photo_items,parent,false);
            NewsNoPhotoAdapterHolder holder=new NewsNoPhotoAdapterHolder(view,onMyItemClickers);

            return holder;
        }
*/

    //**********新版本加载所使用的viewholder 应该能用来加载holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView,onMyItemClickers);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ListHolder(mFooterView,onMyItemClickers);
            //返回mFooterView
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_no_photo_items, parent, false);
        return new ListHolder(layout,onMyItemClickers);
    }

        //********想舍弃哪一个复杂的holder,测试用，测试添加herder和footer
        class  ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            TextView item_title;
            TextView item_time;
            OnMyItemClickers mlistener;

            public ListHolder(View itemView,OnMyItemClickers listener)
            {
                super(itemView);

                this.mlistener=listener;

                itemView.setOnClickListener(this);
                //如果是headerview或者footerview，直接返回
                if(itemView==mHeaderView)
                {
                    return;
                }
                if(itemView==mFooterView)
                {
                    return;
                }

                item_title=(TextView)itemView.findViewById(R.id.newsTitle);
                item_time=(TextView)itemView.findViewById(R.id.newsTime);
            }

            @Override
            public void onClick(View v) {
                mlistener.onItemClick(v,getPosition());
            }
        }


    //监听
    public void setOnMyItemClickers(OnMyItemClickers onMyItemClickers)
    {
        this.onMyItemClickers=onMyItemClickers;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ListHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                ((ListHolder) holder).item_title.setText(newsNoPhotos.get(position-1).getNewsTitle());
                ((ListHolder) holder).item_time.setText(newsNoPhotos.get(position-1).getNewsTime());

                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else if(getItemViewType(position)==TYPE_FOOTER){
            return;
        }

    }


    /**********
    //填充数据？
    @Override
    public void onBindViewHolder(NewsNoPhotoAdapterHolder holder, int position) {
        if(getItemViewType(position)==TYPE_NORMAL) {
            String titleTest = newsNoPhotos.get(position - 1).getNewsTitle();
            String timeTest = newsNoPhotos.get(position - 1).getNewsTime();

            holder.item_title.setText(titleTest);
            holder.item_time.setText(timeTest);
        }
        else
            if(getItemViewType(position)==TYPE_HEADER)
            {
                return;
            }
            else
            {
                return;
            }
    }
****/
//返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView

    @Override
    public int getItemCount() {

        if(mHeaderView == null && mFooterView == null){
            return newsNoPhotos.size();
        }else if(mHeaderView == null && mFooterView != null){
            return newsNoPhotos.size() + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return newsNoPhotos.size() + 1;
        }else {
            return newsNoPhotos.size() + 2;
        }

        //return newsNoPhotos.size();
    }

    //*****下面是添加header和footer所用的构造函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        //position位置添加一条数据
        notifyItemInserted(getItemCount()-1);
    }
    //****重写方法,判断item的类型，绑定不同的view

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView==null&&mFooterView==null)
        {
            return  TYPE_NORMAL;
        }
        if(position==0)
        {
            //第一个位置添加header
            return TYPE_HEADER;
        }
        if(position==getItemCount()-1)
        {
            //最后一个，加载footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }


    //**************************************
}
