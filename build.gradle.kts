plugins {
    kotlin("jvm") version "2.4.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.2.20")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}