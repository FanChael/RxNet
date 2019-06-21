package com.hl.rxnettest.design_pattern.observer_pattern;

public class ObserverPatternDemo {
    public static void main(String[] args){
        Observable observable = new Observable();

        //        observable.registerObserver(new OberverA());
        //        observable.registerObserver(new OberverB());
        //        observable.registerObserver(new Observer() {
        //            @Override
        //            public String function() {
        //                return "/people/repo_匿名";
        //            }
        //
        //            @Override
        //            public void update(String body) {
        //                System.out.println("Observer'body=" + body);
        //            }
        //        });
        //        observable.subcribe();

        observable.registerObserver(OberverA.class);
        observable.registerObserver(OberverB.class);
        observable.subcribe(OberverA.class);
        observable.subcribe(OberverB.class);
    }
}
