package com.study.designpattern;

import com.study.designpattern.proxy.StaticProxy;
import com.study.service.BuyHouse;
import com.study.service.impl.BuyHouseImpl;
import org.junit.Test;

/**
 * Created by wanghh on 2018-7-30.
 */
public class StaticProxyTest {
    @Test
    public void buyHouse(){
        BuyHouse buyHouse = new BuyHouseImpl();
        StaticProxy buyHouseProxy = new StaticProxy(buyHouse);
        buyHouseProxy.buyHouse();
    }
}
