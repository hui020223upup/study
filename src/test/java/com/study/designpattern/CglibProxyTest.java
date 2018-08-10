package com.study.designpattern;

import com.study.designpattern.proxy.CglibProxy;
import com.study.service.BuyHouse;
import com.study.service.impl.BuyHouseImpl;
import org.junit.Test;

/**
 * Created by wanghh on 2018-7-30.
 */
public class CglibProxyTest {
    @Test
    public void buyHouse() {
        BuyHouse buyHouse = new BuyHouseImpl();
        CglibProxy cglibProxy = new CglibProxy();
        BuyHouse buyHouseProxy = (BuyHouseImpl) cglibProxy.getInstance(buyHouse);
        buyHouseProxy.buyHouse();
    }
}
