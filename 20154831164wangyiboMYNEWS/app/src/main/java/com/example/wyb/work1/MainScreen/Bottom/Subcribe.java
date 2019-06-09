package com.example.wyb.work1.MainScreen.Bottom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyb.work1.MainScreen.Adapter.SubscribeLayoutAdapter;
import com.example.wyb.work1.MainScreen.Adapter.SubscribeTopLayoutAdapter;
import com.example.wyb.work1.MainScreen.Home.MyImageView;
import com.example.wyb.work1.MainScreen.MainActivity;
import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;
import com.example.wyb.work1.MainScreen.News.TopNews;
import com.example.wyb.work1.MainScreen.fragment.BaseFragment;
import com.example.wyb.work1.MainScreen.fragment.BasetFragment_lazy;
import com.example.wyb.work1.MySqlConn.GetConn;
import com.example.wyb.work1.R;
import com.example.wyb.work1.Subscribe.Companies;
import com.example.wyb.work1.Subscribe.Fashion;

/**
 * Created by wyb on 2018/4/12.
 */


//加载显示订阅频道的布局

/**
 * 主要就是加载布局
 * 对于上面的布局：轮播图采用的是viewpager和相应的适配器，适配器里面就是对数据相应的填充适配viewpager，
 * 这里面写的主要就是数据的初始化，默认加载，自动播放。后面会采用网络图片，爬虫可以在这里写
 * 爬取图片,url，标题。传入适配器，点击事件在适配器中
 *
 * 对于下面的布局
 * （2018/4/14）仅仅实现了固定数量的加载以及点击事件，对于加载就是在recycleview采用限制性的布局
 * 然后在适配器中(recycleview)匹配布局文件，匹配每一个textview imageview。
 * 这里的点击事件移动到了这个后面的话就在这里写了。
 *
 * (2018/4/22) 轮播标题连接都给好了，就是还没解决图片加载的问题。就很烦
 * 同日 下午，，选择性放弃了轮播图。。。。。。开线程就卡，技术有限啊 啊啊啊 啊啊
 */
public class Subcribe extends BasetFragment_lazy implements  ViewPager.OnPageChangeListener {
    private Context context;

    /**
     * *****************************************************************************
     */
    //上方轮播图  handler和循环播放
    private ViewPager mSubViewPager;
    private TextView adText;
    private ArrayList<ImageView> mImagesList=new ArrayList<>();//轮播图图片
    private List<String> imagesTitle=new ArrayList<>();//轮播标题集合
    private ArrayList<String> imageURl=new ArrayList<>();//轮播图片地址
    public int previouspositon;//当前页面
    boolean isStop=false;

    private ScheduledExecutorService scheduledExecutorService;
    private static int PAGER_TIOME = 5000;//间隔时间
    private ArrayList<TopNews> topNewsArrayList;//获取数据库爬下来的四条新闻

    private ArrayList<String> Top_url=new ArrayList<>();

    private Handler handler_top;
    //上方轮播图
    /**
     * *****************************************************************************
     */

    //下方
    private ArrayList<String> mlist;
    private SubscribeLayoutAdapter adapter;
    private RecyclerView mRecyclerView;
    private ImageView imageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //setUserVisibleHint();

       View     view = inflater.inflate(R.layout.subscribe, container, false);

