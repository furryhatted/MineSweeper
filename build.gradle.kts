import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    application
    id("org.openjfx.javafxplugin") version ("0.0.13")
}

group = "com.github.furryhatted"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
}

javafx {
    version = "18"
    modules = listOf("javafx.controls", "javafx.graphics", "javafx.media")
}

application {
    mainClass.set("com.github.furryhatted.MineSweeper")
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
    manifest {
        attributes["Main-Class"] = "com.github.furryhatted.MineSweeper"
    }
}
