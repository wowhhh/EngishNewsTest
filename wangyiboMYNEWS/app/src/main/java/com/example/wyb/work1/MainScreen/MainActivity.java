package com.example.wyb.work1.MainScreen;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.wyb.work1.MainScreen.Adapter.MyApaterTest;
import com.example.wyb.work1.MainScreen.fragment.FragmentTest2;
import com.example.wyb.work1.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //初步测试是直接加载布局
    //当加入用户功能时候，一个表可以用来保存用户订阅的频道，初始时候可以读取加载这些频道

    private ArrayList<Fragment> fragments;  //提取保存用户订阅频道或者默认频道
    private ArrayList<String>   mTitleList=new ArrayList<>();//保存用户订阅频道的标题

    private TabLayout tlMain;
    private ViewPager vpMain;
    private MyApaterTest apaterTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initTitleList();
        initFragment();
        initview();

        apaterTest=new MyApaterTest(getSupportFragmentManager(),mTitleList,fragments);
        vpMain.setAdapter(apaterTest);
        vpMain.setOffscreenPageLimit(3);
        tlMain.setupWithViewPager(vpMain);


        /*

        //创建一个SwipeRefreshLayout对象
        mswipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.layout_swipe_refresh);

        mswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        */
    }

    //初始化标题
    private  void initTitleList()
    {
        mTitleList.add("测试");mTitleList.add("测试2");
    }
    //初始化fragment 与相关的java代码关联
    private  void initFragment()
    {
        fragments=new ArrayList<>();

        fragments.add(new FragmentTest2());

    }
    //初始化控件
    private  void initview() {
   tlMain=(TabLayout)findViewById(R.id.Tl_NewsItem);
        vpMain=(ViewPager)findViewById(R.id.vpMain1);
    }

}
