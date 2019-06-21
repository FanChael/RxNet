package com.hl.rxnettest.design_pattern.observer_pattern;

public class OberverB implements Observer{
    @Override
    public String function() {
        return "/people/repob";
    }
    @Override
    public void update(String body) {
        System.out.println("OberverB'body=" + body);
    }
}
