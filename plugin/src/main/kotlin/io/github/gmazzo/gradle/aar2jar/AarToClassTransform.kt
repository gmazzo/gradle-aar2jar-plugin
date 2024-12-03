package io.github.gmazzo.gradle.aar2jar

import org.gradle.api.artifacts.transform.CacheableTransform
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.nio.file.CopyOption
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.io.path.exists
import kotlin.io.path.outputStream
import kotlin.sequences.forEach

/**
 * Based on AGP's [com.android.build.gradle.internal.dependency.AarToClassTransform]
 */
@CacheableTransform
abstract class AarToClassTransform : TransformAction<AarToClassTransform.Params> {

    @get:InputArtifact
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    abstract val inputFile: Provider<FileSystemLocation>

    override fun transform(transformOutputs: TransformOutputs) {
        val forCompile = parameters.forCompileUse.get()
        val aar = FileSystems.newFileSystem(inputFile.get().asFile.toPath(), emptyMap<String, String>())
        val jar = aar.getPath("api.jar").takeIf { forCompile && it.exists() } ?: aar.getPath("classes.jar")

        if (jar.exists()) {
            val outputJar = transformOutputs
                .file("${inputFile.get().asFile.nameWithoutExtension}-${if (forCompile) "api" else "runtime"}.jar")
                .toPath()

            Files.copy(jar, outputJar)
        }
    }

    interface Params : TransformParameters {

        @get:Input
        val forCompileUse: Property<Boolean>

    }

}
