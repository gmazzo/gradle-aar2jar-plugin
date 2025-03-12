plugins {
    alias(libs.plugins.publicationsReport)
    base
    `maven-publish`
}

val pluginBuild = gradle.includedBuild("plugin")

tasks.build {
    dependsOn(pluginBuild.task(":$name"))
}

tasks.check {
    dependsOn(pluginBuild.task(":$name"))
}

tasks.publish {
    dependsOn(pluginBuild.task(":$name"))
}

tasks.publishToMavenLocal {
    dependsOn(pluginBuild.task(":$name"))
}
