package com.study.basic;

/**
 * 单继承
 * 无法实例化对象
 * Created by wanghh on 2018-8-10.
 */
public class AbstractClassTest {
    int a;//定义变量
    public static final int b = 1; //定义常量

    void abstractMethod() {
        System.out.println("抽象类的抽象方法，含有默认实现");
    }
}
