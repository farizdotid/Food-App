plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
}

android {
    compileSdkVersion(AppConfig.compileSdk)

    defaultConfig {
        applicationId = "org.codejudge.application"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions(AppConfig.dimension)
    productFlavors {
        create("staging") {
            applicationIdSuffix = ".staging"
            setDimension(AppConfig.dimension)
            buildConfigField("String", "BASE_URL", "\"https://maps.googleapis.com\"")
            buildConfigField(
                "String",
                "API_KEY_MAPS",
                "\"AIzaSyDxVclNSQGB5WHAYQiHK-VxYKJelZ_9mjk\""
            )
        }

        create("production") {
            setDimension(AppConfig.dimension)
            buildConfigField("String", "BASE_URL", "\"https://maps.googleapis.com\"")
            buildConfigField(
                "String",
                "API_KEY_MAPS",
                "\"AIzaSyDxVclNSQGB5WHAYQiHK-VxYKJelZ_9mjk\""
            )
        }
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }

    buildFeatures {
        buildConfig = true
    }

    packagingOptions {
        exclude("META-INF/notice.txt")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    //std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //app libs
    implementation(AppDependencies.appLibraries)
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    //test libs
    testImplementation(AppDependencies.testLibraries)

    kapt(AppDependencies.compilerLibraries)

    androidTestImplementation(AppDependencies.androidTestLibraries)

    debugImplementation(AppDependencies.chuckerDebug)
    releaseImplementation(AppDependencies.chuckerRelease)
}