package com.example.wyb.work1.MainScreen.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.wyb.work1.MainScreen.fragment.BasetFragment_lazy;
import com.example.wyb.work1.MySqlConn.GetConn;
import com.example.wyb.work1.R;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by wyb on 2018/4/25.
 */

public class MyHistory  extends AppCompatActivity{
    private RecyclerView recyclerView_history;
    private NoPhotoApater history_apater;
    private ArrayList<NewsNoPhoto> historylist=new ArrayList<>();
    //
    private ArrayList<NewsNoPhoto> collectlist=new ArrayList<>();
    //
    private Handler handler;
    ProgressDialog pd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_history);
        //初始化控件
        recyclerView_history = (RecyclerView) findViewById(R.id.recyclerview_history);


        //取出数据(用户名)
        SharedPreferences sharedPreferences = getSharedPreferences("setting", 0);
        String name = sharedPreferences.getString("username", "");
        //获取判断上一个activity是要求显示什么内容的
        String getType = getIntent().getStringExtra("getType");


        if(getType.equals(""))
        {

        }

        //获取历史消息
        //  pd.show(MyHistory.this,"Login","正在匹配历史记录。。。请稍等");

        if (getType.equals("history")) {
            Toast.makeText(getApplicationContext(), "获取历史信息中。。。请稍等。。。", Toast.LENGTH_LONG).show();
            getHistory(name);
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {

                        //    pd.dismiss();
                        //适配器加载数据
                        recyclerView_history.removeAllViews();
                        history_apater = new NoPhotoApater(historylist);
                        recyclerView_history.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        recyclerView_history.setAdapter(history_apater);
                        setHeaderView(recyclerView_history);
                        setFooterView(recyclerView_history);
                        recyclerView_history.addItemDecoration(new MyDecoration(getApplicationContext()));
                        history_apater.setOnMyItemClickers(new OnMyItemClickers() {
                            @Override
                            public void onItemClick(View view, int postion) {
                                if (postion==historylist.size())
                                {
                                    Toast.makeText(getApplicationContext(),"这里是加载", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                NewsNoPhoto business=historylist.get(postion);

                                Toast.makeText(getApplicationContext(),business.getNewsUrl(),Toast.LENGTH_SHORT).show();

                                //下面开始写。technology界面的点击时间，跳转到看新闻的界面
                                NewsNoPhoto TechNews=historylist.get(postion);

                                String url=TechNews.getNewsUrl();
                                String title=TechNews.getNewsUrl();
                                Intent intent=new Intent(getApplicationContext(), NewsDetail.class);
                                intent.putExtra("news_url",url);
                                intent.putExtra("news_title",title);
                                startActivity(intent);
                            }
                        });

                        Toast.makeText(getApplicationContext(), "获取历史信息成功", Toast.LENGTH_SHORT).show();

                    }
                    if(msg.what==11)
                    {
                        Toast.makeText(getApplicationContext(), "获取历史信息失败，手动返回qaq", Toast.LENGTH_SHORT).show();
                    }
                }
            };

        }
        //获取收藏信息
        if(getType.equals("collect"))
        {

            Toast.makeText(getApplicationContext(), "获取收藏信息中。。。请稍等。。。", Toast.LENGTH_LONG).show();
            getCollect(name);
            handler=new Handler()
            {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==2)
                    {
                        recyclerView_history.removeAllViews();
                        history_apater = new NoPhotoApater(collectlist);
                        recyclerView_history.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        recyclerView_history.setAdapter(history_apater);
                        setHeaderView(recyclerView_history);
                        setFooterView(recyclerView_history);
                        recyclerView_history.addItemDecoration(new MyDecoration(getApplicationContext()));
                        history_apater.setOnMyItemClickers(new OnMyItemClickers() {
                            @Override
                            public void onItemClick(View view, int postion) {
                                if (postion==collectlist.size())
                                {
                                    Toast.makeText(getApplicationContext(),"这里是加载", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                NewsNoPhoto business=collectlist.get(postion);

                                Toast.makeText(getApplicationContext(),business.getNewsUrl(),Toast.LENGTH_SHORT).show();

                                //下面开始写。technology界面的点击时间，跳转到看新闻的界面
                                NewsNoPhoto TechNews=collectlist.get(postion);

                                String url=TechNews.getNewsUrl();
                                String title=TechNews.getNewsUrl();
                                Intent intent=new Intent(getApplicationContext(), NewsDetail.class);
                                intent.putExtra("news_url",url);
                                intent.putExtra("news_title",title);
                                startActivity(intent);

                            }
                        });

                        Toast.makeText(getApplicationContext(), "获取收藏信息成功", Toast.LENGTH_SHORT).show();

                    }
                }
            };

        }
    }

    //设置header
    private  void setHeaderView(RecyclerView view)
    {
        View header=LayoutInflater.from(getApplicationContext()).inflate(R.layout.header,view,false);
        history_apater.setHeaderView(header);

    }

    //设置header
    private  void setFooterView(RecyclerView view)
    {
        View Footer=LayoutInflater.from(getApplicationContext()).inflate(R.layout.footer,view,false);
        history_apater.setFooterView(Footer);
    }

    public void getHistory(final String name)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetConn conn=new GetConn();
                historylist = conn.getHistory_news(name);
                if(historylist.size()!=0)
                {
                 //发送查询成功的消息
                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
                else
                {
                    Message msg=new Message();
                    msg.what=11;
                    handler.sendMessage(msg);

                }

            }
        }).start();

    }
    //开线程连接数据库获取用户收藏消息
    public void getCollect(final String name)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetConn conn=new GetConn();
                collectlist=conn.getCollect_news(name);
                if(collectlist.size()!=0)
                {
                    //发送查询收藏信息成功的信息
                    Message msg=new Message();
                    msg.what=2;
                    handler.sendMessage(msg);
                }
                else
                {
                    Message msg=new Message();
                    msg.what=22;
                    handler.sendMessage(msg);

                }

            }
        }).start();
    }

}
