package com.hl.rxnettest.design_pattern.observer_pattern;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Observable {
    private final ArrayList<Observer> observerArrayList = new ArrayList<>();
    private final ArrayList<Class<? extends Observer>> classArrayList = new ArrayList<>();

    public void registerObserver(Observer observer){
        if (null == observer) throw new NullPointerException();
        observerArrayList.add(observer);
    }

    public void registerObserver(Class<? extends Observer> _class){
        if (null == _class) throw new NullPointerException();
        classArrayList.add(_class);
    }

    public void unRegisterObserver(Observer observer){
        if (null == observer) throw new NullPointerException();
        if (observerArrayList.contains(observer)){
            observerArrayList.remove(observer);
        }
    }

    public void unRegisterObserver(Class<? extends Observer> _class){
        if (null == _class) throw new NullPointerException();
        if (classArrayList.contains(_class)){
            classArrayList.remove(_class);
        }
    }

    public void unRegisterAll(){
        if (null != observerArrayList){
            observerArrayList.clear();
        }
    }

    public void unRegisterAllClass(){
        if (null != classArrayList){
            classArrayList.clear();
        }
    }

    public void subcribe(){
        for (final Observer item : observerArrayList){
            // 获取请求方法，然后进行子线程网络请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String requestFunction = item.function();
                    System.out.println("请求方法" + requestFunction);
                    // 请求成功回调
                    item.update("{\"code\": 0, \"message\": \"OK\", \"method\": \"" + requestFunction + "\"}");
                }
            }).start();
        }
    }

    public void subcribe(final Class<? extends Observer> cls){
        if (null == cls) throw new NullPointerException();
        if (classArrayList.contains(cls)){
            // 获取请求方法，然后进行子线程网络请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Method[] methods = cls.getDeclaredMethods();
                    try {
                        Method function = cls.getMethod("function");
                        Method update = cls.getMethod("update", String.class);
                        // 创建一个实施对象
                        Object obj = cls.newInstance();
                        Object requestFunction = function.invoke(obj);
                        System.out.println("请求方法" + requestFunction);
                        // 根据请求方法进行网络请求requestFunction
                        // 请求成功回调
                        update.invoke(obj, "{\"code\": 0, \"message\": \"OK\", \"method\": \"" + requestFunction + "\"}");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
