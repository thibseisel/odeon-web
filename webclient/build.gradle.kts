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

val requireNpm by tasks.registering(Exec::class) {
    group = "angular"
    description = "Checks that NPM (Node Package Manager) is installed."

    workingDir = projectDir

    val detectionCommand = "npm -v"
    if (isRunningOnWindows()) {
        commandLine("cmd", "/C", detectionCommand)
    } else {
        commandLine("sh", "-c", detectionCommand)
    }
}

val installAngularDependencies by tasks.registering(Exec::class) {
    group = "angular"
    description =
        "Installs dependencies required to build the Angular front-end application via NPM."
    dependsOn(requireNpm)

    inputs.file("$projectDir/package.json")
    inputs.file("$projectDir/package-lock.json")
    outputs.dir("$projectDir/node_modules")

    workingDir = projectDir
    val installCommand = "npm install"
    if (isRunningOnWindows()) {
        commandLine("cmd", "/C", installCommand)
    } else {
        commandLine("sh", "-c", installCommand)
    }
}

val build by tasks.registering(Exec::class) {
    group = "angular"
    description = "Builds the Angular front-end application with Angular CLI."
    dependsOn(installAngularDependencies)

    inputs.dir("$projectDir/src")
    outputs.dir("$projectDir/dist")

    workingDir = projectDir

    val buildCommand = "npm run build -- --prod"
    if (isRunningOnWindows()) {
        commandLine("cmd", "/C", buildCommand)
    } else {
        commandLine("sh", "-c", buildCommand)
    }
}

fun isRunningOnWindows(): Boolean {
    val osName = System.getProperty("os.name")
        ?: throw GradleException("Unable to detect current Operating System.")
    return osName.contains("Windows", ignoreCase = true)
}
