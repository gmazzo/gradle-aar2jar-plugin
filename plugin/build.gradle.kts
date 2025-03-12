plugins {
    alias(libs.plugins.importClasses)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.samReceiver)
    alias(libs.plugins.dokka)
    alias(libs.plugins.axion.release)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.gradle.pluginPublish)
    alias(libs.plugins.publicationsReport)
    jacoco
}

group = "io.github.gmazzo.aar2jar"
description = "Adds AAR dependency support for Java"
version = scmVersion.version

java.toolchain.languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
samWithReceiver.annotation(HasImplicitReceiver::class.qualifiedName!!)

val originUrl = providers
    .exec { commandLine("git", "remote", "get-url", "origin") }
    .standardOutput.asText.map { it.trim() }

gradlePlugin {
    website = originUrl
    vcsUrl = originUrl

    plugins {
        create("publications-report") {
            id = "io.github.gmazzo.aar2jar"
            displayName = name
            implementationClass = "io.github.gmazzo.gradle.aar2jar.AAR2JARPlugin"
            description = project.description
            tags.addAll("android", "aar", "jar", "java", "aar2jar", "aar-jar")
        }
    }
}

mavenPublishing {
    publishToMavenCentral("CENTRAL_PORTAL", automaticRelease = true)

    pom {
        name = "${rootProject.name}-${project.name}"
        description = provider { project.description }
        url = originUrl

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/license/mit/"
            }
        }

        developers {
            developer {
                id = "gmazzo"
                name = id
                email = "gmazzo65@gmail.com"
            }
        }

        scm {
            connection = originUrl
            developerConnection = originUrl
            url = originUrl
        }
    }
}

importClasses {
    repackageTo = "io.github.gmazzo.gradle.aar2jar.agp"
    keep("com.android.build.gradle.internal.dependency.AarToClassTransform")
    keep("com.android.build.gradle.internal.dependency.AarToClassTransform\$Params")
    include("**/*.class")
    option("-keepclassmembers enum com.android.resources.ResourceType { *; }")
    option("-keepclassmembers,allowobfuscation enum * { *; }")
}

dependencies {
    fun plugin(plugin: Provider<PluginDependency>) =
        plugin.get().run { "$pluginId:$pluginId.gradle.plugin:$version" }

    compileOnly(gradleKotlinDsl())

    importClasses(plugin(libs.plugins.android))
    importClassesLibraries(plugin(libs.plugins.kotlin.jvm))
    importClassesLibraries(gradleApi())

    testImplementation(gradleKotlinDsl())
    testImplementation(gradleTestKit())

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports.xml.required = true
}

afterEvaluate {
    tasks.named<Jar>("javadocJar") {
        from(tasks.dokkaGeneratePublicationJavadoc)
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    mustRunAfter(tasks.publishPlugins)
}

tasks.publishPlugins {
    enabled = "$version".matches("\\d+(\\.\\d+)+".toRegex())
}

tasks.publish {
    dependsOn(tasks.publishPlugins)
}
