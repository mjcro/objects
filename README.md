# Micro J Object Toolkit


![Java8](https://img.shields.io/badge/Java-8-brightgreen)
![GitHub](https://img.shields.io/github/license/mjcro/objects)
[![Release](https://jitpack.io/v/mjcro/objects.svg)](https://jitpack.io/#mjcro/objects)
![Snyk Vulnerabilities for GitHub Repo](https://img.shields.io/snyk/vulnerabilities/github/mjcro/objects)
[![CircleCI](https://circleci.com/gh/mjcro/objects/tree/main.svg?style=svg)](https://circleci.com/gh/mjcro/objects/tree/main)

Zero-dependency collection of basic tools to work with Java `Object`.

### Disclaimer

It is never recommended to work with Java `Object` objects and cast/convert them in required class.
In most cases if far better to use corresponding classes, beans, POJO's - anything but `Object`.
But sometimes there is no other way - and in this case this library can be helpful.

### TL&DR;

```java
// Constructing source data
HashMap<String, Object> source = new HashMap<>();
source.put("foo", "bar");
source.put("some", "1234567890");

ObjectMap<String> o = ObjectMap.wrap(source);

System.out.println(o.getString("foo"));
System.out.println(o.getString("foo").getClass());
System.out.println(o.getString("some"));
System.out.println(o.getString("some").getClass());
System.out.println(o.getInt("some"));
System.out.println(o.getUnixSecondsInstant("some"));
System.out.println(o.getUnixSecondsInstant("some").getClass());
```

output:

```
bar
class java.lang.String
1234567890
class java.lang.String
1234567890
2009-02-13T23:31:30Z
class java.time.Instant
```

## Quick intro

This library introduces two main abstractions: 

1. `Mixed` - container, that holds arbitrary data and is capable to convert it into requested 
    class (like `Mixed.wrap("12345").getInt()`)
2. `ObjectMap<K>` - map-like container that convert its values to requested classes. 
   Pay attention that `ObjectMap` does not extend/implement `Map` interface.
   
All implementation of abstractions above are thread safe and immutable.

To help work with data conversion `Converter` interface with its implementations can be used 

### Distribution

Builds of this repository can be obtained from [JitPack](https://jitpack.io/#mjcro/objects).

Add JitPack repository:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add dependency:

```xml
<dependency>
    <groupId>io.github.mjcro</groupId>
    <artifactId>objects</artifactId>
    <version><!-- Latest version --></version>
</dependency>
```
