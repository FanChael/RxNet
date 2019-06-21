package com.hl.rxnettest.design_pattern.observer_pattern;

public interface Observer {
    String function(); // 返回一个请求方法
    void update(String body); // 通知更新回调，body是网络请求成功后的结果
}
