package com.example.wyb.work1.MainScreen.User;

/**
 * Created by wyb on 2018/4/24.
 */

//用户实例，获取用户信息，登录界面会使用到
public class user_enity {

    private String username;
    private String password;

    public user_enity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
