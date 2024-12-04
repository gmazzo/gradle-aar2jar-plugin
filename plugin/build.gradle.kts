plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.samReceiver)
    alias(libs.plugins.gradle.pluginPublish)
    jacoco
}

group = "io.github.gmazzo.aar2jar"
description = "AAR dependency support for Java Plugin"
version = providers
    .exec { commandLine("git", "describe", "--tags", "--always") }
    .standardOutput.asText.get().trim().removePrefix("v")

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
samWithReceiver.annotation(HasImplicitReceiver::class.qualifiedName!!)

gradlePlugin {
    website.set("https://github.com/gmazzo/gradle-aar2jar-plugin")
    vcsUrl.set("https://github.com/gmazzo/gradle-aar2jar-plugin")

    plugins {
        create("publications-report") {
            id = "io.github.gmazzo.aar2jar"
            displayName = name
            implementationClass = "io.github.gmazzo.gradle.aar2jar.AAR2JARPlugin"
            description = "Adds AAR dependency support for Java"
            tags.addAll("android", "aar", "jar", "java", "aar2jar", "aar-jar")
        }
    }
}

dependencies {
    compileOnly(gradleKotlinDsl())

    testImplementation(gradleKotlinDsl())
    testImplementation(gradleTestKit())

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)
}

tasks.publish {
    dependsOn(tasks.publishPlugins)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports.xml.required = true
}