        /*
        加载下方内容
         */
       mlist=new ArrayList<>();
        mlist.add("Companies");//http://www.chinadaily.com.cn/business/companies
        mlist.add("Industries");//http://www.chinadaily.com.cn/business/biz_industries
        mlist.add("Health");//http://www.chinadaily.com.cn/life/health
        mlist.add("Books");//http://www.chinadaily.com.cn/culture/books
        mlist.add("Basketball");//http://www.chinadaily.com.cn/sports/basketball
        mlist.add("Art");//http://www.chinadaily.com.cn/culture/art
        mlist.add("China.Military");//http://www.chinadaily.com.cn/china/59b8d010a3108c54ed7dfc25
        mlist.add("Finance");//http://www.chinadaily.com.cn/business/money
        mlist.add("Fashion");//http://www.chinadaily.com.cn/life/fashion

        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycleview_subscribe);
        mSubViewPager=(ViewPager)view.findViewById(R.id.viewpager_subscribe);
        adText=(TextView)view.findViewById(R.id.ad_tv);

        // super.setUserVisibleHint(isVisibleToUser);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        //重新写个适配器，用来加载这个布局
        adapter = new SubscribeLayoutAdapter(mlist, context);
        mRecyclerView.setAdapter(adapter);

        //初始化顶部栏的两个控件


        // ImageView=(ImageView)view.findViewById(R.id)
        adapter.setOnMyClickListener(new OnMyItemClickers() {
            @Override
            public void onItemClick(View view, int postion) {
                //获取点击位置
                String text = mlist.get(postion);

                //判断点击位置，打开不同窗体
                switch (text)
                {
                    case "Companies":
                        Intent intent2=new Intent(getContext(), Companies.class);
                        startActivity(intent2);
                        return;
                    case "Fashion":
                        Intent intent3=new Intent(getContext(),Fashion.class);
                        startActivity(intent3);
                        return;

                }
                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
            }
        });

        //初始化的时候就走线程，去获取数据
        get_subTop subTop=new get_subTop();
        subTop.execute();


        return view;

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if(imagesTitle.size()!=0)
        {


        }
        else
        {
            Toast.makeText(getContext(),"订阅模块获取网络数据出错",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        if(imagesTitle.size()!=0)
        {
            //获取成功
            initview();

        }
        else
        {
            Toast.makeText(getContext(),"订阅模块获取网络数据出错",Toast.LENGTH_SHORT).show();
        }
    }



    /**不用啦 ，后面代码。
    *
     */

    //开线程，获取数据
    private class get_subTop extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {

            Log.e("Subscribe:","获取数据Pre");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            GetConn conn=new GetConn();


            topNewsArrayList=conn.getSubscribe_news();

            if(topNewsArrayList!=null)
            {
                for(int i=0;i<topNewsArrayList.size();i++)
                {
                    //读取单条新闻
                    TopNews Sub_top=topNewsArrayList.get(i);
                    String test_title=Sub_top.getTop_title();
                    imagesTitle.add(test_title);
                    Top_url.add(Sub_top.getTop_url());
                    imageURl.add(Sub_top.getTop_imageUrl());
                }

                //如果不出错的话现在已经数据获取完毕，两个想法，要么把加载轮播图放到这里，要么放到第一次选中（判断为不为空）

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }


    private  void initview()
    {
        initdata();

        SubscribeTopLayoutAdapter adapter1=new SubscribeTopLayoutAdapter(mImagesList,mSubViewPager,imagesTitle,Top_url);
        mSubViewPager.setAdapter(adapter1);/**轮播第二步 设置适配器*/
        mSubViewPager.addOnPageChangeListener(this);

        handler.sendEmptyMessageDelayed(0,2000);
    }


    /**
        轮播第一步
        初始化数据：现在是固定的数据和图片，当使用网络的时候，把这个初始化数据修改成匹配爬取下来的数据。

         */
    public void  initdata()
    {
        int[]  imagesIDs={
                R.drawable.sub_1,
                R.drawable.sub_2,
                R.drawable.sub_3,
                R.drawable.sub_4
        };


        MyImageView iv;
        for(int i=0;i<imagesIDs.length;i++)
        {
            iv=new MyImageView(getContext());
            iv.setBackgroundResource(imagesIDs[i]);
          //  iv.setImageURL(imageURl.get(i));
            mImagesList.add(iv);

        }

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
    public void onPageSelected(int position) {
        int text=4;

        //伪无线循环，滑到最后一张又进入第一张
      //  if(mImagesList.size()==8)
        {
       //     text=4;
       //     newposition=position % text;
        }
      //
            int newposition =position % mImagesList.size();


        //切换当前选中的内容i
        adText.setText(imagesTitle.get(newposition));
        //赋值给前一个变量，方便下次操作
        previouspositon=newposition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
    轮播第四步：设置刚打开显示的图片和文字
     */
     private  void setFirstLocation()
     {
         adText.setText(imagesTitle.get(previouspositon));
         // 把ViewPager设置为默认选中Integer.MAX_VALUE / 2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImagesList.size();

         int test=Integer.MAX_VALUE;
        int currentPosition = Integer.MAX_VALUE / 2 - m;
         mSubViewPager.setCurrentItem(currentPosition);

     }
    /**
     * 轮播第五步：设置自动播放，跳转
     */
    //利用handler完成自动播放

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            mSubViewPager.setCurrentItem(mSubViewPager.getCurrentItem()+1);
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




    /**
     * 上方轮播图点击事件  因为在使用轮播效果的时候，数据和图片等都是通过数组传入的，所以没有设置id，
     * 这里就考虑使用OnTouch来实现点击事件
     *
     * 目前没用到  把点击事件设置到适配器里面里，给了imageview
     */
    public void onTouchViewPager(ViewPager viewPager,final int position) {
        //给图片注册监听事件
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            private long downTime;
            private int downX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下去的时候，记录坐标和时间，判断是否为点击事件
                        handler.removeCallbacksAndMessages(null);//按下的时候，取消轮播
                        downX = (int) event.getX();
                        downTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:// 抬起手指时，判断落下抬起的时间差和坐标，符合以下条件为点击
                        // Toast.makeText(context, "手指抬起了", 0).show();
                        if (System.currentTimeMillis() - downTime < 500
                                && Math.abs(downX - event.getX()) < 30) {// ★考虑到手按下和抬起时的坐标不可能完全重合，这里给出30的坐标偏差
                            // 点击事件被触发
                            Toast.makeText(context, imagesTitle.get(position%mImagesList.size()), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    case MotionEvent.ACTION_CANCEL:
                        // ★写这个的目的为了让用户在手指滑动完图片后，能够让轮播图继续自动滚动
                        //startRoll();
                        break;
                }

                return true;
            }

        });
    }

}
