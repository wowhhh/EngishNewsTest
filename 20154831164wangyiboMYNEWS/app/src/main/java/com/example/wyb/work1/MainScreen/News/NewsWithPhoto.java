package com.example.wyb.work1.MainScreen.News;

/**
 * Created by wyb on 2018/3/22.
 */


//本实体类是带有图片的新闻，新闻图片给的是连接，后面可以使用imageview加载网络图片
public class NewsWithPhoto {
    private String new_title;//新闻标题
    private String new_time;//时间
    private String new_url;//新闻连接
    private String new_photo_url;//图片连接

    public NewsWithPhoto(String new_title, String new_url) {
        this.new_title = new_title;
        this.new_url = new_url;
    }

    public NewsWithPhoto(String new_title, String new_time, String new_url, String new_photo_url) {
        this.new_title = new_title;
        this.new_time = new_time;
        this.new_url = new_url;
        this.new_photo_url = new_photo_url;
    }

    public NewsWithPhoto(String new_title, String new_time, String new_url) {
        this.new_title = new_title;
        this.new_time = new_time;
        this.new_url = new_url;
    }

//getset

    public String getNew_title() {
        return new_title;
    }

    public void setNew_title(String new_title) {
        this.new_title = new_title;
    }

    public String getNew_time() {
        return new_time;
    }

    public void setNew_time(String new_time) {
        this.new_time = new_time;
    }

    public String getNew_url() {
        return new_url;
    }

    public void setNew_url(String new_url) {
        this.new_url = new_url;
    }

    public String getNew_photo_url() {
        return new_photo_url;
    }

    public void setNew_photo_url(String new_photo_url) {
        this.new_photo_url = new_photo_url;
    }


}
