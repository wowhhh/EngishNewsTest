package com.example.wyb.work1.MainScreen.Home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyb.work1.MainScreen.Adapter.SubscribeTopLayoutAdapter;
import com.example.wyb.work1.MainScreen.fragment.BaseFragment;
import com.example.wyb.work1.R;

import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by wyb on 2018/4/17.
 */

public class header_viewpager extends BaseFragment implements  ViewPager.OnPageChangeListener {

    //上方轮播图  handler和循环播放
    private ViewPager headerViewPager;
    private TextView headerText;
    private ArrayList<ImageView> mImagesList;//轮播图图片
    private List<String> imagesTitle;//轮播标题集合
    public int previouspositon;//当前页面
    boolean isStop=false;

    private ScheduledExecutorService scheduledExecutorService;
    private static int PAGER_TIOME = 5000;//间隔时间


    //上方轮播图


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.header,container,false);
           //初始化控件
           headerViewPager=(ViewPager)view.findViewById(R.id.header_viewPager);
           headerText=(TextView) view.findViewById(R.id.header_text);
        initview();
        setFirstLocation();

        return view;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if(isVisible)
        {


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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //伪无线循环，滑到最后一张又进入第一张
        int newposition =position % mImagesList.size();
        //切换当前选中的内容i
        headerText.setText(imagesTitle.get(newposition));
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
                R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3,
                R.drawable.image4
        };
        mImagesList=new ArrayList<>();
        imagesTitle=new LinkedList<>() ;
        ImageView iv;
        for(int i=0;i<imagesIDs.length;i++)
        {
            iv=new ImageView(getContext());
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
        imagesTitle.add("第一张");
        imagesTitle.add("第二张");
        imagesTitle.add("第三张");
        imagesTitle.add("第四张");

    }

    private  void initview()
    {
        initdata();
        SubscribeTopLayoutAdapter adapter1=new SubscribeTopLayoutAdapter(headerViewPager,mImagesList,imagesTitle);
        headerViewPager.setAdapter(adapter1);/**轮播第二步 设置适配器*/
        headerViewPager.addOnPageChangeListener(this);

        handler.sendEmptyMessageDelayed(0,2000);
    }
    /**
     轮播第四步：设置刚打开显示的图片和文字
     */
    private  void setFirstLocation()
    {
        headerText.setText(imagesTitle.get(previouspositon));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / 2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImagesList.size();

        int test=Integer.MAX_VALUE;
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        headerViewPager.setCurrentItem(currentPosition);

    }

    /**
     * 轮播第五步：设置自动播放，跳转
     */
    //利用handler完成自动播放

    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            headerViewPager.setCurrentItem(headerViewPager.getCurrentItem()+1);
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
