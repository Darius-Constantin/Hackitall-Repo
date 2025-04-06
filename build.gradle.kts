plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.4"
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
    implementation("com.github.adedayo.intellij.sdk:git4idea:142.1")
    implementation("io.socket:socket.io-client:2.0.1") {
        exclude(group = "org.json", module = "json") // Avoid JSON conflict
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1.7")
    type.set("IC")

    plugins.set(listOf("com.intellij.java"))
}

tasks {
    withType<JavaCompile> {
    }

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
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(17)
}