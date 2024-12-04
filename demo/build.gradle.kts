plugins {
    java
    jacoco
    `java-test-fixtures`
    alias(libs.plugins.kotlin.jvm)
    id("io.github.gmazzo.aar2jar")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get())

dependencies {
    compileOnly(libs.demo.android)
    testFixturesApi(libs.demo.android)

    implementation(libs.demo.androidx.fragment)
    testFixturesApi(libs.demo.androidx.browser)
    testImplementation(libs.demo.androidx.camera)

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
