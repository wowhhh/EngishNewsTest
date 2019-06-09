package com.example.wyb.work1.MainScreen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wyb.work1.MainScreen.News.NewsNoPhoto;
import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;
import com.example.wyb.work1.MainScreen.NewsAdapter.MyDecoration;
import com.example.wyb.work1.MainScreen.NewsAdapter.NoPhotoApater;
import com.example.wyb.work1.MainScreen.NewsDetail.NewsDetail;
import com.example.wyb.work1.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by wyb on 2018/4/12.
 */

//用于匹配加载人民网英文版的科学板块内容
//2018/4/21  下拉刷新测试  测试成功，并且重写了BaseFragment 实现了懒加载
    //现在开始测试上拉加载
public class FragmentEnPeSc extends  BasetFragment_lazy {
    public ArrayList<NewsNoPhoto> EnScience=new ArrayList<>();
    private Handler handler;
    public RecyclerView recyclerView;
    public NoPhotoApater noPhotoApater;
    public SwipeRefreshLayout mRefreshLayout ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


         View  view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_enpesc, container, false);
            //关联recycleview
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_enpesc);
            mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
            //为下拉刷新添加时间
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    RefreshData();
                    mRefreshLayout.setRefreshing(false);
                }
            });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);

        //把加载数据写到第一次选中的情况
      /*  if(isVisible)
        {
            //加载数据，下拉刷新
          RefreshData();
        }*/
    }

    @Override
    protected void onFragmentFirstVisible() {
        //下载数据
        RefreshData();
    }

    //刷新数据所用
    public  void RefreshData()
    {
        //爬取之前先清空
        EnScience.clear();

        getNews();

        Log.e("hander.what",Integer.toString(233));
        handler =new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==2)
                {
                    //获取数据成功
                    //移除之前的数据
                    recyclerView.removeAllViews();

                    //recycleview三部曲
                    noPhotoApater=new NoPhotoApater(EnScience);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(noPhotoApater);

                    setHeaderView(recyclerView);
                    setFooterView(recyclerView);
                    recyclerView.addItemDecoration(new MyDecoration(getContext()));

                    //设置点击事件
                    noPhotoApater.setOnMyItemClickers(new OnMyItemClickers() {
                        @Override
                        public void onItemClick(View view, int postion) {
                            if(postion==0)
                            {

                                Toast.makeText(getContext(),"这里还没写",Toast.LENGTH_SHORT).show();
                                return;

                            }
                            if (postion==EnScience.size()+1)
                            {
                                Toast.makeText(getContext(),"这里是加载",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            NewsNoPhoto EnSc=EnScience.get(postion-1);

                            String url="http://en.people.cn/"+EnSc.getNewsUrl();
                            Intent intent=new Intent(getContext(), NewsDetail.class);
                            intent.putExtra("news_url",url);

                            startActivity(intent);
                        }
                    });

                }
                if(msg.what==22)
                {
                    Toast.makeText(getContext(),"加载错误",Toast.LENGTH_SHORT).show();

                }
            }
        };

    }


    //设置header
    private  void setHeaderView(RecyclerView view)
    {
        View header=LayoutInflater.from(getContext()).inflate(R.layout.header,view,false);
        noPhotoApater.setHeaderView(header);

    }

    //设置header
    private  void setFooterView(RecyclerView view)
    {
        View Footer=LayoutInflater.from(getContext()).inflate(R.layout.footer,view,false);
        noPhotoApater.setFooterView(Footer);
    }

    private  void getNews()
    {
        //提取新闻的方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Document doc= Jsoup.connect("http://en.people.cn/202936/index.html").get();
                    Elements pagnews=doc.select("div.d2p3_left");//解析这个页面的所有新闻
                    Elements newlist=pagnews.select("div.d2_17");//假设现在已经分条

                    Log.e("size",Integer.toString(newlist.size()));

                    for(int j=0;j<newlist.size();j++)
                    {
                        String time=newlist.get(j).select("div.on2").select("span").text();

                        String title=newlist.get(j).select("div.on1").text();
                        String url=newlist.get(j).select("a").attr("href");

                        NewsNoPhoto news=new NewsNoPhoto(title,url,time);
                        EnScience.add(news);


                    }

                    Message msg=new Message();
                    msg.what=2;
                    handler.sendMessage(msg);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg=new Message();
                    msg.what=22;
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }
}
