plugins {
    java
    jacoco
    `java-test-fixtures`
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    id("io.github.gmazzo.aar2jar")
}

group = "io.github.gmazzo.aar2jar.demo"
version = "0.1.0"

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

publishing.publications.register<MavenPublication>("maven") {
    from(components["java"])
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports.xml.required = true
}

tasks.build {
    dependsOn(tasks.publishToMavenLocal)
}
