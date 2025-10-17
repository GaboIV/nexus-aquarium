import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.security.crypto)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            
            // Serialization
            implementation(libs.kotlinx.serialization.json)
            
            // Coil for images
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.cio)
        }
    }
}

android {
    namespace = project.findProperty("app.package.name") as String? ?: "com.nexusaquarium"
    compileSdk = (project.findProperty("app.compile.sdk") as String? ?: libs.versions.android.compileSdk.get()).toInt()

    defaultConfig {
        applicationId = project.findProperty("app.package.name") as String? ?: "com.nexusaquarium"
        minSdk = (project.findProperty("app.min.sdk") as String? ?: libs.versions.android.minSdk.get()).toInt()
        targetSdk = (project.findProperty("app.target.sdk") as String? ?: libs.versions.android.targetSdk.get()).toInt()
        versionCode = (project.findProperty("app.version.code") as String? ?: "1").toInt()
        versionName = project.findProperty("app.version.name") as String? ?: "0.0.1"
        
        // Set app name from properties
        resValue("string", "app_name", project.findProperty("app.name") as String? ?: "Nexus Aquarium")
        resValue("string", "app_display_name", project.findProperty("app.display.name") as String? ?: "Nexus Aquarium")
        resValue("string", "app_description", project.findProperty("app.description") as String? ?: "Nexus Aquarium - Fish Management System")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.nexusaquarium.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = project.findProperty("app.package.name") as String? ?: "com.nexusaquarium"
            packageVersion = project.findProperty("app.version.name") as String? ?: "0.0.1"
            description = project.findProperty("app.description") as String? ?: "Nexus Aquarium - Fish Management System"
            vendor = project.findProperty("app.company.name") as String? ?: "Nexus Aquarium"
        }
    }
}
