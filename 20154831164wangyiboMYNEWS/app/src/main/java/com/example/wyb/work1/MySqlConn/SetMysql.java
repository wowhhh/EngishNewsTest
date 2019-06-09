package com.example.wyb.work1.MySqlConn;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by wyb on 2018/4/24.
 */

//用来向数据库中插入数据

public class SetMysql {
    //数据库哦
    /** private static final String DB_URL="jdbc:mysql://23.106.151.159/Android_myNews";
     private static final String user="News";
     private static final String pass="News";
     */
    private static final String DB_URL="jdbc:mysql://106.2.23.8/wangyibo";
    private static final String user="mysql";
    private static final String pass="mysql";
    private java.sql.Connection connection=null;
    private Statement statement;

    //插入用户注册表信息
    public  boolean registeruser(String username,String pass)
    {
        String sql="insert into user (Username,Password) values(?,?)";
        PreparedStatement preparedStatement;
        try
        {
            getconn();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,pass);

            int row=preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
            if(row>0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  false;
    }
    //插入用户的浏览历史信息
    public int insertUserHistory(String username,String news_title,String news_url )
    {
        int row=0;
        String sql="insert into news_history (Username,news_title,news_url) values(?,?,?)";
        PreparedStatement preparedStatement;

        try
        {   getconn();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,news_title);
            preparedStatement.setString(3,news_url);

            row=preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }
    //插入用户的收藏数据
    public int insertUserCollect(String username,String news_title,String news_url)
    {

        int row=0;
        String sql="insert into news_collect (Username,news_title,news_url) values(?,?,?)";
        PreparedStatement preparedStatement;

        try
        {   getconn();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,news_title);
            preparedStatement.setString(3,news_url);

            row=preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }


    //创表
    public void execreat(String sql)
    {
        PreparedStatement preparedStatement;

        try
        {   getconn();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void getconn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection(DB_URL,user,pass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
