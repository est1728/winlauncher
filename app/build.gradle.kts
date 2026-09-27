plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.winlauncher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.winlauncher"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"
    }

    // กุญแจเซ็นแอปแบบตายตัว (ไฟล์ winlauncher.keystore.jks ที่ root โปรเจกต์)
    // ใช้กุญแจเดียวกันทุกครั้งที่ build ผ่าน GitHub Actions เพื่อให้ "ติดตั้งทับของเดิม = อัปเดต"
    // ได้จริง ไม่ต้องถอนแอปเก่าออกก่อน
    signingConfigs {
        create("stable") {
            storeFile = rootProject.file("winlauncher.keystore.jks")
            storePassword = "winlauncher123"
            keyAlias = "winlauncher"
            keyPassword = "winlauncher123"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("stable")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
}
