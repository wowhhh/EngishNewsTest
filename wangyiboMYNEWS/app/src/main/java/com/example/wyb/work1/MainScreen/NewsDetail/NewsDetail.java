package com.example.wyb.work1.MainScreen.NewsDetail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.wyb.work1.MySqlConn.SetMysql;
import com.example.wyb.work1.R;

/**
 * Created by wyb on 2018/4/13.
 */


//处理直接给新闻连接的情况
public class NewsDetail extends AppCompatActivity {
    private  String news_url;
    private String news_title;
    WebView webView;

    //toolbar
    private  String title;

    //计算下看的时间
    private  long backStartTime,backEndTime;

    //插入用户浏览历史
    private int in_hi_ok=0;
    private Handler in_hi_hanlder; //7
    //插入用户收藏数据
    private int in_cl_ok=0;
    private Handler in_cl_hander;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        //获取新闻链接，上面传过来的
        news_url=getIntent().getStringExtra("news_url");
        news_title=getIntent().getStringExtra("news_title");
        //初始化toolbar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("测试");
        toolbar.setSubtitle("测试");


        //获取用户的用户名 目前 数据用户名标题和新闻链接 向news_collect插入数据
        SharedPreferences userSettings =this.getSharedPreferences("setting",0);
        //取出数据
        final String username=userSettings.getString("username","loss");


        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //判断是否存在用户登录，不登录就提示不能收藏
                if(username=="")
                {
                    Toast.makeText(getApplicationContext(),"请登录！！！",Toast.LENGTH_SHORT).show();
                    return  false;
                }

                switch (item.getItemId())
                {
                    case R.id.share:
                        Toast.makeText(getApplicationContext(),"会接入qq分享",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.collect:

                        Toast.makeText(getApplicationContext(),"正在添加到我的收藏",Toast.LENGTH_LONG).show();
                        adduserCollect(username,news_title,news_url);
                        //handler 判断是否插入成功
                        in_cl_hander=new Handler()
                        {
                            @Override
                            public void handleMessage(Message msg) {
                                if(msg.what==1)
                                {
                                    Toast.makeText(getApplicationContext(),"收藏添加成功",Toast.LENGTH_LONG).show();
                                }
                            }
                        };

                        break;
                    case R.id.setting:

                        Toast.makeText(getApplicationContext(),"可以设置字体大小",Toast.LENGTH_LONG).show();
                        break;
                }
                return  true;
            }
        });



        //***********获取历史信息加到数据库里面*********

        //获取用户的用户名

        //使用SharedPreferences
        //不是真正需要的时候关闭这个功能
       // /**

        if(username!="loss") {
            adduserHistory(username, news_title, news_url);
        }
        in_hi_hanlder=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==7)
                {
                    Toast.makeText(NewsDetail.this,"浏览历史保存成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(NewsDetail.this,"浏览历史保存失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        //*/
        //***********获取历史信息加到数据库里面*********

        //初始化窗体
        webView=(WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(news_url);

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载toobar的选项
        getMenuInflater().inflate(R.menu.menu_web,menu);
        return true;
    }

    private void adduserHistory(final String Username,final  String news_title,final String news_url)
    {
        //添加用户浏览历史，在线程里面跑
        new Thread(new Runnable() {
            @Override
            public void run() {
                SetMysql insert_history=new SetMysql();

                in_hi_ok=insert_history.insertUserHistory(Username,news_title,news_url);
                if(in_hi_ok!=0) {
                    Message msg = new Message();
                    msg.what = 7;
                    in_hi_hanlder.sendMessage(msg);

                     }

            }
        }).start();

    }

    //添加用户收藏数据到news_collect
    private void adduserCollect(final String Username,final String news_title,final  String news_url)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SetMysql insert_collect=new SetMysql();
                in_cl_ok=insert_collect.insertUserCollect(Username,news_title,news_url);
                //判断是否有插入，会返回受影响的行数
                if(in_cl_ok!=0)
                {
                    Message msg=new Message();
                    msg.what=1;
                    in_cl_hander.sendMessage(msg);
                }
            }
        }).start();
    }
}
