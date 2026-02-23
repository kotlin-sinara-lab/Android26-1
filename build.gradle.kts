plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // CSV parsing
    implementation("com.opencsv:opencsv:5.9")

    // Kandy visualisation
    implementation("org.jetbrains.kotlinx:kandy-lets-plot:0.7.0")
    implementation("org.jetbrains.lets-plot:lets-plot-batik:4.3.3")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-kernel:4.7.3")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("io.mockk:mockk:1.13.10")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("MainKt")
}
