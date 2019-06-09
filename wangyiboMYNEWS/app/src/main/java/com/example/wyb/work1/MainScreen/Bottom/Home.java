package com.example.wyb.work1.MainScreen.Bottom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wyb.work1.MainScreen.Adapter.MyApaterTest;
import com.example.wyb.work1.MainScreen.Adapter.MyFragmentPagerAdapter;
import com.example.wyb.work1.MainScreen.Adapter.ViewPagerAdapter;
import com.example.wyb.work1.MainScreen.fragment.BaseFragment;
import com.example.wyb.work1.MainScreen.fragment.FragmentEnPeSc;
import com.example.wyb.work1.MainScreen.fragment.FragmentTest2;
import com.example.wyb.work1.MainScreen.fragment.Fragment_business;
import com.example.wyb.work1.R;

import java.util.ArrayList;

/**
 * Created by wyb on 2018/4/12.
 */


//实现首页内容的加载，实现tablyout标签的加载

/**
 * 首页的加载主要就是看新闻板块的加载，多个fragment嵌套recycleview，匹配相应的适配器
 */
public class Home extends BaseFragment  {


    private ArrayList<Fragment> fragments;  //提取保存用户订阅频道或者默认频道
    private ArrayList<String>   mTitleList=new ArrayList<>();//保存用户订阅频道的标题

    private TabLayout tlMain;
    private ViewPager vpMain;
    private MyApaterTest apaterTest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home,container,false);

        //初始化控件
        tlMain=(TabLayout)view.findViewById(R.id.tabNews);
        vpMain=(ViewPager)view.findViewById(R.id.vpNews);


        //加载顶部标题栏
        initTitleList();
        initFragment();

        MyFragmentPagerAdapter fragmentPagerAdapter=new MyFragmentPagerAdapter(getChildFragmentManager(),mTitleList,fragments);

        vpMain.setAdapter(fragmentPagerAdapter);

        vpMain.setOffscreenPageLimit(4);
        tlMain.setupWithViewPager(vpMain);




        return view;

       // return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);


    }

    //初始化标题
    private  void initTitleList()
    {
        mTitleList.add("Technology");mTitleList.add("测试2");mTitleList.add("商业");
    }
    //初始化fragment 与相关的java代码关联
    private  void initFragment()
    {
        fragments=new ArrayList<>();
        fragments.add(new Fragment_business());
        fragments.add(new FragmentTest2());
        fragments.add(new FragmentEnPeSc());

    }
    //初始化控件
    private  void initview() {


    }


}
