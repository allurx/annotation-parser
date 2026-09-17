# annotation-parser

**A Java library for processing custom type-use annotations in objects, collections, maps, and reference arrays.**

## JDK Version

**JDK 25**

## Maven Dependency

<pre><code>&lt;dependency&gt;
    &lt;groupId&gt;io.allurx&lt;/groupId&gt;
    &lt;artifactId&gt;annotation-parser&lt;/artifactId&gt;
    &lt;version&gt;<a href="https://central.sonatype.com/artifact/io.allurx/annotation-parser">LATEST_VERSION</a>&lt;/version&gt;
&lt;/dependency&gt;</code></pre>

Replace `LATEST_VERSION` with a version from the linked Maven Central page.

## Example

### Custom Annotation

Use `@Parse` to associate a runtime type-use annotation with its handler.

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

Capture the annotated type with `AnnotatedTypeToken` from
`io.allurx.kit.base.reflection`, then pass it to `AnnotationParser.parse`.
Use `@Cascade` on an object type to process its fields or record components;
nested object types that need traversal also require `@Cascade`.

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

### Notes

- Use the value returned by `parse()`; parsing may create a new object or container.
- Collection and map results use the input's concrete implementation class, which
  must support creating and filling a result instance. If its constructors are
  insufficient, register an `InstanceCreator` through
  [InstanceCreators](src/main/java/io/allurx/annotation/parser/util/InstanceCreators.java).

## JPMS

For a named module, require `io.allurx.annotation.parser`. Open model packages
when parsing non-public members, and handler packages when their constructors
need reflective access. Replace the example module and package names below:

```java
module com.example.app {
    requires io.allurx.annotation.parser;
    opens com.example.model to io.allurx.annotation.parser;
}
```

## Principles

Parsing uses Java's `AnnotatedType` metadata and an ordered set of type parsers.
For background on the reflection types involved:

- [Java Type](https://www.allurx.io/Java/Reflection/Type)
- [Java AnnotatedType](https://www.allurx.io/Java/Reflection/AnnotatedType)
- [Java AnnotatedElement](https://www.allurx.io/Java/Reflection/AnnotatedElement)

## Build and CI/CD

Run `mvn -B -ntp clean verify` from the repository root. See
[CI and releases](docs/ci-cd.md) for local verification, CI and release steps.

## License

This project is licensed under the [Apache License 2.0](LICENSE.txt).
