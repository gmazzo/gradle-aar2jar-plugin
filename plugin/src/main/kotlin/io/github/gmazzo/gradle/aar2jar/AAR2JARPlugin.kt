package io.github.gmazzo.gradle.aar2jar

import io.github.gmazzo.gradle.aar2jar.agp.AarToClassTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JAR_TYPE
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.Usage.JAVA_API
import org.gradle.api.attributes.Usage.JAVA_RUNTIME
import org.gradle.api.attributes.Usage.USAGE_ATTRIBUTE
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.registerTransform
import org.gradle.kotlin.dsl.the

/**
 * Allows consuming Android's AAR artifacts as JARs
 */
public class AAR2JARPlugin : Plugin<Project> {

    public companion object {
        public const val AAR_TYPE: String = "aar"
    }

    override fun apply(target: Project): Unit = with(target) {
        apply(plugin = "java-base")

        fun setup(name: String) = configurations[name].attributes {
            attribute(ARTIFACT_TYPE_ATTRIBUTE, JAR_TYPE)
        }

        the<SourceSetContainer>().configureEach {
            setup(runtimeClasspathConfigurationName)
            setup(compileClasspathConfigurationName)
        }

        dependencies {
            attributesSchema.attribute(LIBRARY_ELEMENTS_ATTRIBUTE) {
                compatibilityRules.add(AARCompatibilityRule::class)
            }

            fun registerTransform(usage: Usage) = registerTransform(AarToClassTransform::class) {
                from.attribute(USAGE_ATTRIBUTE, usage)
                from.attribute(ARTIFACT_TYPE_ATTRIBUTE, AAR_TYPE)
                to.attribute(USAGE_ATTRIBUTE, usage)
                to.attribute(ARTIFACT_TYPE_ATTRIBUTE, JAR_TYPE)
                parameters.generateRClassJar.value(true)
                parameters.forCompileUse.value(usage.name == JAVA_API)
            }

            registerTransform(objects.named(JAVA_API))
            registerTransform(objects.named(JAVA_RUNTIME))
        }
    }

}
