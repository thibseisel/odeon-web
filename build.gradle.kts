import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
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

val os = System.getProperty("os.name") ?: throw GradleException("Unable to detect OS name.")
val isWindows = os.contains("Windows", ignoreCase = true)
val webDir = "$rootDir/web"

tasks.register<Exec>("checkNpmIsInstalled") {
	description = "Checks that NPM (Node Package Manager) is installed."

	workingDir = file(webDir)

	if (isWindows) {
		commandLine("cmd", "/C", "npm -v")
	} else {
		commandLine("sh", "-c", "npm -v")
	}
}

tasks["build"].dependsOn("checkNpmIsInstalled")

tasks.register<Exec>("installAngularDependencies") {
	description = "Installs dependencies required to build the Angular front-end application via NPM."
	dependsOn("checkNpmIsInstalled")

	inputs.file("$webDir/package.json")
	outputs.dir("$webDir/node_modules")

	workingDir = file(webDir)
	commandLine("cmd", "/C", "npm install")
}

tasks.register<Exec>("buildAngular") {
	description = "Builds the Angular front-end application with Angular CLI."

	shouldRunAfter("installAngularDependencies")

	inputs.dir(webDir)
	outputs.dir("$webDir/dist")

	workingDir = file(webDir)
	commandLine("cmd", "/C", "npm run build -- --prod")
}
