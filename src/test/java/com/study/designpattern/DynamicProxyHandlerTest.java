package com.study.designpattern;

import com.study.designpattern.proxy.DynamicProxyHandler;
import com.study.service.BuyHouse;
import com.study.service.impl.BuyHouseImpl;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Created by wanghh on 2018-7-30.
 */
public class DynamicProxyHandlerTest {
    @Test
    public void buyHouse() {
        BuyHouse buyHouse = new BuyHouseImpl();
        BuyHouse buyHouseProxy = (BuyHouse) Proxy.newProxyInstance(BuyHouse.class.getClassLoader(), new Class[]{BuyHouse.class}, new DynamicProxyHandler(buyHouse));
        buyHouseProxy.buyHouse();
    }
}
