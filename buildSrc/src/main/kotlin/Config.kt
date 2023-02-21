object Android {
    const val compileSdk = 33
    const val applicationId = "com.nsyw.composeark"
    const val minSdk = 23
    const val targetSdk = 33
    const val versionCode = 103000
    const val versionName = "10.3.0"
    const val composeVersion="1.3.1"
    const val composeCompilerVersion="1.4.2"
    const val kotlinCompiler="1.8.10"
}

object Dependencies {
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val convert_gson = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val gson = "com.google.code.gson:gson:2.9.0"
    const val coil = "io.coil-kt:coil-compose:2.2.2"
}

object AndroidX {
    const val core_ktx = "androidx.core:core-ktx:1.7.0"
    const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
    const val appcompat = "androidx.appcompat:appcompat:1.4.1"
    const val material = "com.google.android.material:material:1.5.0"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.3"

    //compose
    const val activity_compose = "androidx.activity:activity-compose:1.3.1"
    const val compose_ui = "androidx.compose.ui:ui:${Android.composeVersion}"
    const val compose_ui_tools_preview = "androidx.compose.ui:ui-tooling-preview:${Android.composeVersion}"
    const val compose_material = "androidx.compose.material:material:${Android.composeVersion}"
    const val compose_navigation = "androidx.navigation:navigation-compose:2.4.0-beta02"

    // accompanist辅助插件
    const val accompanist_systemuicontroller =
        "com.google.accompanist:accompanist-systemuicontroller:0.28.0"
    const val accompanist_insets = "com.google.accompanist:accompanist-insets:0.28.0"
    const val accompanist_pager = "com.google.accompanist:accompanist-pager:0.28.0"
    const val accompanist_pager_indicators =
        "com.google.accompanist:accompanist-pager-indicators:0.28.0"
//    const val accompanist_swipe_refresh = "com.google.accompanist:accompanist-swiperefresh:0.28.0"

    //jetpack

}