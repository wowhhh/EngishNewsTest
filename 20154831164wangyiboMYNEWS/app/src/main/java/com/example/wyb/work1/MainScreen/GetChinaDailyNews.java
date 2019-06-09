package com.example.wyb.work1.MainScreen;

import android.os.Message;
import android.util.Log;

import com.example.wyb.work1.MainScreen.News.NewsWithPhoto;
import com.example.wyb.work1.MainScreen.NewsAdapter.WithPhotoApater;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by wyb on 2018/4/17.
 */
//公用的爬取新闻方法，传入url 返回arraylist
public class GetChinaDailyNews {

    private ArrayList<NewsWithPhoto> newsWithPhotoList=new ArrayList<>();
    public ArrayList<NewsWithPhoto> getNews(final String url)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Document doc = Jsoup.connect(url).get();
                    //爬取页面所有新闻
                    Elements news=doc.select("div.main_art");
                    //将新闻分条
                    Elements newslist=doc.select("div.mb10");
                    //循环提取每一条新闻的，时间，标题，链接，图片链接
                    for (int i=0;i<newslist.size();i++)
                    {
                        String title=newslist.get(i).select("a").text();
                        String time=newslist.get(i).select("b").text();
                        String url=newslist.get(i).select("a").attr("href");
                        String image_url=null;
                        //因为有的新闻没有图片
                        try
                        {
                            image_url=newslist.get(i).select("img").attr("src");
                        }
                        catch (Exception image_e)
                        {
                            image_e.printStackTrace();
                        }

                        //如果新闻没有图片的话就不添加吧
                        if(image_url=="")
                        {
                            //NewsWithPhoto newsWithPhoto=new NewsWithPhoto(title,time,url);
                            //newsPhotoList.add(newsWithPhoto);
                            //测试

                        }
                        else
                        {
                            NewsWithPhoto newsWithPhoto=new NewsWithPhoto(title,time,url,image_url);

                            newsWithPhotoList.add(newsWithPhoto);
                        }
                    }

                    Log.e("商业新闻数据：",Integer.toString(newsWithPhotoList.size()) );

                }
                catch (Exception e)
                {
                    e.printStackTrace();

                }

            }
        }).start();
        return newsWithPhotoList;
    }
}
