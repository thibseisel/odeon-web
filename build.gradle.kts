import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.7.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

group = "com.github.thibseisel.music"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

fun isRunningOnWindows(): Boolean {
	val osName = System.getProperty("os.name")
			?: throw GradleException("Unable to detect current Operating System.")
	return osName.contains("Windows", ignoreCase = true)
}

val webDir = "$rootDir/web"

val requireNpm by tasks.registering(Exec::class) {
	description = "Checks that NPM (Node Package Manager) is installed."

	workingDir = file(webDir)

	val detectionCommand = "npm -v"
	if (isRunningOnWindows()) {
		commandLine("cmd", "/C", detectionCommand)
	} else {
		commandLine("sh", "-c", detectionCommand)
	}
}

val installAngularDependencies by tasks.registering(Exec::class) {
	description = "Installs dependencies required to build the Angular front-end application via NPM."
	dependsOn(requireNpm)

	inputs.file("$webDir/package.json")
	outputs.dir("$webDir/node_modules")

	workingDir = file(webDir)
	val installCommand = "npm install"
	if (isRunningOnWindows()) {
		commandLine("cmd", "/C", installCommand)
	} else {
		commandLine("sh", "-c", installCommand)
	}
}

val buildAngular by tasks.registering(Exec::class) {
	description = "Builds the Angular front-end application with Angular CLI."
	dependsOn(installAngularDependencies)

	inputs.file("$webDir/package-lock.json")
	inputs.dir("$webDir/src")
	outputs.dir("$webDir/dist")

	workingDir = file(webDir)

	val buildCommand = "npm run build -- --prod"
	if (isRunningOnWindows()) {
		commandLine("cmd", "/C", buildCommand)
	} else {
		commandLine("sh", "-c", buildCommand)
	}
}

val copyCompiledAngularApp by tasks.registering(Copy::class) {
	description = "Copy compiled Angular app to server public resources."
	dependsOn(buildAngular)

	from("$webDir/dist/odeon-web")
	into("$buildDir/resources/main/public")
}

tasks["bootJar"].dependsOn(copyCompiledAngularApp)
