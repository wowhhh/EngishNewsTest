package com.example.wyb.work1.MainScreen.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by wyb on 2018/3/22.
 */


//主要功能是加载主页面的tablayout以及fragment（目前加载首页的这些都放在了home里，所以适配器用的是myFragmnetpageradapter）
public class MyApaterTest extends FragmentPagerAdapter{

    //定义数组用来读取从主界面传进来的标题栏和fragment
    public ArrayList<String> titlelist;
    public ArrayList<Fragment> fragmentslist;

    //初始化，获取相应的值和fragment
    public MyApaterTest(FragmentManager fm, ArrayList<String> titlelist, ArrayList<Fragment> fragmentslist )
    {
        super(fm);
        this.titlelist=titlelist;
        this.fragmentslist=fragmentslist;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentslist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentslist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }
}
