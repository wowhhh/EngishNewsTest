package com.example.wyb.work1.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wyb.work1.MainScreen.GetChinaDailyNews;
import com.example.wyb.work1.MainScreen.News.NewsWithPhoto;
import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;
import com.example.wyb.work1.MainScreen.NewsAdapter.MyDecoration;
import com.example.wyb.work1.MainScreen.NewsAdapter.WithPhotoApater;
import com.example.wyb.work1.MainScreen.NewsDetail.NewsDetail;
import com.example.wyb.work1.MainScreen.fragment.BasetFragment_lazy;
import com.example.wyb.work1.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import static com.example.wyb.work1.R.id.container;
import static java.security.AccessController.getContext;

/**
 * Created by wyb on 2018/4/22.
 */


//加载新闻版块新闻，我好像记得在调用的这个窗体不能用线程，先试试吧
public class Companies extends AppCompatActivity {
    private Handler handler;
    private ArrayList<NewsWithPhoto> newsComList=new ArrayList<>();
    private RecyclerView recyclerView;

    private WithPhotoApater ComPhotoApater;

    //下拉刷新
    public SwipeRefreshLayout mswipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_companies);
        //关联recycleview
        recyclerView = (RecyclerView)this.findViewById(R.id.recyclerview_companies);



        //关联下拉刷新
        mswipeRefreshLayout=(SwipeRefreshLayout)this.findViewById(R.id.Tech_DownRedresh);


        final SwipeRefreshLayout.OnRefreshListener refreshListener=new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新一下重新获取一页新闻
                GetComNews();
                mswipeRefreshLayout.setRefreshing(false);
            }
        };

        mswipeRefreshLayout.setOnRefreshListener(refreshListener);
 /*
        *来自博客：
        * 使用 RecyclerView 加官方下拉刷新的时候，如果绑定的 List 对象在更新数据之前进行了 clear，
        * 而这时用户紧接着迅速上滑 RV，就会造成崩溃，而且异常不会报到你的代码上，属于RV内部错误。
        * 初次猜测是，当你 clear 了 list 之后，
        * 这时迅速上滑，而新数据还没到来，导致 RV 要更新加载下面的 Item 时候，找不到数据源了，造成 crash
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mswipeRefreshLayout.setRefreshing(true);
                refreshListener.onRefresh();
            }
        },100);

    }

    //设置header
    private  void setFooterView(RecyclerView view)
    {
        View Footer=LayoutInflater.from(this).inflate(R.layout.footer,view,false);
        ComPhotoApater.setmFooterView(Footer);
    }
    //获取新闻数据
    public void GetComNews()
    {
        //final ProgressDialog pd=ProgressDialog.show(getActivity() ,"Teachnolory","Loading...");
        //爬取之前先清空，以免下次选中之后重复记载

        newsComList.clear();
        final GetChinaDailyNews getCDNews=new GetChinaDailyNews();

        getNews();


        //弃用handler
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                //获取新闻数据成功
                if(msg.what==3)
                {   //移除之前所有数据
                    ComPhotoApater=new WithPhotoApater(newsComList);

                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                    recyclerView.setAdapter(ComPhotoApater);


                    //初始化加载头和尾部
                    //setHeaderView(null);
                    setFooterView(recyclerView);
                    recyclerView.addItemDecoration(new MyDecoration(getApplicationContext()));



                    //设置点击事件
                    ComPhotoApater.setOnMyItemClickers(new OnMyItemClickers() {
                        @Override
                        public void onItemClick(View view, int postion) {

                            if (postion==newsComList.size())
                            {
                                Toast.makeText(getApplicationContext(),"这里是加载", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            NewsWithPhoto business=newsComList.get(postion);

                            Toast.makeText(getApplicationContext(),business.getNew_url(),Toast.LENGTH_SHORT).show();

                            //下面开始写。technology界面的点击时间，跳转到看新闻的界面
                            NewsWithPhoto TechNews=newsComList.get(postion);

                            String url=TechNews.getNew_url();
                            Intent intent=new Intent(getApplicationContext(), NewsDetail.class);
                            intent.putExtra("news_url",url);

                            startActivity(intent);
                        }
                    });


                    //移除进度条

                    //        pd.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"加载数据失败",Toast.LENGTH_SHORT).show();
                    //          pd.dismiss();
                }
            }
        };


    }



    //爬取新闻
    public void getNews()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc=null;
                    try {
                        doc = Jsoup.connect("http://www.chinadaily.com.cn/business/companies").get();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    //爬取页面所有新闻
                    Elements news=doc.select("div.main_art");
                    //将新闻分条
                    Elements newslist=doc.select("div.mb10");
                    //循环提取每一条新闻的，时间，标题，链接，图片链接
                    for (int i=0;i<newslist.size();i++)
                    {
                        String title=newslist.get(i).select("a").text();
                        String time=newslist.get(i).select("b").text();
                        String url=newslist.get(i).select("a").attr("href");
                        String image_url=null;
                        //因为有的新闻没有图片
                        try
                        {
                            image_url=newslist.get(i).select("img").attr("src");
                        }
                        catch (Exception image_e)
                        {
                            image_e.printStackTrace();
                        }

                        //如果新闻没有图片的话就不添加吧
                        if(image_url=="")
                        {
                            //NewsWithPhoto newsWithPhoto=new NewsWithPhoto(title,time,url);
                            //newsPhotoList.add(newsWithPhoto);
                            //测试

                        }
                        else
                        {
                            NewsWithPhoto newsWithPhoto=new NewsWithPhoto(title,time,url,image_url);

                            newsComList.add(newsWithPhoto);
                        }
                    }

                    Log.e("商业新闻数据：",Integer.toString(newsComList.size()) );

                    Message msg=new Message();
                    msg.what=3;
                    handler.sendMessage(msg);




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg=new Message();
                    msg.what=33;
                    handler.sendMessage(msg);
                }

            }
        }).start();
    }
}
