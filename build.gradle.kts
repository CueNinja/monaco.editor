plugins {
    kotlin("jvm") version "1.2.30"
    maven
}

group = "ninja.cue"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib"))
}

tasks {
    "uploadArchives"(Upload::class) {
        repositories {
            withConvention(MavenRepositoryHandlerConvention::class) {
                mavenDeployer {
                    withGroovyBuilder {
                        "repository"("url" to uri("$buildDir/mvn-repo"))
                    }
                    pom.project {
                        withGroovyBuilder {
                            "parent" {
                                "groupId"("ninja.cue")
                                "artifactId"("monaco.editor")
                                "version"("0.1-SNAPSHOT")

                            }
                            "licenses" {
                                "license" {
                                    "name"("The MIT License (MIT)")
                                    "url"("https://mit-license.org/license.txt")
                                    "distribution"("repo")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
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

defaultTasks("webpack", "uploadArchives")
