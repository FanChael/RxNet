package com.hl.rxnettest.design_pattern.factory_pattern;

class Cat implements Animal {
    @Override
    public void eatFood() {
        System.out.println("猫粮走起!");
    }
}
