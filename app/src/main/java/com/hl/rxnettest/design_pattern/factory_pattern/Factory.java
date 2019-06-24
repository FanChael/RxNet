package com.hl.rxnettest.design_pattern.factory_pattern;

public class Factory {
    public static Animal decodeDog(){
        return new Dog();
    }

    public static Animal decodeCat(){
        return new Cat();
    }
}
