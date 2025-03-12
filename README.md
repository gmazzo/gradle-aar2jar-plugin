![GitHub](https://img.shields.io/github/license/gmazzo/gradle-aar2jar-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.gmazzo.aar2jar/io.github.gmazzo.aar2jar.gradle.plugin)](https://central.sonatype.com/artifact/io.github.gmazzo.aar2jar/io.github.gmazzo.aar2jar.gradle.plugin)
[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.github.gmazzo.aar2jar)](https://plugins.gradle.org/plugin/io.github.gmazzo.aar2jar)
[![Build Status](https://github.com/gmazzo/gradle-aar2jar-plugin/actions/workflows/ci-cd.yaml/badge.svg)](https://github.com/gmazzo/gradle-aar2jar-plugin/actions/workflows/ci-cd.yaml)
[![Coverage](https://codecov.io/gh/gmazzo/gradle-aar2jar-plugin/branch/main/graph/badge.svg?token=D5cDiPWvcS)](https://codecov.io/gh/gmazzo/gradle-aar2jar-plugin)
[![Users](https://img.shields.io/badge/users_by-Sourcegraph-purple)](https://sourcegraph.com/search?q=content:io.github.gmazzo.aar2jar+-repo:github.com/gmazzo/gradle-aar2jar-plugin)

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
