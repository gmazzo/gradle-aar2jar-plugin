![GitHub](https://img.shields.io/github/license/gmazzo/gradle-aar2jar-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.gmazzo.aar2jar/io.github.gmazzo.aar2jar.gradle.plugin)](https://central.sonatype.com/artifact/io.github.gmazzo.aar2jar/io.github.gmazzo.aar2jar.gradle.plugin)
[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.gmazzo.aar2jar)](https://plugins.gradle.org/plugin/io.github.gmazzo.aar2jar)
[![Build Status](https://github.com/gmazzo/gradle-aar2jar-plugin/actions/workflows/ci-cd.yaml/badge.svg)](https://github.com/gmazzo/gradle-aar2jar-plugin/actions/workflows/ci-cd.yaml)
[![Coverage](https://codecov.io/gh/gmazzo/gradle-aar2jar-plugin/branch/main/graph/badge.svg?token=D5cDiPWvcS)](https://codecov.io/gh/gmazzo/gradle-aar2jar-plugin)
[![Users](https://img.shields.io/badge/users_by-Sourcegraph-purple)](https://sourcegraph.com/search?q=content:io.github.gmazzo.aar2jar+-repo:github.com/gmazzo/gradle-aar2jar-plugin)

[![Contributors](https://contrib.rocks/image?repo=gmazzo/gradle-aar2jar-plugin)](https://github.com/gmazzo/gradle-aar2jar-plugin/graphs/contributors)

# gradle-aar2jar-plugin

A Gradle plugin to allow consuming Android's AAR dependencies as JAR dependencies for JVM projects.

# Usage

Apply the plugin at the **root** project (preferable):

```kotlin
plugins {
    java
    id("io.github.gmazzo.aar2jar") version "<latest>"
}

dependencies {
    implementation("androidx.fragment:fragment:1.8.5")
}

repositories {
    mavenCentral()
    google()
}
```

Then, whenever you consume classes from the AAR as it if it was a JAR.

## Consuming a local `com.android.library` module
This plugin will enable to transform `aar`s into `jar`s,
but Android Libraries still publish multiple variants (at least `debug` and `release`) and
the `java` plugin does not support it.

You'll must tell Gradle will variant you want to consume, by specifying the
`import com.android.build.gradle.internal.attributes.VariantAttr` attribute. For instance:

```kotlin
dependencies {
  implementation(project(":my-android-library")) {
    attributes {
      attribute(VariantAttr.ATTRIBUTE, objects.named("release"))
    }
  }
}
```
will consume the `release` variant of the `my-android-library` module.

> [!NOTE]
> This should be the workaround if you are getting this error after appling the plugin
> 
> <pre>The consumer was configured to find a library for use during compile-time, compatible with Java 21,<br/>preferably in the form of class files, preferably optimized for standard JVMs, <br/>and its dependencies declared externally, as well as attribute 'artifactType' with value 'jar'. <br/>However we cannot choose between the following variants of project ':lib':<br/>          - debugApiElements<br/>          - releaseApiElements</pre>
