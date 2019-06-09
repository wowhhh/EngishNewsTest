package com.example.wyb.work1.MainScreen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wyb.work1.MainScreen.News.NewsNoPhoto;
import com.example.wyb.work1.MainScreen.News.NewsWithPhoto;
import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;
import com.example.wyb.work1.MainScreen.NewsAdapter.MyDecoration;
import com.example.wyb.work1.MainScreen.NewsAdapter.NoPhotoApater;
import com.example.wyb.work1.MainScreen.NewsDetail.NewsDetail;
import com.example.wyb.work1.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by wyb on 2018/3/22.
 */

public class FragmentTest2  extends BaseFragment{


    public ArrayList<NewsNoPhoto> newslist =new ArrayList<>();
    private Handler handler;
    private RecyclerView mRecyclerView;
    private NoPhotoApater msportsNewsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //关联fragment与recycleview

        if(rootView==null) {
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_two, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview2);
        }



        return rootView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if(isVisible)
        {
            //爬取新闻之前先清空，防止因为刷新而引起的数据重复
            newslist.clear();

            getnews();
            handler=new Handler()
            {
                @Override
                public void handleMessage(Message msg) {
              if (msg.what==1)
              {

                  //在加载之前清楚之前的所有显示数据
                  mRecyclerView.removeAllViews();

                  msportsNewsAdapter=new NoPhotoApater(newslist);
                  mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
               //   mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
                  mRecyclerView.setAdapter(msportsNewsAdapter);

                  //为recycleview添加hederview和foterview
                  setHeaderView(mRecyclerView);
                  setFooterView(mRecyclerView );

                  //画分割线
                  mRecyclerView.addItemDecoration(new MyDecoration(getContext()));


                  //点击事件
                  msportsNewsAdapter.setOnMyItemClickers(new OnMyItemClickers() {
                      @Override
                      public void onItemClick(View view, int postion) {
                          //当点击header和footer的时候的判断
                          if(postion==0)
                          {
                              Toast.makeText(getContext(),"header",Toast.LENGTH_SHORT).show();
                              return;
                          }
                          if(postion==newslist.size()+1)
                          {
                              Toast.makeText(getContext(),"加载（还没开发出来）",Toast.LENGTH_SHORT).show();
                              return;
                          }



                          //获取点击位置的新闻内容,因为有头和尾部，所以说位置差一位
                          NewsNoPhoto SportsNews=newslist.get(postion-1);
                          //显示新闻的窗体
                          Intent intent=new Intent(getContext(), NewsDetail.class);
                          //将新闻连接传进去
                          intent.putExtra("news_url",SportsNews.getNewsUrl());
                         try {

                             startActivity(intent);

                         }
                         catch (Exception e)
                         {
                             e.printStackTrace();
                             Log.e("跳转窗体错误提示:",e.toString());
                         }
                      }
                  });
              }
              if(msg.what==11)
              {
                  Toast.makeText(getContext(),"XXX",Toast.LENGTH_SHORT).show();
              }
                }
            };
        }
    }

    //设置header
    private  void setHeaderView(RecyclerView view)
    {
        View header=LayoutInflater.from(getContext()).inflate(R.layout.header,view,false);
        msportsNewsAdapter.setHeaderView(header);

    }

    //设置header
    private  void setFooterView(RecyclerView view)
    {
        View Footer=LayoutInflater.from(getContext()).inflate(R.layout.footer,view,false);
        msportsNewsAdapter.setFooterView(Footer);
    }

    //提取新闻的方法
    private  void getnews()
    {


        new Thread (new Runnable() {
            @Override
            public  void run() {
                try
                {
                    int test=1;


                    //获取20页新闻
                    for(int i=1;i<2;i++)
                    {

                        // Document doc=Jsoup.connect("https://voice.hupu.com/nba"+Integer.toString(i)).get();
                        Document doc= Jsoup.connect("https://voice.hupu.com/nba/"+Integer.toString(i)).get();

                        Elements titlelinks=doc.select("div.list-hd");//解析每条新闻的连接和地址
                        Elements timelinks=doc.select("div.otherInfo");//解析每天新闻的时间和来源

                        Log.e("title",Integer.toString(titlelinks.size()));
                        //for循环遍历获取到每条新闻的四个数据并封装到news实体类中
                        for(int j=0;j<titlelinks.size();j++)
                        {
                            String title = titlelinks.get(j).select("a").text();
                            String uri = titlelinks.get(j).select("a").attr("href");
                            //   String desc = descLinks.get(j).select("span").text();
                            String time = timelinks.get(j).select("span.other-left").select("a").text();
                            NewsNoPhoto news = new NewsNoPhoto(title,uri,time);
                            newslist.add(news);
                        }

                    }
                    Thread.sleep(100);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                catch(Exception e){
                    e.printStackTrace();
                    Message msg=new Message();
                    msg.what=11;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

}
