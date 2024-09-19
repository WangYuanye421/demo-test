# Junit5简介
与单一模块设计的Junit4不同,Junit5引入了模块化架构,由三个主要子项目组成：

+ JUnit Platform：测试运行的基础平台，支持不同的测试引擎（不仅仅是 JUnit，还可以扩展其他测试框架，如 TestNG）。
+ JUnit Jupiter：JUnit5 的新编程模型和扩展模型，包含新的注解和测试方法（如 @Test, @BeforeEach，@AfterEach 等）。
+ JUnit Vintage：提供对 JUnit4 及更早版本的向后兼容支持，因此 JUnit5 可以运行旧的 JUnit4 测试代码。

## Junit5 注解
1. @BeforeEach 和 @AfterEach：取代了 JUnit4 的 @Before 和 @After，作用于每个测试方法。
2. @BeforeAll 和 @AfterAll：取代了 JUnit4 的 @BeforeClass 和 @AfterClass，可以作用于整个类生命周期，且在 JUnit5 中可以是非静态方法（通过注入 TestInstance）。
3. @DisplayName：允许为测试方法和类指定自定义名称，方便生成更具可读性的测试报告。
4. @Nested：支持嵌套的测试类，便于组织复杂的测试场景。
5. @ParameterizedTest：增强了参数化测试的支持，允许为测试方法传递多个参数集。
6. DynamicTest：动态创建测试用例，支持灵活的测试流程。

> `@BeforeAll``@AfterAll` 类级别，只执行一次
>
> `@BeforeEach` `@AfterEach` 方法级别，每个方法都会执行
>
> <font style="color:#080808;background-color:#ffffff;">All 和 each的区别，在执行</font>**<font style="color:#080808;background-color:#ffffff;">类级别</font>**<font style="color:#080808;background-color:#ffffff;">测试时才能看出来: all仅执行一次,each执行次数取决于有多少个Test方法</font>
>



## Junit5与Spring结合
在与Spring集成时,不再使用@RunWith

+ @ExtendWith, 指定拓展为Spring, 测试中可以使用Spring注解进行依赖注入,@ContextConfiguration指定配置类
+ @Transactional, 测试中提供事务支持
+ @MockBean @SpyBean, 提供Mocking支持,模拟Bean行为

> 在springboot项目中，`@SpringBootTest`注解内部就是使用了`<font style="color:#080808;background-color:#ffffff;">@ExtendWith({SpringExtension.class})</font>`<font style="color:#080808;background-color:#ffffff;">提前帮我们配置好了</font>
>

# 差异概览
| 功能/特性 | **JUnit4** | **JUnit5** | **Spring Test** |
| --- | --- | --- | --- |
| **架构** | 单一模块 | 模块化架构（Platform, Jupiter, Vintage） | 基于 TestContext 框架，与 JUnit 集成 |
| **注解** | `@Test`<br/>, `@Before`<br/>, `@After` | `@Test`<br/>, `@BeforeEach`<br/>, `@AfterEach` | `@ContextConfiguration`<br/>, `@Transactional` |
| **扩展机制** | `@RunWith`<br/>, `TestRule` | `@ExtendWith`<br/>, `TestInstance` | `@ExtendWith(SpringExtension.class)` |
| **参数化测试** | 较弱的参数化测试支持 | 强大的参数化测试支持 | 与 Spring 环境集成，支持 Mock 和依赖注入 |
| **事务管理** | N/A | N/A | 支持 `@Transactional`<br/>，测试完成后自动回滚 |
| **Spring 集成** | `@RunWith(SpringJUnit4ClassRunner.class)` | `@ExtendWith(SpringExtension.class)` | 内置的对 Spring 上下文的管理和 Bean 注入支持 |


# Mocking
## @MockBean
如果不指定规则，则mockBean执行完返回默认值，即对象为null，数字为0

如果指定了规则，就按照规则返回，下面例子按照规则返回`ok`

## @SpyBean
有规则按照规则走，没有规则按照真实服务进行。比如：在多服务调用过程中，如果部分服务不可用，可以定义规则，如果服务可用，则调用真实的服务。

## Demo
```java
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

```

# 注意事项
1. 使用断言进行判断，严禁System.out进行人工判断
2. 丰富测试场景的多样性，通过不同参数测试增加多样性
3. 测试覆盖率

