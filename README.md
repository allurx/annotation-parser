# annotation-parser
基于Java反射api解析任意数据结构中的自定义注解
# jdk版本
jdk21
# maven依赖
```xml

<dependency>
    <groupId>red.zyc</groupId>
    <artifactId>annotation-parser</artifactId>
    <version>1.0.0</version>
</dependency>
```
# 例子
1. [解析Array中的注解](https://github.com/allurx/annotation-parser/blob/master/src/test/java/red/zyc/parser/ArrayTest.java)
2. [解析Collection中的注解](https://github.com/allurx/annotation-parser/blob/master/src/test/java/red/zyc/parser/CollectionTest.java)
3. [解析Map中的注解](https://github.com/allurx/annotation-parser/blob/master/src/test/java/red/zyc/parser/MapTest.java)
4. [解析对象Field上的注解](https://github.com/allurx/annotation-parser/blob/master/src/test/java/red/zyc/parser/CascadeTest.java)
5. [更多例子](https://github.com/allurx/annotation-parser/tree/master/src/test/java/red/zyc/parser)
# 原理
annotation-parser库是基于JDK1.8新增的`AnnotatedType`类型体系并通过责任链这种设计模式来解析各种复杂数据结构中的注解的，要想完全理解其背后的实现原理需要对Java的`Type`体系和`AnnotatedType`体系有较为深刻的理解，可以参考以下几篇文章进行深入了解。

* [Java Type](https://www.zyc.red/Java/Reflection/Type)
* [Java AnnotatedType](https://www.zyc.red/Java/Reflection/AnnotatedType)
* [Java AnnotatedElement](https://www.zyc.red/Java/Reflection/AnnotatedElement)

# License
[Apache License 2.0](https://github.com/allurx/annotation-parser/blob/master/LICENSE.txt)
