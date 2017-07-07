package com.commlibary.utils;


import android.graphics.Bitmap;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Test {

    public static void main(String[] args){

        String [] names={"A","B"};
        Observable.from(names).flatMap(new Func1<String, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(String s) {
                return null;
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {

            }
        });

//        Observable.just("bbb").map(new Func1<String, Integer>() {
//            @Override
//            public Integer call(String s) {
//                return Integer.valueOf(s);
//            }
//        }).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer integer) {
//
//            }
//        });

        Observable ob = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("aaa");
            }


        });
        ob.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                System.out.print(o);
            }
        });




    }




}
