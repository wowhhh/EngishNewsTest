package com.example.wyb.work1.MainScreen.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wyb.work1.MainActivity;
import com.example.wyb.work1.MySqlConn.GetConn;
import com.example.wyb.work1.R;

/**
 * Created by wyb on 2018/4/22.
 */

public class user_login  extends AppCompatActivity {

    //跳转关闭
    static Activity instance;
    //获取登录按钮
    public Button button_login;
    public Button button_register;
    //获取注册按钮
    public EditText username;
    public  EditText password;
    public  GetConn loginTF=new GetConn();

    //判断获取用户登录信息状态
    private Boolean loginback;
    private Handler handler;
    //定义登录按钮的点击事件
    ProgressDialog pd;
    public Button.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            LoginThread();
         //   pd.show(user_login.this,"Login","正在匹配用户信息。。。请稍等");
            Toast.makeText(getApplicationContext(),"正在匹配您的账号信息...",Toast.LENGTH_SHORT).show();
            handler=new Handler()
            {
                @Override
                public void handleMessage(Message msg) {
                  //如果信息存在
                    if(msg.what==1)
                  {

                      //传入信息
                      Toast.makeText(getApplicationContext(),"用户信息匹配成功",Toast.LENGTH_SHORT).show();
                      //直接跳转到mainactivity
                      Intent intent=new Intent(user_login.this, MainActivity.class);
                      //使用SharedPreferences
                      SharedPreferences userSettings =getSharedPreferences("setting",0);
                      //setting处于可编辑的状态
                      SharedPreferences.Editor editor=userSettings.edit();
                      //存放数据
                      editor.putString("username",username.getText().toString());
                      editor.commit();
                     // pd.dismiss();

                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      startActivity(intent);


                  }
                  else
                    {
                        Toast.makeText(getApplicationContext(),"用户信息匹配失败",Toast.LENGTH_SHORT).show();
                       // pd.dismiss();
                    }
                }
            };
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //初始化控件
        button_login = (Button) findViewById(R.id.sign_in_button);
        button_register = (Button) findViewById(R.id.register_in_button);
        username=(EditText) findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);

        //分别设置监听 登录监听
        button_login.setOnClickListener(onClickListener);
        //注册监听
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(getApplicationContext(), user_register.class);
                startActivity(intent2);
            }
        });

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public void LoginThread()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取用户输入信息，传入
              loginback=  loginTF.getUserInfo(username.getText().toString(),password.getText().toString());
                if(loginback==true)
                {
                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
                else
                {
                    Message msg=new Message();
                    msg.what=11;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
