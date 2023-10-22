# annotation-parser
基于Java反射api解析任意数据结构中的自定义注解
# 用法
## JDK版本
JDK21
## maven依赖
```xml
<dependency>
    <groupId>red.zyc</groupId>
    <artifactId>annotation-parser</artifactId>
    <version>1.0.0</version>
</dependency>
```
## 例子
自定义注解
```java
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parse(handler = EraseStringAnnotationHandler.class, annotation = EraseString.class)
public @interface EraseString {
}
```
编写注解处理器
```java
public class EraseStringAnnotationHandler implements AnnotationHandler<String, EraseString, String> {

    @Override
    public String handle(String target, EraseString annotation) {
        return "******";
    }
}
```
最后通过[AnnotationParser](https://github.com/allurx/annotation-parser/blob/master/src/main/java/red/zyc/parser/AnnotationParser.java)则可以在任意数据结构中解析该注解
```java
void parse() {

    // String
    var v1 = AnnotationParser.parse("123456", new AnnotatedTypeToken<@EraseString String>() {
    });
    assert "******".equals(v1);

    // Collection
    var v2 = AnnotationParser.parse(Stream.of("123456").collect(Collectors.toList()), new AnnotatedTypeToken<List<@EraseString String>>() {
    });
    v2.forEach(s -> {
        assert "******".equals(s);
    });

    // Array
    var v3 = AnnotationParser.parse(new String[]{"123456"}, new AnnotatedTypeToken<@EraseString String[]>() {
    });
    Arrays.stream(v3).forEach(s -> {
        assert "******".equals(s);
    });

    // Map
    var v4 = AnnotationParser.parse(Stream.of("张三").collect(Collectors.toMap(s -> s, s -> "123456")), new AnnotatedTypeToken<Map<@EraseString String, @EraseString String>>() {
    });
    v4.forEach((s1, s2) -> {
        assert "******".equals(s1) && "******".equals(s2);
    });

    // Object
    record Person(@EraseString String password) {
    }
    var v5 = AnnotationParser.parse(new Person("123456"), new AnnotatedTypeToken<@Cascade Person>() {
    });
    assert "******".equals(v5.password);
}
```
# 原理
annotation-parser库是基于JDK1.8新增的`AnnotatedType`类型体系并通过责任链这种设计模式来解析任意数据结构中自定义注解的，要想完全理解其背后的实现原理需要对Java的`Type`体系和`AnnotatedType`体系有较为深刻的理解，可以参考以下几篇文章进行深入了解。

* [Java Type](https://www.zyc.red/Java/Reflection/Type)
* [Java AnnotatedType](https://www.zyc.red/Java/Reflection/AnnotatedType)
* [Java AnnotatedElement](https://www.zyc.red/Java/Reflection/AnnotatedElement)

# License
[Apache License 2.0](https://github.com/allurx/annotation-parser/blob/master/LICENSE.txt)
