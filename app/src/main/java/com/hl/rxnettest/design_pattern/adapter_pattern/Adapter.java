package com.hl.rxnettest.design_pattern.adapter_pattern;

public class Adapter extends NeedAdapter implements CustomNeed {
    @Override
    public void needEverything() {
        //  提供给用户所有选择，不然不够用户眼睛看！
        System.out.println("随便你选择！");
    }
}
