package com.commlibary.utils;


import android.graphics.Bitmap;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Test {

    public static void main(String[] args){

        Subscriber<String> sb = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.print(s);
            }
        };

        List<String> temp = new ArrayList<>();
        temp.add("A");
        temp.add("B");
        Observable.from(temp).subscribe(sb);

        String tempstr = "23.15955";
        System.out.print(Float.valueOf(tempstr));



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


        List<Random> a = getValue(Random.class);



    }

    private static <T> List<T>getValue(Class<T> cls){
        return null;
    }




}
