package com.hl.rxnettest.design_pattern.factory_pattern;

class Dog implements Animal {
    @Override
    public void eatFood() {
        System.out.println("狗粮也来一把!");
    }
}
