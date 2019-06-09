package com.example.wyb.work1.MainScreen.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by wyb on 2018/4/11.
 */

//作用是为首页里面的fragment加载适配器

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> titlelist;
    private ArrayList<Fragment> fragmentArrayList;
    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<String> titlelist, ArrayList<Fragment> fragmentslist)
    {
        super(fm);
        this.titlelist=titlelist;
        this.fragmentArrayList=fragmentslist;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }

}
