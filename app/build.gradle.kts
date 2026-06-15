import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.ksp)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun secret(key: String): String =
    localProperties.getProperty(key) ?: project.findProperty(key)?.toString().orEmpty()

android {
    namespace = "be.mauricedeke.shinkai"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "be.mauricedeke.shinkai"
        minSdk = 34
        targetSdk = 36
        versionCode = (project.findProperty("versionCode") as? String)?.toInt() ?: 1        // Firebase
        versionName = "1.0-b${(project.findProperty("versionCode") as? String) ?: "local"}" // Humans

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "AMQP_USERNAME", "\"${secret("AMQPusername")}\"")
        buildConfigField("String", "AMQP_PASSWORD", "\"${secret("AMQPpassword")}\"")
        buildConfigField("String", "AMQP_URL", "\"${secret("AMQPurl")}\"")
        buildConfigField("String", "AMQP_EXCHANGE", "\"${secret("AMQPexchange")}\"")
        buildConfigField("String", "AMQP_VHOST", "\"${secret("AMQPvhost")}\"")
        buildConfigField("String", "AMQP_PUBLISH_ROUTING_KEY", "\"${secret("AMQPpublishroutingkey")}\"")
        buildConfigField("String", "AMQP_SUBSCRIBE_ROUTING_KEY", "\"${secret("AMQPsubscriberoutingkey")}\"")

        buildConfigField("String", "MAPBOX_PUBLIC_TOKEN", "\"${secret("MAPBOX_PUBLIC_TOKEN")}\"")
        buildConfigField("String", "MAPBOX_ACCESS_TOKEN", "\"${secret("MAPBOX_ACCESS_TOKEN")}\"")

        buildConfigField("String", "API_BASE_URL", "\"${secret("API_BASE_URL")}\"")



    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/io.netty.versions.properties"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.material.icons.core)

    // Navigation
    implementation(libs.androidx.compose.navigation)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // DataStore
    implementation(libs.androidx.preferences.datastore)

    //APIs
    implementation(libs.coil.compose)
    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Messaging
    implementation(libs.rabbitmq.amqp.client)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Location & Geofencing
    implementation(libs.play.services.location)

    // MapBox
    implementation(libs.mapbox.maps)
    implementation(libs.mapbox.maps.compose)

    // FireBase
    implementation(platform("com.google.firebase:firebase-bom:34.14.0"))
    implementation("com.google.firebase:firebase-analytics")


}