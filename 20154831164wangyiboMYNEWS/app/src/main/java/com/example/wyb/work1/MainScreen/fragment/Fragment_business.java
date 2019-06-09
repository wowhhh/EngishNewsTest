package com.example.wyb.work1.MainScreen.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyb.work1.MainScreen.Adapter.SubscribeTopLayoutAdapter;
import com.example.wyb.work1.MainScreen.GetChinaDailyNews;
import com.example.wyb.work1.MainScreen.Home.MyImageView;
import com.example.wyb.work1.MainScreen.Home.header_viewpager;
import com.example.wyb.work1.MainScreen.News.NewsWithPhoto;
import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;
import com.example.wyb.work1.MainScreen.NewsAdapter.MyDecoration;
import com.example.wyb.work1.MainScreen.NewsAdapter.WithPhotoApater;
import com.example.wyb.work1.MainScreen.NewsDetail.NewsDetail;
import com.example.wyb.work1.MySqlConn.SetMysql;
import com.example.wyb.work1.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wyb on 2018/4/17.
 */

//用于加载商业新闻，换布局    20188/4/7/17  加入缓存图片功能，在本代码中创建缓存目录，并且线程中缓存图片  实验失败
    //图片命名方式可以是标题
    //2018/4/21  目前在第三个fragemnt 中实验懒加载以及下拉刷新
public class Fragment_business extends  BasetFragment_lazy  implements  ViewPager.OnPageChangeListener{
    //上方轮播图  handler和循环播放
    private ViewPager BusinViewPager;
    private TextView BusinText;
    private ArrayList<ImageView> mImagesList;//轮播图图片
    private List<String> imagesTitle;//轮播标题集合
    public int previouspositon;//当前页面
    boolean isStop=false;
    private ArrayList<String> imageurls=new ArrayList<>();//轮播新闻地址
    private ArrayList<NewsWithPhoto> newsViewPagerList=new ArrayList<>();

    private  MyImageView iv;
    private SubscribeTopLayoutAdapter adapter_top;
//分割线
    private ArrayList<NewsWithPhoto> newsPhotoList=new ArrayList<>();
    private Handler handler2;//用来匹配爬取下方新闻的线程
    private RecyclerView recyclerView;
    private WithPhotoApater withPhotoApater;
//原始进度条
    public ProgressDialog pd;
    //下拉刷新
    public SwipeRefreshLayout  mswipeRefreshLayout;
    private boolean mIsRefreshing=false;//解决刷新bug

