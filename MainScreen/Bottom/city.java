package com.example.wyb.work1.MainScreen.Bottom;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyb.work1.MainScreen.City.LocationUtils;
import com.example.wyb.work1.MainScreen.Home.MyImageView;
import com.example.wyb.work1.MainScreen.News.quote;
import com.example.wyb.work1.MainScreen.fragment.BaseFragment;
import com.example.wyb.work1.MainScreen.fragment.BasetFragment_lazy;
import com.example.wyb.work1.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by wyb on 2018/4/13.
 */

public class city extends BasetFragment_lazy {


    private MyImageView imageView;
    private TextView textView_title;
  //  private TextView textView_name;
    //
    private TextView city_name;
    private Context context;
    public quote quote_text;
    public Handler handler;
public Button button_next;
    private  Button button_pre;
    public ArrayList<quote>  quotesList=new ArrayList<>();

    //数据库哦
    private static final String DB_URL="jdbc:mysql://23.106.151.159/Android_myNews";
    private static final String user="News";
    private static final String pass="News";
    private Statement statement;
    private java.sql.Connection connection;
    private ResultSet resultSet;

    int i=0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city, container, false);
        textView_title=(TextView)view.findViewById(R.id.quote_text);
        imageView=(MyImageView)view.findViewById(R.id.quoet_image);
        //textView_name=(TextView)view.findViewById(R.id.quote_name);
        button_next=(Button)view.findViewById(R.id.quote_button);
        button_pre=(Button)view.findViewById(R.id.quotr_button2);




        //handler判断查到数据没
        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                //当然是获取成功啦
                if(msg.what==1)
                {
                    i=0;
                    quote_text=quotesList.get(i);
                    imageView.setImageURL(quote_text.getQuort_imageUrl());

                    button_pre.setEnabled(true);

                    button_next.setEnabled(true);
                    button_pre.setText("上一张");
                    button_next.setText("下一张");


                    // textView_name.setText(quote_text.getQuote_name());
                    textView_title.setText("    "+quote_text.getQuote_title()+"------"+quote_text.getQuote_name()+"");
                }
            }
        };

        //获取数据
        getquote_mysql objQuote=new getquote_mysql();
        objQuote.execute("");





        return view;
    }

    @Override
    protected void onFragmentFirstVisible() {



        //加载下一张
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if(i<=quotesList.size()-1)
                {
                    button_pre.setEnabled(true);
                    quote_text=quotesList.get(i);
                    imageView.setImageURL(quote_text.getQuort_imageUrl());
                    // textView_name.setText(quote_text.getQuote_name());
                    textView_title.setText("    "+quote_text.getQuote_title()+"----"+quote_text.getQuote_name()+"");
                }
                else
                {
                    i=i-1;//偷个懒
                    button_next.setEnabled(false);
                    Toast.makeText(getContext(),"已经是最后一张咯！！！",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        //加载上一张
        button_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                if(i>=0)
                {

                    button_next.setEnabled(true);
                    quote_text=quotesList.get(i);
                    imageView.setImageURL(quote_text.getQuort_imageUrl());
                    //textView_name.setText(quote_text.getQuote_name());
                    textView_title.setText("    "+quote_text.getQuote_title()+"----"+quote_text.getQuote_name()+"");


                }
                else
                {
                    i=i+1;
                    button_pre.setEnabled(false);
                    Toast.makeText(getContext(),"已经是第一张咯！！！",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }



    /* @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==666)
                {
                    quote_text=quotesList.get(0);
                    imageView.setImageURL(quote_text.getQuort_imageUrl());
                }
            }
        };
    }*/

   //使用另外一种线程，去读取数据库吧
   private class getquote_mysql extends AsyncTask<String,String,String>
   {

       String msg="";
       //做之前


       @Override
       protected void onPreExecute() {
           //作为加载提示
           button_next.setText("正在加载，请稍等");
           button_pre.setText("正在加载，请稍等");
           button_pre.setEnabled(false);
           button_next.setEnabled(false);
           super.onPreExecute();
       }

       //获取数据库里面的信息
       @Override
       protected String doInBackground(String... params) {
           try
           {

               Class.forName("com.mysql.jdbc.Driver");
               connection= DriverManager.getConnection(DB_URL,user,pass);
               if(connection==null)
           {
               Toast.makeText(getContext(),"quote数据库连接失败" ,Toast.LENGTH_SHORT).show();
           }
           else
           {
               statement=connection.createStatement();//创建statement对象
               String sql="select * from quote";
               resultSet=statement.executeQuery(sql);
               //遍历 读取到quotelist中
               while(resultSet.next())
               {
                   quote quote_mysql=new quote(resultSet.getString("quote_title"),resultSet.
                           getString("quote_author"),
                           resultSet.getString("quote_imageurl"));

                   quotesList.add(quote_mysql);
               }
               msg="ok";

               Message msg_handler=new Message();
               msg_handler.what=1;
               handler.sendMessage(msg_handler);
           }
           }
           catch (Exception e)
           {

               e.printStackTrace();
               msg=e.toString();
           }
           finally {
               try {
                   if (connection != null) {
                       connection.close();
                   }
                   if (statement != null) {
                       statement.close();
                   }
                   if (resultSet != null) {
                       resultSet.close();
                   }
               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }
           }
           return msg;
       }

       @Override
       protected void onPostExecute(String s) {

           //加载
          // quote_text=quotesList.get(i);
          // String test=quote_text.getQuort_imageUrl();
          // imageView.setImageURL(quote_text.getQuort_imageUrl());

       }
   }


}

