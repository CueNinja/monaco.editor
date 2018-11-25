import org.gradle.api.tasks.bundling.Jar

plugins {
    kotlin("jvm") version "1.3.10"
    `maven-publish`
    id("org.openjfx.javafxplugin") version "0.0.5"
}

javafx {
    modules = List(1) { "javafx.web" }
}

group = "ninja.cue"
version = "0.4-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib"))
}

publishing {
    publications {
        register(name=group as String, type=MavenPublication::class) {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("$buildDir/mvn-repo")
        }
    }
}

tasks {
    val yarn by registering(Exec::class) {
        inputs.file("$projectDir/package.json")
        inputs.file("$projectDir/websrc/package.json")
        inputs.file("$projectDir/websrc/yarn.lock")
        outputs.dir("$projectDir/websrc/node_modules")
        commandLine("yarn")
    }
    register(name="parcel", type=Exec::class) {
        inputs.file("$projectDir/package.json")
        inputs.file("$projectDir/websrc/package.json")
        inputs.dir("$projectDir/websrc")
        outputs.dir("$projectDir/src/main/resources/ninja/cue/views/monaco")
        dependsOn(yarn)
        commandLine("yarn", "dist")
    }
}

defaultTasks("build", "parcel", "publish")
