package com.example.wyb.work1.MainScreen.News;

/**
 * Created by wyb on 2018/4/19.
 */

public class quote {

    private String quote_title;
    private String quote_name;
    private String  quort_imageUrl;

    public quote(String quote_title, String quote_name, String quort_imageUrl) {
        this.quote_title = quote_title;
        this.quote_name = quote_name;
        this.quort_imageUrl = quort_imageUrl;
    }

    //getset
    public String getQuote_title() {
        return quote_title;
    }

    public void setQuote_title(String quote_title) {
        this.quote_title = quote_title;
    }

    public String getQuote_name() {
        return quote_name;
    }

    public void setQuote_name(String quote_name) {
        this.quote_name = quote_name;
    }

    public String getQuort_imageUrl() {
        return quort_imageUrl;
    }

    public void setQuort_imageUrl(String quort_imageUrl) {
        this.quort_imageUrl = quort_imageUrl;
    }
}
