package com.hl.rxnettest.design_pattern.observer_pattern;

public class OberverA implements Observer{
    @Override
    public String function() {
        return "/people/repoa";
    }

    @Override
    public void update(String body) {
        System.out.println("OberverA'body=" + body);
    }
}
