package io.github.gmazzo.gradle.aar2jar

import org.gradle.api.artifacts.transform.CacheableTransform
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.util.zip.ZipFile

@CacheableTransform
abstract class AAR2JARTransform : TransformAction<TransformParameters.None> {

    @get:InputArtifact
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    abstract val inputFile: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val file = inputFile.get().asFile

        ZipFile(file).use { aar ->
            val jar = aar.getEntry("classes.jar") ?: return

            outputs
                .file("${file.nameWithoutExtension}.jar")
                .outputStream()
                .use(aar.getInputStream(jar)::copyTo)
        }
    }

}
