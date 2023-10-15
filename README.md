# annotation-parser
基于Java反射api解析任意数据结构中的任意注解
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
