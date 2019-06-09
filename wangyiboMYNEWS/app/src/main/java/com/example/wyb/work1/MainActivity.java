package com.example.wyb.work1;
import android.content.Context;
import android.net.VpnService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.wyb.work1.MainScreen.Adapter.ViewPagerAdapter;
import com.example.wyb.work1.MainScreen.Bottom.Home;
import com.example.wyb.work1.MainScreen.Bottom.Subcribe;
import com.example.wyb.work1.MainScreen.Bottom.User;
import com.example.wyb.work1.MainScreen.Bottom.city;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    ViewPager vpMain;
    ViewPagerAdapter adapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                   case R.id.navigation_home:
                    vpMain.setCurrentItem(0);
          //          mTextMessage.setText(R.string.title_home);
                    return true;

                case R.id.navigation_dashboard:
           //   订阅      mTextMessage.setText(R.string.title_dashboard);
                    vpMain.setCurrentItem(1);
                    return true;
                case R.id.navigation_city:
                    vpMain.setCurrentItem(2);
                    return true;
                case R.id.navigation_notifications:
                    vpMain.setCurrentItem(3);
             //       mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        getActionBar().hide();
        //获取控件 主页面的
        vpMain=(ViewPager)findViewById(R.id.vpMain222);
        //mTextMessage = (TextView) findViewById(R.id.message);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        //适配器
        adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home());
        adapter.addFragment(new Subcribe());
        adapter.addFragment(new city());

        adapter.addFragment(new User());

        vpMain.setAdapter(adapter);


        vpMain.setOffscreenPageLimit(4);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //为viewpager设置监听
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当viewpager滑动后设置选中的项
             //滑动出错，因为“我”的position是2，"city"的position是3

                int text=position;
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        {

        };
    }


}
