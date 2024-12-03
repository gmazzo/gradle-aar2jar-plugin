import java.lang.Thread.sleep

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    id("io.github.gmazzo.aar2jar")
    jacoco
}

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get())

dependencies {
    implementation(libs.androidx.fragment)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports.xml.required = true
}
