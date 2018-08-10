package com.study.basic;

/**
 * 多实现
 * 无法实例化对象
 * Created by wanghh on 2018-8-10.
 */
public interface InterfaceTest {
    int a = 0;//接口中定义的变量默认是public static final(常量)
    //如果是可变的成员变量，但该变量是static，在方法区，在方法中无法修改该变量，
    // 因为方法中引用的变量属于线程私有的，会随之方法执行结束而被回收

    void abstractMethod();//方法是public abstract,抽象方法不含方法体，需由实现类实现

    //是public的，可以被重写
    default void defaultMethod() {
        System.out.println("1.8加了默认实现方法，更接近抽象类了");
    }

    //只能在本接口中调用，职责上是工具方法
    static int staticMethod() {
        return 1;
    }
}
