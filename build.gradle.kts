plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.4"
//    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    kotlin("jvm")
}

group = "com.ctrlaltdefeat"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.json:json:20231013")
    implementation("com.openai:openai-java:0.44.2")
    implementation(kotlin("stdlib-jdk8"))
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1.7")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("com.intellij.java"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
    }
//    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//        kotlinOptions.jvmTarget = "17"
//    }

    patchPluginXml {
        sinceBuild.set("241")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

val javaFxVersion = "17.0.9" // or 21.0.2, etc.

val osName = when {
    org.gradle.internal.os.OperatingSystem.current().isWindows -> "win"
    org.gradle.internal.os.OperatingSystem.current().isMacOsX -> "mac"
    else -> "linux"
}

dependencies {
    implementation("org.openjfx:javafx-base:$javaFxVersion:$osName")
    implementation("org.openjfx:javafx-controls:$javaFxVersion:$osName")
    implementation("org.openjfx:javafx-graphics:$javaFxVersion:$osName")
    // Add more if needed: javafx-fxml, javafx-media, etc.
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.4.0")
    implementation("org.jetbrains.lets-plot:lets-plot-jfx:4.4.0") // OR lets-plot-swing
    implementation("org.jetbrains.lets-plot:lets-plot-batik:4.4.0")
    implementation("xerces:xercesImpl:2.12.2")
}
kotlin {
    jvmToolchain(17)
}