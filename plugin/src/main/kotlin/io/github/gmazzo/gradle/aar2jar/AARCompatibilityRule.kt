package io.github.gmazzo.gradle.aar2jar

import io.github.gmazzo.gradle.aar2jar.AAR2JARPlugin.Companion.AAR_TYPE
import org.gradle.api.attributes.AttributeCompatibilityRule
import org.gradle.api.attributes.CompatibilityCheckDetails
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.LibraryElements.CLASSES
import org.gradle.api.attributes.LibraryElements.CLASSES_AND_RESOURCES
import org.gradle.api.attributes.LibraryElements.JAR
import org.gradle.api.attributes.LibraryElements.RESOURCES

public class AARCompatibilityRule : AttributeCompatibilityRule<LibraryElements> {

    private val compatibles = setOf(JAR, CLASSES, RESOURCES, CLASSES_AND_RESOURCES)

    override fun execute(details: CompatibilityCheckDetails<LibraryElements>): Unit = with(details) {
        if (consumerValue?.name in compatibles && producerValue?.name == AAR_TYPE) {
            compatible()
        }
    }

}