    //插入用户浏览历史
    private int in_hi_ok=0;
    private Handler in_hi_hanlder; //7
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_business, container, false);
            //关联recycleview 以及上方控件
            recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_business);
            BusinViewPager=(ViewPager)view.findViewById(R.id.tech_viewpager);
            BusinText=(TextView)view.findViewById(R.id.tech_text);
        //
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mIsRefreshing)
                {
                    return true;
                }
                else
                {
                    return  false;
                }
            }
        });
        //当刷新时设置
        //mIsRefreshing=true;
        //刷新完毕后还原为false
        //mIsRefreshing=false;


        //关联下拉刷新
        mswipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.Tech_DownRedresh);

            //****  上方轮播图
            initview();
            setFirstLocation();

            //***

        final SwipeRefreshLayout.OnRefreshListener refreshListener=new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新一下重新获取一页新闻
                mIsRefreshing=true;
                GetTechNews();
                mswipeRefreshLayout.setRefreshing(false);
                mIsRefreshing=false;
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
        //mswipeRefreshLayout.onRefresh();

        //获取上方新闻  handler控制线程
            //实验效果，哇  太卡啦
          /*  getViewPagerNews();
            handler3=new Handler()
            {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==4)
                    {
                        initview();
                        setFirstLocation();
                    }
                    else
                    {
                        Log.e("提示","上方新闻出错");
                    }
                }
            };*/

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);

        //选中之后开始加载这个fragment
        /*if(isVisible)
        {
            GetTechNews();
        }*/

    }

    //第一次点击时加载


    @Override
    protected void onFragmentFirstVisible() {

    }


    //获取新闻数据
    public void GetTechNews()
    {
        //final ProgressDialog pd=ProgressDialog.show(getActivity() ,"Teachnolory","Loading...");
        //爬取之前先清空，以免下次选中之后重复记载

        newsPhotoList.clear();
        final GetChinaDailyNews getCDNews=new GetChinaDailyNews();
        //分开是为了后面可能会用到 简单的刷新操作
            /*newsPhotoList= getCDNews.getNews("http://www.chinadaily.com.cn/business/tech/"+"page_"+1+".html");


            //根据请求数据是否成功 进行加载数据
            if(newsPhotoList.size()!=0)
            {

            }
            else
            {
                Message msg=new Message();
                msg.what=33;
                handler.sendMessage(msg);

            }*/

        getNews();


        //弃用handler
        handler2=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                //获取新闻数据成功
                if(msg.what==3)
                {   //移除之前所有数据
                    recyclerView.removeAllViews();
                    withPhotoApater=new WithPhotoApater(newsPhotoList);

                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    recyclerView.setAdapter(withPhotoApater);


                    //初始化加载头和尾部
                    //setHeaderView(null);
                    setFooterView(recyclerView);
                    recyclerView.addItemDecoration(new MyDecoration(getContext()));



                    //设置点击事件
                    withPhotoApater.setOnMyItemClickers(new OnMyItemClickers() {
                        @Override
                        public void onItemClick(View view, int postion) {
                               /*if(postion==0)
                               {

                                   Toast.makeText(getContext(),"这里还没写",Toast.LENGTH_SHORT).show();
                                   return;

                               }*/
                            if (postion==newsPhotoList.size())
                            {
                                Toast.makeText(getContext(),"这里是加载", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            NewsWithPhoto business=newsPhotoList.get(postion);

                            Toast.makeText(getContext(),business.getNew_url(),Toast.LENGTH_SHORT).show();

                            //下面开始写。technology界面的点击时间，跳转到看新闻的界面
                            NewsWithPhoto TechNews=newsPhotoList.get(postion);

                            String url=TechNews.getNew_url();
                            String title=TechNews.getNew_title();
                            Intent intent=new Intent(getContext(), NewsDetail.class);
                            intent.putExtra("news_url",url);
                            intent.putExtra("news_title",title);
                            startActivity(intent);
                        }
                    });

                }
                else
                {
                    Toast.makeText(getContext(),"加载数据失败",Toast.LENGTH_SHORT).show();

                }
            }
        };


    }



    //设置header
    private  void setHeaderView(RecyclerView view)
    {
        View header=LayoutInflater.from(getContext()).inflate(R.layout.item_topnews,view,false);
        withPhotoApater.setmHeaderView(header);

    }

    //设置header
    private  void setFooterView(RecyclerView view)
    {
        View Footer=LayoutInflater.from(getContext()).inflate(R.layout.footer,view,false);
        withPhotoApater.setmFooterView(Footer);
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
                         doc = Jsoup.connect("http://www.chinadaily.com.cn/business/tech").get();
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

                            newsPhotoList.add(newsWithPhoto);
                        }
                    }

                    Log.e("商业新闻数据：",Integer.toString(newsPhotoList.size()) );

                    Message msg=new Message();
                    msg.what=3;
                    handler2.sendMessage(msg);




                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Message msg=new Message();
                    msg.what=33;
                    handler2.sendMessage(msg);
                }

            }
        }).start();
    }

    /**
     轮播第三步：
     * 第三步：给viewpager设置轮播监听器
     * viewpager的监听器
     * 当ViewPager页面被选中时, 触发此方法.
     * @param position 当前被选中的页面的索引
     *
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //伪无线循环，滑到最后一张又进入第一张
        int newposition =position % mImagesList.size();
        //切换当前选中的内容i
        BusinText.setText(imagesTitle.get(newposition));
        //赋值给前一个变量，方便下次操作
        previouspositon=newposition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {

    }



    /**
     轮播第一步
     初始化数据：现在是固定的数据和图片，当使用网络的时候，把这个初始化数据修改成匹配爬取下来的数据。

     */
    public void  initdata()
    {



        int[]  imagesIDs={
            R.drawable.image_test1,
            R.drawable.image_test2,
            R.drawable.image_test3,
            R.drawable.image_test4
    };
        mImagesList=new ArrayList<>();
        imagesTitle=new LinkedList<>() ;


        for(int i=0;i<imagesIDs.length;i++)
        {
            iv=new MyImageView(getContext());
            iv.setBackgroundResource(imagesIDs[i]);
            mImagesList.add(iv);


            //初始化每张图片下面的额文字
            /*imagesTitle=new String[]
                    {
                            "不好好",
                            "帮客户包括",
                            "四六级啊",
                            "打车废弃物"
                    };
                    */
        }
        imagesTitle.add("Before and after: Syrian war and its devastation");
        imagesTitle.add("Walk down memory lane: Xi's 40-year-old ties with Hainan");
        imagesTitle.add("Snow turns Yichun into white fairyland");
        imagesTitle.add("Latest development of domestically-built aircraft carrier");

        imageurls.add("http://www.chinadaily.com.cn/a/201804/17/WS5ad537dda3105cdcf6518a01.html");
        imageurls.add("http://www.chinadaily.com.cn/a/201804/17/WS5ad53123a3105cdcf65189ed_6.html");
        imageurls.add("http://www.chinadaily.com.cn/a/201804/17/WS5ad569f9a3105cdcf6518c70.html");
        imageurls.add("http://www.chinadaily.com.cn/a/201804/17/WS5ad56b26a3105cdcf6518c7c.html");

    }

    private  void initview()
    {
        initdata();
         adapter_top=new SubscribeTopLayoutAdapter(mImagesList,BusinViewPager,imagesTitle,imageurls);
        BusinViewPager.setAdapter(adapter_top);/**轮播第二步 设置适配器*/
        BusinViewPager.addOnPageChangeListener(this);

        handler.sendEmptyMessageDelayed(0,2000);
    }
    /**
     轮播第四步：设置刚打开显示的图片和文字
     */
    private  void setFirstLocation()
    {
        BusinText.setText(imagesTitle.get(previouspositon));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / 2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImagesList.size();

        int test=Integer.MAX_VALUE;
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        BusinViewPager.setCurrentItem(currentPosition);

    }

    /**
     * 轮播第五步：设置自动播放，跳转
     */
    //利用handler完成自动播放

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            BusinViewPager.setCurrentItem(BusinViewPager.getCurrentItem()+1);
            if(!isStop)
            {
                handler.sendEmptyMessageDelayed(0,2000);
            }
        }
    };

    /**
     * 第六步: 当Activity销毁时取消图片自动播放
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        isStop=true;
    }

}
