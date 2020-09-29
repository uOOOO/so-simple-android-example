buildscript {
    apply(from = "env.gradle")
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
        maven(url = "https://dl.bintray.com/ekito/koin")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Version.kotlin}")
        classpath("com.android.tools.build:gradle:${Version.agp}")
        classpath("org.koin:koin-gradle-plugin:${Version.koinMpp}")
    }
}
group = "com.example"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://dl.bintray.com/ekito/koin")
    }
}

tasks.register("clean").configure {
    delete(rootProject.buildDir)
}
