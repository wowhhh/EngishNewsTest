package com.example.wyb.work1.MainScreen.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.wyb.work1.MainScreen.Home.MyImageView;
import com.example.wyb.work1.MainScreen.News.OnMyItemClickers;
import com.example.wyb.work1.MainScreen.NewsDetail.NewsDetail;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by wyb on 2018/4/14.
 */

public class SubscribeTopLayoutAdapter extends PagerAdapter implements View.OnClickListener {

    private List<ImageView> images;
    public ViewPager viewPager;
    public List<String> title;
    public List<String> url;
    public OnMyItemClickers onMyItemClickers;//用于监听用户的点击效果

    //以后写这个点击事件，跳转窗体直接在下面log的地方写 ,先把url依数组的形式传过来，然后，在点击时间中写


    /*
    构造方法，传入图片列表和viewpager
     */
public void setOnMyClickListener(OnMyItemClickers onMyClickListener)
{
    this.onMyItemClickers=onMyClickListener;
}

    public SubscribeTopLayoutAdapter(List<ImageView> images) {
        this.images = images;
    }

    public SubscribeTopLayoutAdapter(ViewPager viewPager,List<ImageView> images)
    {
        this.viewPager=viewPager;
        this.images=images;
    }
    public SubscribeTopLayoutAdapter(ViewPager viewPager,List<ImageView> images,List<String> title)
    {
        this.viewPager=viewPager;
        this.images=images;
        this.title=title;
    }

    //用了这个构造方法，哪个啥，url就能加载过来了，这样就能在这里实现跳转了  哈哈哈哈
    public SubscribeTopLayoutAdapter(List<ImageView> images, ViewPager viewPager, List<String> title, List<String> url) {
        this.images = images;
        this.viewPager = viewPager;
        this.title = title;
        this.url = url;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;//返回的值为顶部加载内容数量
    }


    /**
     * 判断是否使用缓存, 如果返回的是true, 使用缓存. 不去调用instantiateItem方法创建一个新的对象
     */

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    /*
    初始化一个条目
     */

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        //把position里面的内容相应的加载到 viewpager
        //MyImageView iv=images.get(position %images.size());
        ImageView iv=images.get(position %images.size());

        viewPager.addView(iv);

        //点击事件响应成功
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("标题",title.get(position%images.size()));
                Log.e("ok",Integer.toString(position%images.size()));
                Log.e("",url.get(position%images.size()));

                //太神奇了，一步步去获取viewpager的承载窗体
                Intent intent=new Intent(viewPager.getRootView().getContext(),NewsDetail.class);
                intent.putExtra("news_url",url.get(position%images.size()));

                viewPager.getRootView().getContext().startActivity(intent);
                //Toast.makeText(,"ok",Toast.LENGTH_LONG).show();
            }
        });


//返回当前的imageview
        return iv;
    }

    /*
    销毁条目:如果不销毁的话就会出现异常，log里面提示你销毁，注意不要越界
     */


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        viewPager.removeView(images.get(position%images.size()));
    }

    @Override
    public void onClick(View v) {
        onMyItemClickers.onItemClick(v,viewPager.getCurrentItem());
    }
}
