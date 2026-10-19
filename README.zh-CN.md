# annotation-parser

[English](README.md) | 简体中文

用于处理自定义类型使用注解的 Java 库，支持对象、集合、Map 和引用类型数组。需要 JDK 25。

## Maven 依赖

<pre><code>&lt;dependency&gt;
    &lt;groupId&gt;io.allurx&lt;/groupId&gt;
    &lt;artifactId&gt;annotation-parser&lt;/artifactId&gt;
    &lt;version&gt;<a href="https://central.sonatype.com/artifact/io.allurx/annotation-parser">LATEST_VERSION</a>&lt;/version&gt;
&lt;/dependency&gt;</code></pre>

将 `LATEST_VERSION` 替换为链接中已发布的版本。

## 示例

### 自定义注解

通过 `@Parse` 将运行时保留的类型使用注解与其处理器关联。

```java
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parse(handler = EraseStringAnnotationHandler.class, annotation = EraseString.class)
public @interface EraseString {
}
```

### 注解处理器

```java
public class EraseStringAnnotationHandler implements AnnotationHandler<String, EraseString, String> {

    @Override
    public String handle(String target, EraseString annotation) {
        return "******";
    }
}
```

### 解析

使用 `io.allurx.kit.base.reflection.AnnotatedTypeToken` 捕获带注解的类型，再传给 `AnnotationParser.parse`。需要遍历对象字段或 record 组件时，为对象类型添加 `@Cascade`；需要继续遍历的每个嵌套类型也应添加该注解。

```java
void parse() {

    // String
    var v1 = AnnotationParser.parse("123456", new AnnotatedTypeToken<@EraseString String>() {
    });
    assertEquals("******", v1);

    // Collection
    var v2 = AnnotationParser.parse(Stream.of("123456").collect(Collectors.toList()), new AnnotatedTypeToken<List<@EraseString String>>() {
    });
    v2.forEach(s -> assertEquals("******", s));

    // Array
    var v3 = AnnotationParser.parse(new String[]{"123456"}, new AnnotatedTypeToken<@EraseString String[]>() {
    });
    Arrays.stream(v3).forEach(s -> assertEquals("******", s));

    // Map
    var v4 = AnnotationParser.parse(Stream.of("allurx").collect(Collectors.toMap(s -> s, s -> "123456")), new AnnotatedTypeToken<Map<@EraseString String, @EraseString String>>() {
    });
    v4.forEach((s1, s2) -> {
        assertEquals("******", s1);
        assertEquals("******", s2);
    });

    // Object
    record Person(@EraseString String password) {
    }
    var v5 = AnnotationParser.parse(new Person("123456"), new AnnotatedTypeToken<@Cascade Person>() {
    });
    assertEquals("******", v5.password);
}
```

### 注意事项

- 使用 `parse()` 的返回值；解析过程可能创建新的对象或容器。
- 集合和 Map 的解析结果保留输入的具体实现类。该实现必须支持创建和填充结果；如果现有构造器无法满足要求，可通过 [InstanceCreators](src/main/java/io/allurx/annotation/parser/util/InstanceCreators.java) 注册 `InstanceCreator`。

## JPMS

命名模块需声明 `requires io.allurx.annotation.parser`。解析非公开成员时，应开放模型所在的包；需要反射访问处理器构造器时，也应按需开放处理器所在的包。请替换示例中的模块名和包名：

```java
module com.example.app {
    requires io.allurx.annotation.parser;
    opens com.example.model to io.allurx.annotation.parser;
}
```

## 反射

解析过程使用 `AnnotatedType` 元数据和有序的类型解析器集合。相关反射类型的背景说明：

- [Java Type](https://blog.allurx.io/reflection-types/)
- [Java AnnotatedType](https://blog.allurx.io/annotated-type/)
- [Java AnnotatedElement](https://blog.allurx.io/annotated-element/)

## 构建

在仓库根目录使用 Maven 构建并运行测试：

```sh
mvn -B -ntp clean verify
```

生成源码包和 Javadoc，但不签名或发布：

```sh
mvn -B -ntp -Prelease "-Dgpg.skip=true" clean verify
```

CI 和发布流程使用 [allurx-build](https://github.com/allurx/allurx-build)。依赖更新采用共享的 [Renovate 策略](https://github.com/allurx/allurx-build/blob/main/docs/dependency-updates.md)，目标分支为 `dev`。

## 许可证

[Apache License 2.0](LICENSE.txt)。
