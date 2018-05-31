package com.example.wyb.work1.MySqlConn;

import com.example.wyb.work1.MainScreen.News.NewsNoPhoto;
import com.example.wyb.work1.MainScreen.News.NewsWithPhoto;
import com.example.wyb.work1.MainScreen.User.user_enity;
import com.example.wyb.work1.MainScreen.News.TopNews;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.wyb.work1.R.id.collect;
import static com.example.wyb.work1.R.id.username;

/**
 * Created by wyb on 2018/4/20.
 */
//获取用户的浏览历史信息
//获取订阅界面上方的数据库数据
//获取用户的用户名密码信息并且匹配是否存在，传输给登录界面。
public class GetConn  {

    //数据库哦
    private static final String DB_URL="jdbc:mysql://23.106.151.159/Android_myNews";
    private static final String user="News";
    private static final String pass="News";
    private java.sql.Connection connection;
    private   Statement statement;

    private user_enity user_enity;//用户登录信息
    private ArrayList<TopNews> subscribe_news=new ArrayList<>();
    private ArrayList<NewsNoPhoto> history_news=new ArrayList<>();

    private ArrayList<NewsNoPhoto> collect_news=new ArrayList<>();
    //获取订阅界面上方的数据库数据
    public ArrayList<TopNews> getSubscribe_news()
    {
        Statement statement_sub=getStatement();
        String sql="select * from subscribe_top";

        try {
            ResultSet rSet = statement_sub.executeQuery(sql);
            //遍历 读取到quotelist中
            while (rSet.next())
            {
                //获取单条
                TopNews Sub_news=new TopNews(rSet.getString("news_title"),
rSet.getString("news_url"),rSet.getString("news_image_url"));
                subscribe_news.add(Sub_news);
            }
            statement_sub.close();
            rSet.close();

            return subscribe_news;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    //获取用户的用户名密码信息并且匹配是否存在，传输给登录界面。
    public boolean getUserInfo(String username,String password)
    {

        Statement statement_user=getStatement();

        String sql="select * from user where Username= " +
                " '"+username+"' "+ " and Password= '"+password+"' ";
        try
        {
            ResultSet resultSet=statement_user.executeQuery(sql);
            if(resultSet.next())
            {
                statement_user.close();
                resultSet.close();
                connection.close();
                return true;
            }
            else
            {
                statement_user.close();
                resultSet.close();
                connection.close();
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return false;
    }
//获取用户的浏览历史信息
//获取订阅界面上方的数据库数据,根据用户名
public ArrayList<NewsNoPhoto> getHistory_news(String username)
{
    Statement statement_his=getStatement();
    String sql="select * from news_history where Username= '"+username+"'";

    try {
        ResultSet rSet = statement_his.executeQuery(sql);
        //遍历 读取到quotelist中
        while (rSet.next())
        {
            //获取单条
           NewsNoPhoto history=new NewsNoPhoto(rSet.getString("news_title"),rSet.getString("news_url"),"");
            history_news.add(history);
        }
        statement_his.close();
        rSet.close();

        return history_news;

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }

    return null;
}
//获取用户的收藏新闻的信息
    public ArrayList<NewsNoPhoto> getCollect_news(String username)
    {
        Statement statement_his=getStatement();
        String sql="select * from news_collect where Username= '"+username+"'";

        try {
            ResultSet rSet = statement_his.executeQuery(sql);
            //遍历 读取到quotelist中
            while (rSet.next())
            {
                //获取单条
                NewsNoPhoto history=new NewsNoPhoto(rSet.getString("news_title"),rSet.getString("news_url"),"");
                collect_news.add(history);
            }
            statement_his.close();
            rSet.close();

            return collect_news;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }



    public  Statement getStatement()
    {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL,user , pass);
            if (connection == null) {
                return  null;
            }
            else
            {
                statement=connection.createStatement();//创建statement对象
                   return statement;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        finally {
            try {

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
       return  statement;
    }

    //2018、4/20 根据情况再考虑要不要直接把获取数据的方法写到里面吧，可以考虑根据不同的加载情况，运行不同的sql,获取不同的result

}
