package com.example.httpdemo;

public interface CallBack {
    void onSuccess(String result);
    void onError(Exception e);

}
