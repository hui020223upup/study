package com.study.basic;

import java.util.HashMap;

/**
 * 想测jdk1.8之前的版本在扩容的时候可能出现循环链表的情况
 * 但jdk1.8已经解决了这个问题
 * 至于是怎么解决的请听下文分解
 *  Collections.synchronizedMap(new HashMap(...));可使hashmap线程安全
 *  ConcurrentHashMap 线程安全（分段锁）
 * Created by wanghh on 2018-8-7.
 */
public class HasMapTest {
    public static final HashMap<String, String> map = new HashMap<String, String>();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread() {
            public void run() {
                for (int i = 0; i < 25; i++) {
                    map.put(String.valueOf(i), String.valueOf(i));
                }
            }
        };
        Thread thread2 = new Thread() {
            public void run() {
                for (int i = 25; i < 50; i++) {
                    map.put(String.valueOf(i), String.valueOf(i));
                }
            }
        };
        thread1.start();
        thread2.start();

        Thread.currentThread().sleep(1000);

        for (int i = 0; i < 50; i++) {
            //如果key和value不同，说明在两个线程put的过程中出现异常。
            if (!String.valueOf(i).equals(map.get(String.valueOf(i)))) {
                System.err.println(String.valueOf(i) + ":" + map.get(String.valueOf(i)));
            }
        }

    }
}
