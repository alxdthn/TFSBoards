plugins {
    id(BuildPlugins.androidApplication)
    kotlin(BuildPlugins.kotlinAndroid)
    kotlin(BuildPlugins.kotlinAndroidExtensions)
    kotlin(BuildPlugins.kotlinKapt)
}

android {
    compileSdkVersion(AndroidSdk.compile)
    defaultConfig {
        applicationId = "com.alxdthn.tfsboards"
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
			isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.appCompat)
    implementation(Libraries.ktxCore)
    implementation(Libraries.fragment)
    implementation(Libraries.lifeCycle)

    //  View
    implementation(Libraries.constraintLayout)
    implementation(Libraries.material)
    implementation(Libraries.recyclerView)
    implementation(Libraries.dragListView)
    implementation(Libraries.gifDrawable)
    implementation(Libraries.glide)
    annotationProcessor(Libraries.glideCompiler)

    //  RX
    implementation(Libraries.rxKotlin)
    implementation(Libraries.rxAndroid)

    //  Retrofit
    implementation(Libraries.retrofit)
    implementation(Libraries.rxRetrofit)
    implementation(Libraries.gson)

    //  Dagger
    implementation(Libraries.dagger)
    kapt(Libraries.daggerCompiler)
    compileOnly(Libraries.javaxAnnotation)

    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.espresso)
    implementation(TestLibraries.reflect)
}