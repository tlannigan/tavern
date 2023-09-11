import de.nilsdruyen.gradle.ftp.UploadExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("kapt") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("de.nilsdruyen.gradle-ftp-upload-plugin") version "0.4.0"
}

group = "com.tlannigan"
version = "1.0-SNAPSHOT"

val targetJavaVersion = 17
val minecraftApiVersion = "1.20"
val commandApiVersion = "9.1.0"
val daggerVersion = "2.46.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.md5lukas.de/releases")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation(kotlin("test"))
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    // MongoDB Kotlin
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("org.mongodb:bson-kotlinx:4.10.1")

    // CommandAPI
    compileOnly("dev.jorel:commandapi-bukkit-core:$commandApiVersion")
    compileOnly("dev.jorel:commandapi-annotations:$commandApiVersion")
    kapt("dev.jorel:commandapi-annotations:$commandApiVersion")
    implementation("dev.jorel:commandapi-bukkit-shade:$commandApiVersion")

    // Skedule
    implementation("de.md5lukas:skedule:2.0.0")
    implementation("de.md5lukas:schedulers:1.0.1")
}

tasks {
    processResources {
        val props = linkedMapOf(
            "version" to version,
            "minecraftApiVersion" to minecraftApiVersion
        )
        filteringCharset = "UTF-8"
        filesMatching("*plugin.yml") {
            expand(props)
        }
    }
    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")
        relocate("dev.jorel.commandapi", "com.tlannigan.tavern.commandapi")
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = targetJavaVersion.toString()
    }
    uploadFilesToFtp {
        dependsOn(shadowJar)
    }
    configure<UploadExtension> {
        host = providers.gradleProperty("host").get()
        port = providers.gradleProperty("port").get().toIntOrNull() ?: 22
        username = providers.gradleProperty("username").get()
        password = providers.gradleProperty("password").get()
        sourceDir = project.buildDir.toString() + providers.gradleProperty("sourceDir").get()
        targetDir = providers.gradleProperty("targetDir").get()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}

kotlin {
    jvmToolchain(targetJavaVersion)
}
