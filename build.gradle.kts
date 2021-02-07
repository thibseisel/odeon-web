/*
 * Copyright 2021 Thibault SEISEL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
allprojects {
    group = "com.github.thibseisel.music"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    group = "clean"
    description = "Delete the root build directory"

    delete(buildDir)
}

val stage by tasks.registering(Copy::class) {
    group = "build"
    description = "Build executable for deployment to Heroku"

    val server = project(":server")
    dependsOn("clean", ":server:bootJar")

    from("${server.buildDir}/libs")
    into("$buildDir/libs")
}
