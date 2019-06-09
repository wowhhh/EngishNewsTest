package com.example.wyb.work1.MainScreen.News;

/**
 * Created by wyb on 2018/4/22.
 */

public class TopNews {

    private  String Top_title;
    private String  Top_url;
    private String Top_imageUrl;

    public TopNews(String top_title, String top_url, String top_imageUrl) {
        Top_title = top_title;
        Top_url = top_url;
        Top_imageUrl = top_imageUrl;
    }

    //
    public String getTop_title() {
        return Top_title;
    }

    public void setTop_title(String top_title) {
        Top_title = top_title;
    }

    public String getTop_url() {
        return Top_url;
    }

    public void setTop_url(String top_url) {
        Top_url = top_url;
    }

    public String getTop_imageUrl() {
        return Top_imageUrl;
    }

    public void setTop_imageUrl(String top_imageUrl) {
        Top_imageUrl = top_imageUrl;
    }
}
