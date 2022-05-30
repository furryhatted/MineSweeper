import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.openjfx.javafxplugin") version "0.0.13"
    kotlin("jvm") version "1.6.21"
    application
}

group = "com.github.furryhatted"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.openjfx:javafx-base:18.0.1")
    implementation("org.openjfx:javafx-controls:18.0.1")
    implementation("org.openjfx:javafx-media:18.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
}


javafx {
    version = "18.0.1"
    modules = listOf("javafx.controls", "javafx.media")
}

application {
    mainClass.set("com.github.furryhatted.Launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}


tasks.withType<JavaExec> {
    jvmArgs = listOf("-Xmx512M", "-Xss2M")
}

tasks.withType<Jar> {
    manifest { attributes["Main-Class"] = "com.github.furryhatted.Launcher" }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

