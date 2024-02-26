package net.blusutils.smupe.data.repos

import net.blusutils.smupe.R
import net.blusutils.smupe.data.licenses.LicenseInResources
import net.blusutils.smupe.data.licenses.LicensedObject

// Never ever do like this
object LicensesRepo {

    private val gh = "https://github.com"
    val apache = R.string.apache2_license_title
    val apache_text = R.string.apache2_license_text_appendix
    private val aosp = "2024 The Android Open Source Project"
    private val jb = "2024 JetBrains"
    private val googleProto = "2008 Google Inc."
    private val google = "2024 Google Inc."
    private val square = "2019 Square, Inc."
    private val coil = "2023 Coil Contributors"
    private val gildor = "2019  gildor (Andrey Mischenko)"
    private val once = "2021 Jon Finerty"
    private val reorderable = "2022 André Claßen"
    private val swipecards = "2023 Amir Hossein Aghajari"
    private val android_repo = "https://android.googlesource.com/platform/frameworks/support/+/refs/heads"
    private val aosp_jetpack = "$android_repo/androidx-main/LICENSE.txt"
    private val aosp_room = "$android_repo/android-room-release/LICENSE.txt"

    // TODO use Flows
    val licenses = listOf(
        LicensedObject(
            "Kotlin",
            LicenseInResources(
                apache, 0, 0, apache_text, jb,
                "$gh/JetBrains/kotlin/blob/master/license/LICENSE.txt"
            )
        ),
        LicensedObject(
            "AndroidX",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Coil",
            LicenseInResources(
                apache, 0, 0, apache_text,
                coil, "$gh/coil-kt/coil/blob/master/LICENSE.txt"
            )
        ),
        LicensedObject(
            "Protocol Buffers",
            LicenseInResources(
                R.string.protobufs_license_title,
                0,
                0,
                R.string.protobufs_license_text,
                googleProto,
                "$gh/protocolbuffers/protobuf/blob/main/LICENSE"
            )
        ),
        LicensedObject(
            "Material Icons",
            LicenseInResources(
                apache, 0, 0, apache_text, google,
                "$gh/google/material-design-icons/blob/master/LICENSE"
            )
        ),
        LicensedObject(
            "ComponentActivity, AppCompatActivity",
            LicenseInResources(apache, 0, 0, apache_text, google, aosp_jetpack)
        ),
        LicensedObject(
            "Jetpack Compose Libraries BOM",
            LicenseInResources(apache, 0, 0, apache_text, google, aosp_jetpack)
        ),
        LicensedObject(
            "Compose Foundation",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Compose UI",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Compose Navigation",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Compose Graphics",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Compose UI Preview Tooling",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Compose Material3 Components",
            LicenseInResources(apache, 0, 0, apache_text, google, aosp_jetpack)
        ),
        LicensedObject(
            "AndroidX Test Library",
            LicenseInResources(apache, 0, 0, apache_text, google, aosp_jetpack)
        ),
        LicensedObject(
            "Compose Testing For JUnit4",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Compose Tooling",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Compose Testing Manifest Dependency",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "Room Database",
            LicenseInResources(
                apache, 0, 0, apache_text, aosp, aosp_room
            )
        ),
        LicensedObject(
            "Proto DataStore",
            LicenseInResources(apache, 0, 0, apache_text, aosp, aosp_jetpack)
        ),
        LicensedObject(
            "KSP API",
            LicenseInResources(
                apache, 0, 0, apache_text,
                google, "$gh/google/ksp/blob/main/LICENSE"
            )
        ),
        LicensedObject(
            "LazySwipeCards",
            LicenseInResources(
                apache, 0, 0, apache_text,
                swipecards, "$gh/Aghajari/LazySwipeCards/blob/main/LICENSE"
            )
        ),
        LicensedObject(
            "OkHttp",
            LicenseInResources(
                apache, 0, 0, apache_text,
                square, "$gh/square/okhttp/blob/master/LICENSE.txt"
            )
        ),
        LicensedObject(
            "Moshi",
            LicenseInResources(
                apache, 0, 0, apache_text,
                square, "$gh/square/moshi/blob/master/LICENSE.txt"
            )
        ),
        LicensedObject(
            "Kotlin Coroutines OkHttp",
            LicenseInResources(
                apache, 0, 0, apache_text,
                gildor, "$gh/gildor/kotlin-coroutines-okhttp/blob/master/LICENSE.txt"
            )
        ),
        LicensedObject(
            "Once",
            LicenseInResources(
                apache, 0, 0, apache_text,
                once, "$gh/jonfinerty/Once/blob/master/LICENSE.txt"
            )
        ),
        LicensedObject(
            "Compose LazyList/Grid reorder",
            LicenseInResources(
                apache, 0, 0, apache_text,
                reorderable, "$gh/aclassen/ComposeReorderable/blob/main/LICENSE"
            )
        )
    )
}