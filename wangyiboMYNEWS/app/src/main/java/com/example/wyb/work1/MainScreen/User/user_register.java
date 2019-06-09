package com.example.wyb.work1.MainScreen.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyb.work1.MainActivity;
import com.example.wyb.work1.MySqlConn.GetConn;
import com.example.wyb.work1.MySqlConn.SetMysql;
import com.example.wyb.work1.R;

/**
 * Created by wyb on 2018/5/31.
 */

public class user_register extends AppCompatActivity {
    private Button back;
    private TextView register_name;
    private TextView register_pass;
    private TextView register_pass2;
    private Button submit;
    //判断注册成功
    private Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //用于用户注册，注册成功后自动登录
        back=(Button)findViewById(R.id.register_back);
        register_name=(TextView)findViewById(R.id.register_name);
        register_pass=(TextView)findViewById(R.id.register_password);
        register_pass2=(TextView)findViewById(R.id.register_password_sure);
        submit=(Button) findViewById(R.id.register_complete);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一个activity
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断
                final String name=register_name.getText().toString();
                String pass=register_pass.getText().toString();
                String pass2=register_pass2.getText().toString();
                if(!pass.equals(pass2) || name==null) {
                    Toast.makeText(getApplicationContext(),"两次输入不一致！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(name.trim().equals("") || pass.trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"不能为空！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                submit.setEnabled(false);
                //插入值并登录
                boolean register_succ=register(name,pass);
               handler=new Handler(){
                   @Override
                   public void handleMessage(Message msg) {
                       if(msg.what==1)
                       {
                           Toast.makeText(getApplicationContext(),"注册成功，开始跳转",Toast.LENGTH_SHORT).show();
                           //直接跳转到mainactivity
                           Intent intent=new Intent(user_register.this, MainActivity.class);
                           //使用SharedPreferences
                           SharedPreferences userSettings =getSharedPreferences("setting",0);
                           //setting处于可编辑的状态
                           SharedPreferences.Editor editor=userSettings.edit();
                           //存放数据
                           editor.putString("username",name);
                           editor.commit();
                           // pd.dismiss();

                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           startActivity(intent);

                       }
                       else
                       {
                           Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                           submit.setEnabled(true);
                       }
                   }
               };
            }
        });

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public boolean register(final String name, final String pass)
    {        new Thread(new Runnable() {
            @Override
            public void run() {
                SetMysql register=new SetMysql();
             if( register.registeruser(name,pass))
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
        return false;
    }
}
