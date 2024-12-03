package io.github.gmazzo.gradle.aar2jar

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.DIRECTORY_TYPE
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JAR_TYPE
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_CLASS_DIRECTORY
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JVM_RESOURCES_DIRECTORY
import org.gradle.api.attributes.AttributeCompatibilityRule
import org.gradle.api.attributes.CompatibilityCheckDetails
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.LibraryElements.CLASSES
import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.gradle.api.attributes.LibraryElements.JAR
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.LibraryElements.RESOURCES
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.Usage.JAVA_API
import org.gradle.api.attributes.Usage.JAVA_RUNTIME
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.registerTransform
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.named

/**
 * Allows consuming Android's AAR artifacts as JARs
 */
class AAR2JARPlugin : Plugin<Project> {

    companion object {
        const val AAR2JAR_TYPE = "aar2jar"
        const val AAR_TYPE = "aar"
    }

    override fun apply(target: Project): Unit = with(target) {
        apply(plugin = "java-base")

        fun setup(name: String) = configurations[name].attributes {
            attribute(ARTIFACT_TYPE_ATTRIBUTE, AAR2JAR_TYPE)
        }

        the<SourceSetContainer>().configureEach {
            setup(runtimeClasspathConfigurationName)
            setup(compileClasspathConfigurationName)
        }

        dependencies {
            attributesSchema {
                attribute(ARTIFACT_TYPE_ATTRIBUTE) { compatibilityRules.add(ArtifactTypeCompatibilityRule::class) }
                attribute(LIBRARY_ELEMENTS_ATTRIBUTE) { compatibilityRules.add(LibraryElementsCompatibilityRule::class) }
            }

            fun registerTransform(usage: Usage) = registerTransform(AarToClassTransform::class) {
                parameters.forCompileUse.set(usage.name == JAVA_API)
                from.attribute(USAGE_ATTRIBUTE, usage)
                from.attribute(ARTIFACT_TYPE_ATTRIBUTE, AAR_TYPE)
                to.attribute(USAGE_ATTRIBUTE, usage)
                to.attribute(ARTIFACT_TYPE_ATTRIBUTE, JAR_TYPE)
            }

            registerTransform(objects.named(JAVA_API))
            registerTransform(objects.named(JAVA_RUNTIME))
        }
    }

    class ArtifactTypeCompatibilityRule : AttributeCompatibilityRule<String> {

        private val compatibles = setOf(JAR_TYPE, JVM_CLASS_DIRECTORY, JVM_RESOURCES_DIRECTORY, DIRECTORY_TYPE)

        override fun execute(details: CompatibilityCheckDetails<String>) = with(details) {
            if (consumerValue == AAR2JAR_TYPE && producerValue in compatibles) {
                compatible()
            }
        }

    }

    class LibraryElementsCompatibilityRule : AttributeCompatibilityRule<LibraryElements> {

        private val compatibles = setOf(JAR, CLASSES, RESOURCES, CLASSES_AND_RESOURCES)

        override fun execute(details: CompatibilityCheckDetails<LibraryElements>) = with(details) {
            if (consumerValue?.name in compatibles && producerValue?.name == AAR_TYPE) {
                compatible()
            }
        }

    }

}