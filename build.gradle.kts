import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    application
    id("org.openjfx.javafxplugin") version ("0.0.13")
}

group = "com.github.furryhatted"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.github.furryhatted.MineSweeper")
}

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
    modules = listOf("javafx.controls")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
}