# annotation-parser

**A library for parsing custom annotations in any data structures.**

## JDK Version

**JDK 21**

## Maven Dependency

```xml
<dependency>
    <groupId>io.allurx</groupId>
    <artifactId>annotation-parser</artifactId>
    <version>${latest version}</version>
</dependency>
```

## Example

### Custom Annotation

```java
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parse(handler = EraseStringAnnotationHandler.class, annotation = EraseString.class)
public @interface EraseString {
}
```

### Writing an Annotation Handler

```java
public class EraseStringAnnotationHandler implements AnnotationHandler<String, EraseString, String> {

    @Override
    public String handle(String target, EraseString annotation) {
        return "******";
    }
}
```

### Parsing with AnnotationParser

You can parse the annotation in any data structure using the `AnnotationParser`. Here are some examples:

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

## Principles

The `annotation-parser` library is built on the `AnnotatedType` type system introduced in JDK 1.8 and utilizes the Chain of Responsibility design pattern to parse custom annotations in arbitrary data structures. A thorough understanding of Java's `Type` and `AnnotatedType` systems is essential for grasping the underlying implementation principles. For deeper insights, consider reading the following articles:

- [Java Type](https://www.zyc.red/Java/Reflection/Type)
- [Java AnnotatedType](https://www.zyc.red/Java/Reflection/AnnotatedType)
- [Java AnnotatedElement](https://www.zyc.red/Java/Reflection/AnnotatedElement)

## License

This project is licensed under the [Apache License 2.0](LICENSE.txt).