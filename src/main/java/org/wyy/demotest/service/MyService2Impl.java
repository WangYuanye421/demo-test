package org.wyy.demotest.service;

import org.springframework.stereotype.Service;

/**
 * @author yuanyewang515@gmail.com
 * @since v1.0
 **/
@Service
public class MyService2Impl implements MyService2 {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }

    @Override
    public String say(String msg) {
        return msg;
    }
}
