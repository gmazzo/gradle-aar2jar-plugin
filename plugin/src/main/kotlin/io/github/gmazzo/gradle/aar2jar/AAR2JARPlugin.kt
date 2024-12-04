package io.github.gmazzo.gradle.aar2jar

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE
import org.gradle.api.artifacts.type.ArtifactTypeDefinition.JAR_TYPE
import org.gradle.api.attributes.LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.add
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.registerTransform
import org.gradle.kotlin.dsl.the

/**
 * Allows consuming Android's AAR artifacts as JARs
 */
class AAR2JARPlugin : Plugin<Project> {

    companion object {
        const val AAR_TYPE = "aar"
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

            registerTransform(AAR2JARTransform::class) {
                from.attribute(ARTIFACT_TYPE_ATTRIBUTE, AAR_TYPE)
                to.attribute(ARTIFACT_TYPE_ATTRIBUTE, JAR_TYPE)
            }
        }
    }

}
