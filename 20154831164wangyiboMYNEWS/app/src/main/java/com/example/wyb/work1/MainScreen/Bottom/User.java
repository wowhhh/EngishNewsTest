package com.example.wyb.work1.MainScreen.Bottom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyb.work1.MainScreen.User.MyHistory;
import com.example.wyb.work1.MainScreen.User.user_login;
import com.example.wyb.work1.MainScreen.fragment.BasetFragment_lazy;
import com.example.wyb.work1.MySqlConn.SetMysql;
import com.example.wyb.work1.R;

/**
 * Created by wyb on 2018/4/12.
 */


//用来匹配布局文件。加载页面
    //2018/4/25  登录基本功能实现进程：实现了用户在界面登录的过程，然后
public class User extends BasetFragment_lazy {

    private Button button_login;//登录
    private Button button_loginout;//退出登录
    private Button button_history;//历史记录
    private Button button_collect;//用户收藏
    private Button button_creat;//创表
    private String loginback_name="";
    //private TextView textView_userInfo;

    //用于建表线程
    private Handler handler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //退出登录的点击事件
    public Button.OnClickListener onClickListener_loginout=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sp=getActivity().getSharedPreferences("setting",0);
            SharedPreferences.Editor editor=sp.edit();
            editor.clear();
            editor.commit();
            Toast.makeText(getContext(),"退出登录成功",Toast.LENGTH_SHORT).show();
            //清楚用户的登录显示信息

      //      textView_userInfo.setText("登录享受更多体验");
            button_login.setEnabled(true);
            button_login.setText("登录");
            button_loginout.setEnabled(false);//设置不可退出登录
            button_history.setEnabled(false); //设置不可查看浏览历史
            button_collect.setEnabled(false);//设置不可查看收藏

        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user,container,false);

        button_login=(Button)view.findViewById(R.id.button_login);
        //textView_userInfo=(TextView)view.findViewById(R.id.textview_userinfo);
        button_loginout=(Button)view.findViewById(R.id.button_loginout);
        button_history=(Button)view.findViewById(R.id.button_history);
        button_collect=(Button)view.findViewById(R.id.button_collect);
       button_creat=(Button)view.findViewById(R.id.creatTable);
        //登录事m件
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到登录界面
                Intent intent_login=new Intent(getContext(), user_login.class);
                startActivity(intent_login);
            }
        });

        //退出登录点击事件
        button_loginout.setOnClickListener(onClickListener_loginout);
        //创表点击事件
        button_creat.setOnClickListener(onClickListener_crearTable);
        //历史记录，跳转
        button_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_history=new Intent(getContext(), MyHistory.class);
                intent_history.putExtra("getType","history");//用于传入到显示窗体，给下一个activity判断获取什么数据
                startActivity(intent_history);
            }
        });


        //收藏信息，跳转
        button_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_collect=new Intent(getContext(),MyHistory.class);
                intent_collect.putExtra("getType","collect");//用于传入到下一个显示信息的activity
                startActivity(intent_collect);
            }
        });
        //初始化判断是否存在用户登录
        if(loginback_name!="")
        {
          //  textView_userInfo.setText(loginback_name);
            button_login.setEnabled(false);
            button_login.setText(loginback_name);
            button_loginout.setEnabled(true);//设置可以退出登录
            button_history.setEnabled(true); //设置可以查看浏览历史
        }
        else
        {
            Toast.makeText(getContext(),"登录享受更多体验撒",Toast.LENGTH_SHORT).show();
            button_loginout.setEnabled(false);//设置不可退出登录
            button_history.setEnabled(false); //设置不可查看浏览历史
            button_collect.setEnabled(false);//设置不可查看收藏

        }
       return view;
       // return super.onCreateView(inflater, container, savedInstanceState);
    }
    //创表键
    public Button.OnClickListener onClickListener_crearTable=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //定义多条sql
            String sql_history=" CREATE TABLE `news_history` (\n" +
                    "  `ID` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `Username` varchar(20) NOT NULL,\n" +
                    "  `news_title` varchar(100) NOT NULL,\n" +
                    "  `news_url` varchar(100) NOT NULL,\n" +
                    "  PRIMARY KEY (`ID`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 \n";
            String sql_collect="CREATE TABLE `news_collect` (\n" +
                    "  `ID` int(6) NOT NULL,\n" +
                    "  `Username` varchar(20) NOT NULL,\n" +
                    "  `news_title` varchar(100) NOT NULL,\n" +
                    "  `news_url` varchar(100) NOT NULL\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4\n";
            String sql_quote="\n" +
                    "CREATE TABLE `quote` (\n" +
                    "  `ID` int(8) NOT NULL,\n" +
                    "  `quote_url` varchar(100) NOT NULL,\n" +
                    "  `quote_title` varchar(100) NOT NULL,\n" +
                    "  `quote_author` varchar(20) NOT NULL,\n" +
                    "  `quote_imageurl` varchar(100) NOT NULL,\n" +
                    "  PRIMARY KEY (`ID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='保存quote项目'\n" +
                    "\n";
            String sql_subscribe_top="" +
                    "\n" +
                    "CREATE TABLE `subscribe_top` (\n" +
                    "  `ID` int(8) NOT NULL,\n" +
                    "  `news_title` varchar(100) NOT NULL,\n" +
                    "  `news_url` varchar(100) NOT NULL,\n" +
                    "  `news_image_url` varchar(100) NOT NULL,\n" +
                    "  PRIMARY KEY (`ID`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订阅界面上方轮播图'\n" +
                    "\n";
            String sql_user="CREATE TABLE `user` (\n" +
                    "  `Username` varchar(20) NOT NULL,\n" +
                    "  `Password` varchar(20) NOT NULL,\n" +
                    "  PRIMARY KEY (`Username`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
         creattable(sql_collect,sql_history,sql_quote,sql_subscribe_top,sql_user);
            handler=new Handler()
            {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==666)
                    {
                        Toast.makeText(getContext(),"建表成功!",Toast.LENGTH_SHORT).show();
                    }
                }
            };
        }
    };

    //不可行  问题原因，从aticity不太容易传值到fragment
    //解决办法：在登录界面，干脆直接跳转到mainactivtiy,然后获取用户名显示就好了

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //使用SharedPreferences
        SharedPreferences userSettings =getContext().getSharedPreferences("setting",0);

        //取出数据
        String name=userSettings.getString("username","");
        loginback_name=name;
        if(loginback_name!="") {
            Toast.makeText(getContext(), name + "登录成功", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected boolean isFragmentVisible() {
        if(isVisible())
        {
            //如果被选中,这里的被选中主要是想设置为，用户登录成功后的返回，可以接受返回的值，然后判断是否为空，

            Log.e("登录返回：","yes");
            //String loginback=getIntent;

        }
        return super.isFragmentVisible();
    }
    public void creattable(final String sql1,final String sql2,final String sql3,final String sql4,final String sql5)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"正在创建!",Toast.LENGTH_SHORT).show();
                SetMysql creat=new SetMysql();
                creat.execreat(sql1);
                creat.execreat(sql2);
                creat.execreat(sql3);
                creat.execreat(sql4);
                creat.execreat(sql5);
                Message msg=new Message();
                msg.what=666;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
