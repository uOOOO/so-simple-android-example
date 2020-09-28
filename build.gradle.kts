buildscript {
    apply(from = "env.gradle")
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
        classpath("com.android.tools.build:gradle:${Version.agp}")
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
    }
}

tasks.register("clean").configure {
    delete(rootProject.buildDir)
}
