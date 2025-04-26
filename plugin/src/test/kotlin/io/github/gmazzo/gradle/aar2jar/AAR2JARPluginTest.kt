package io.github.gmazzo.gradle.aar2jar

import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.get
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AAR2JARPluginTest {

    @Test
    fun `can be applied with java plugin`() = with(ProjectBuilder.builder().build()) {
        apply(plugin = "java")
        apply(plugin = "io.github.gmazzo.aar2jar")
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource(
        "compileClasspath, runtime-1.1.1-api.jar|common-1.1.1.jar|support-annotations-26.1.0.jar",
        "runtimeClasspath, runtime-1.1.1-runtime.jar|common-1.1.1.jar|support-annotations-26.1.0.jar",
    )
    fun `can resolve AAR dependencies`(configuration: String, expectedArtifacts: String): Unit =
        with(ProjectBuilder.builder().build()) {
            apply(plugin = "java")
            apply(plugin = "io.github.gmazzo.aar2jar")

            repositories.mavenCentral()
            repositories.google()

            dependencies.add("implementation", "android.arch.core:runtime:1.1.1")

            val resolvedArtifacts = configurations[configuration].files.mapTo(mutableSetOf()) { it.name }

            assertEquals(expectedArtifacts.split('|').toSet(), resolvedArtifacts)
        }

}
