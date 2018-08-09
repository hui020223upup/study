package com.study.designpattern.proxy;

import com.study.service.BuyHouse;

/**
 * Created by wanghh on 2018-7-30.
 */
public class StaticProxy implements BuyHouse {
    private BuyHouse buyHouse;

    public StaticProxy(final BuyHouse buyHouse) {
        this.buyHouse = buyHouse;
    }

    public void buyHouse() {
        System.out.println("买房前准备");
        buyHouse.buyHouse();
        System.out.println("买房后装修");
    }
}
