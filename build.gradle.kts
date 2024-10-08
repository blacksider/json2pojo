plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.snart.idea.plugin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.2.6")
    type.set("IC")
    downloadSources.set(true)
    plugins.set(listOf("com.intellij.java"))
}

dependencies {
    implementation("org.jsonschema2pojo:jsonschema2pojo-core:1.2.2")
//    implementation("org.jetbrains.kotlin:kotlin-stdlib")
//    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

//    test {
//        useJUnitPlatform()
//    }

    runIde {
        autoReloadPlugins.set(false)
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
//        certificateChainFile.set(file(System.getenv("CERTIFICATE_CHAIN")))
//        privateKeyFile.set(file(System.getenv("PRIVATE_KEY")))
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
        channels = listOf("beta")
    }
}
