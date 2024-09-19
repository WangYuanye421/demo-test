package org.wyy.demotest;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.wyy.demotest.service.MyService;
import org.wyy.demotest.service.MyService2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class DemoTestApplicationTests {
    @SpyBean
    MyService myService;// 所有方法使用myService方法,可自定义覆盖

    @MockBean
    MyService2 myService2; // 所有方法需要自定义返回,否则使用默认初始值或null

    // 调用Spy模拟服务,
    @Test
    void invokeSpy(){
        // 仅定义hello的mock规则
        doReturn("hello handsome").when(myService).hello("wyy");
        assertEquals("hello handsome", myService.hello("wyy"));

        // 未定义规则,直接调用say()逻辑
        assertEquals("hello", myService.say("hello"));
    }

    // 调用Mock模拟服务,
    @Test
    void invokeMock(){
        // 仅定义hello的mock规则
        doReturn("hello handsome").when(myService2).hello("wyy");
        assertEquals("hello handsome", myService2.hello("wyy"));

        // 未定义规则,返回初始值或null
        assertEquals("hello", myService2.say("hello"));
    }


    // All 和 each的区别,执行单独的测试方法区分不了,
    // 在执行类级别测试时才能看出来: all仅执行一次,each执行次数取决于有多少个Test方法
    @BeforeAll
    static void beforeAll(){
        System.out.println("before all");
    }

    @BeforeEach
    void beforeEach(){
        System.out.println("before each");
    }

    @AfterEach
    void afterEach(){
        System.out.println("after each");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("after all");
    }
}
