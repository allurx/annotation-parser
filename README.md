# annotation-parser

A Java library for processing custom type-use annotations in objects, collections, maps, and reference arrays. Requires JDK 25.

## Maven Dependency

<pre><code>&lt;dependency&gt;
    &lt;groupId&gt;io.allurx&lt;/groupId&gt;
    &lt;artifactId&gt;annotation-parser&lt;/artifactId&gt;
    &lt;version&gt;<a href="https://central.sonatype.com/artifact/io.allurx/annotation-parser">LATEST_VERSION</a>&lt;/version&gt;
&lt;/dependency&gt;</code></pre>

Replace `LATEST_VERSION` with a published version from the link.

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

### Annotation Handler

```java
public class EraseStringAnnotationHandler implements AnnotationHandler<String, EraseString, String> {

    @Override
    public String handle(String target, EraseString annotation) {
        return "******";
    }
}
```

### Parsing

Capture the annotated type with `io.allurx.kit.base.reflection.AnnotatedTypeToken`,
then pass it to `AnnotationParser.parse`. Mark object types with `@Cascade` to
traverse their fields or record components, including each nested type that needs
traversal.

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
- Collection and map results keep the input's concrete implementation class. It
  must support creating and filling the result; if its constructors are insufficient,
  register an `InstanceCreator` through
  [InstanceCreators](src/main/java/io/allurx/annotation/parser/util/InstanceCreators.java).

## JPMS

Require `io.allurx.annotation.parser` in named modules. Open model packages when
parsing non-public members, and handler packages as needed for reflective
constructor access. Replace the example module and package names:

```java
module com.example.app {
    requires io.allurx.annotation.parser;
    opens com.example.model to io.allurx.annotation.parser;
}
```

## Reflection

Parsing uses `AnnotatedType` metadata and an ordered set of type parsers.
Background on the reflection types:

- [Java Type](https://www.allurx.io/Java/Reflection/Type)
- [Java AnnotatedType](https://www.allurx.io/Java/Reflection/AnnotatedType)
- [Java AnnotatedElement](https://www.allurx.io/Java/Reflection/AnnotatedElement)

## Build

Build and test with Maven from the repository root:

```sh
mvn -B -ntp clean verify
```

Build sources and Javadoc without signing or publishing:

```sh
mvn -B -ntp -Prelease "-Dgpg.skip=true" clean verify
```

CI and releases use [allurx-build](https://github.com/allurx/allurx-build).

## License

[Apache License 2.0](LICENSE.txt).
