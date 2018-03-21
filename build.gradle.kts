import org.gradle.api.tasks.bundling.Jar

plugins {
    kotlin("jvm") version "1.2.30"
    `maven-publish`
}

group = "ninja.cue"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib"))
}

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(java.sourceSets["main"].allSource)
}

publishing {
    repositories {
        maven {
            url = uri("$buildDir/mvn-repo")
        }
    }
    (publications) {
        "mavenJava"(MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

tasks {
    "yarn"(Exec::class) {
        inputs.file("package.json").withPathSensitivity(PathSensitivity.RELATIVE)
        inputs.file("websrc/package.json").withPathSensitivity(PathSensitivity.RELATIVE)
        inputs.file("websrc/yarn.lock").withPathSensitivity(PathSensitivity.RELATIVE)
        outputs.dir("websrc/node_modules")
        commandLine("yarn")
    }
    "webpack"(Exec::class) {
        inputs.file("package.json").withPathSensitivity(PathSensitivity.RELATIVE)
        inputs.file("websrc/package.json").withPathSensitivity(PathSensitivity.RELATIVE)
        inputs.dir("websrc").withPathSensitivity(PathSensitivity.RELATIVE)
        outputs.dir("src/main/resources/ninja/cue/views/monaco")
        dependsOn("yarn")
        commandLine("yarn", "run", "dist")
    }
}

defaultTasks("webpack", "publish")
