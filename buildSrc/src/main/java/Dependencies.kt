import org.gradle.api.JavaVersion

object Config {
    const val application_id = "com.example.translator"
    const val compile_sdk = 30
    const val build_sdk = "30.0.3"
    const val min_sdk = 21
    const val target_sdk = 30
    const val jvm_target = "1.8"
    val java_version = JavaVersion.VERSION_1_8
}

object Releases {
    const val version_code = 1
    const val version_name = "1.0"
}

object Modules {
    const val app = ":app"
    const val core = ":core"
    const val model = ":model"
    const val repository = ":repository"
    const val utils = ":utils"
}

object Versions {
    const val multidex = "1.0.3"

    const val appcompat = "1.2.0"
    const val material = "1.3.0"
    const val constraint = "2.0.4"

    const val core = "1.3.2"
    const val stdlib = "1.4.30"
    const val coroutinesCore = "1.4.1"
    const val coroutinesAndroid = "1.4.1"

    const val retrofit = "2.9.0"
    const val converterGson = "2.7.1"
    const val interceptor = "3.12.1"
    const val adapterCoroutines = "0.9.2"

    const val koin = "2.0.1"

    const val glide = "4.9.0"
    const val glideCompiler = "4.9.0"

    const val picasso = "2.5.2"

    const val roomKtx = "2.2.0-alpha01"
    const val runtime = "2.2.0-alpha01"
    const val roomCompiler = "2.2.0-alpha01"

    const val jUnit = "4.12"
    const val runner = "1.2.0"
    const val espressoCore = "3.2.0"
    const val ext = "1.1.2"

    const val rxandroid = "2.1.0"
    const val rxjava = "2.2.8"

    const val dagger = "2.17"

    const val legacy = "1.0.0"

}

object Tools {
    const val multidex = "com.android.support:multidex:${Versions.multidex}"
}

object Design {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
}

object Kotlin {
    const val core = "androidx.core:core-ktx:${Versions.core}"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.stdlib}"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.converterGson}"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.interceptor}"
    const val adapter_coroutines =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.adapterCoroutines}"
}

object Koin {
    const val koin_android = "org.koin:koin-android:${Versions.koin}"
    const val koin_view_model = "org.koin:koin-android-viewmodel:${Versions.koin}"
}

object Glide {
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glideCompiler}"
}

object Picasso {
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.runtime}"
    const val compiler = "androidx.room:room-compiler:${Versions.roomCompiler}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.roomKtx}"
}

object TestImpl {
    const val junit = "junit:junit:${Versions.jUnit}"
    const val runner = "androidx.test:runner:${Versions.runner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val ext = "androidx.test.ext:junit:${Versions.ext}"
}

object RxJava {
    const val rxandroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}"
    const val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
}

object Dagger {
    const val android = "com.google.dagger:dagger-android:${Versions.dagger}"
    const val android_support = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val processor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
}

object Legacy {
    const val support = "androidx.legacy:legacy-support-v4:${Versions.legacy}"
} 