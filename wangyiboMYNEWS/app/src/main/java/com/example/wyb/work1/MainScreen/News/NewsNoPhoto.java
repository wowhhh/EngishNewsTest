package com.example.wyb.work1.MainScreen.News;

/**
 * Created by wyb on 2018/3/15.
 */

//初期测试一下吧，后面可能也会使用到这个类，
//主要是用来存储爬取下来的体育新闻
    //发现虎扑新闻可以通过爬取xml的方式获取到图片的地址
    //所以可以通过爬取xml来获取相应的内容，那么首页的布局就采用图片加文字的形式了

public class NewsNoPhoto {

    private  String NewsTitle;//新闻标题
    private  String NewsUrl;//新闻地址
    private  String NewsTime;//新闻时间与来源

    public NewsNoPhoto(String newsTitle, String newsUrl) {
        NewsTitle = newsTitle;
        NewsUrl = newsUrl;
    }

    //设置getset方法
    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getNewsUrl() {
        return NewsUrl;
    }

    public String getNewsTime() {
        return NewsTime;
    }

    public void setNewsTime(String newsTime) {
        NewsTime = newsTime;
    }

    public void setNewsUrl(String newsUrl) {
        NewsUrl = newsUrl;
    }

    public NewsNoPhoto(String NewsTitle, String NewsUrl, String NewsTime){
        this.NewsTime=NewsTime;
        this.NewsTitle=NewsTitle;
        this.NewsUrl=NewsUrl;



    }
}
