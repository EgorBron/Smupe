import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    // Sometimes plugin order matters!
    id("com.google.protobuf") version "0.9.4"
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.dokka")
}

android {
    namespace = "net.blusutils.smupe"
    compileSdk = 34
    flavorDimensions += "store_distribution"

    defaultConfig {
        applicationId = "net.blusutils.smupe"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 33
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resourceConfigurations += listOf("en", "ru")
    }

    buildTypes {
        debug {
            isPseudoLocalesEnabled = true
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    productFlavors {
        create("foss") {
            dimension = "store_distribution"
            applicationIdSuffix = ".foss"
            versionNameSuffix = "-FOSS"
        }
        create("playmarket") {
            dimension = "store_distribution"
            applicationIdSuffix = ".market"
            versionNameSuffix = "-MARKET"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    outputDirectory.set(file("../website/public/dokka/"))
    dokkaSourceSets {
        configureEach {
            documentedVisibilities.set(setOf(DokkaConfiguration.Visibility.PUBLIC))
            reportUndocumented.set(false)
            skipEmptyPackages.set(true)
            skipDeprecated.set(false)
            suppressGeneratedFiles.set(true)
        }
    }
}



dependencies {

    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    // Documentation
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.10")

    // AndroidX (Jetpack) dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
//    implementation(platform("androidx.compose:compose-bom:2023.06.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.0")

    // Room DB
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")


    implementation("com.squareup.okhttp3:okhttp:4.12.0") // HTTP client
    implementation("ru.gildor.coroutines:kotlin-coroutines-okhttp:1.0") // And Coroutines support for it

    implementation("com.squareup.moshi:moshi:1.14.0") // JSON parser
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    implementation("io.coil-kt:coil:2.5.0") // Image loader
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.5.0")

    implementation("io.github.aghajari:LazySwipeCards:1.0.1") // Swipeable Cards
//    implementation(project(":LazySwipeCards"))
    implementation("androidx.compose.foundation:foundation:1.6.1") // For... what?
    implementation("androidx.navigation:navigation-compose:2.7.7") // For NavController support
    implementation("org.burnoutcrew.composereorderable:reorderable:0.7.0") // For Modifier.reorderable

//    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
//    implementation(composeBom)
    implementation("androidx.compose.material:material-icons-extended") // Material icons (all)

    implementation("com.airbnb.android:lottie-compose:6.2.0") // Lottie

    implementation("androidx.browser:browser:1.7.0") // Chrome Custom Tabs

    implementation("androidx.datastore:datastore:1.0.0") // DataStore with ProtoBufs

    implementation("com.google.protobuf:protobuf-javalite:3.19.6") // ProtoBufs themselves

    implementation("com.jonathanfinerty.once:once:1.3.1") // For actions to do "Once"

    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha") // Accompanist Permissions

    implementation("ch.acra:acra-http:5.11.3") // Android Crash Reports via HTTP

    // Dev dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.9.2"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                // Configures the task output type
                create("java") {
                    // Java Lite has smaller code size and is recommended for Android
                    option("lite")
                }
            }
        }
    }
}