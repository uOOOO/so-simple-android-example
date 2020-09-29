import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("kotlin-android-extensions")
}

group = "com.example"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    iosX64("ios") {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${Version.serialization}")
                implementation("io.ktor:ktor-client-core:${Version.ktor}")
                implementation("io.ktor:ktor-client-logging:${Version.ktor}")
                implementation("io.ktor:ktor-client-serialization:${Version.ktor}")
                implementation(project(":shared:domain"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.3.1")
                implementation("io.ktor:ktor-client-okhttp:${Version.ktor}")
                implementation("com.squareup.okhttp3:logging-interceptor:${Version.okhttp}")
            }
        }
        val androidTest by getting
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:${Version.ktor}")
            }
        }
        val iosTest by getting
    }
}

dependencies {
    // chucker
    debugImplementation("com.github.chuckerteam.chucker:library:${Version.chucker}")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:${Version.chucker}")
}

android {
    compileSdkVersion(Version.Android.compileSdkVersion)
    defaultConfig {
        minSdkVersion(Version.Android.minSdkVersion)
        targetSdkVersion(Version.Android.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)