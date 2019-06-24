package com.hl.rxnettest.design_pattern.factory_pattern;

public class FactoryDemo {
    public static void main(String[] args){
        Animal cat = Factory.decodeCat();
        Animal dog = Factory.decodeDog();
        cat.eatFood();
        dog.eatFood();
    }
}
