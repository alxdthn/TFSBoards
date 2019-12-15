const val kotlinVersion = "1.3.61"

object BuildPlugins {

	object Versions {
		const val buildToolsVersions = "3.5.3"
	}

	const val androidGradlePlugin		= "com.android.tools.build:gradle:${Versions.buildToolsVersions}"
	const val kotlinGradlePlugin		= "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
	const val androidApplication		= "com.android.application"
	const val kotlinAndroid				= "android"
	const val kotlinKapt				= "kapt"
	const val kotlinAndroidExtensions	= "android.extensions"
}

object AndroidSdk {
	const val min		= 22
	const val compile	= 29
	const val target	= compile
}

object Libraries {

	object Versions {
		const val jetpack			= "1.1.0"
		const val ktx				= "1.1.0"
		const val fragment			= "1.2.0-rc02"
		const val lifeCycle			= "1.1.1"

		const val constraintLayout	= "1.1.3"
		const val material			= "1.0.0"
		const val recyclerView		= "1.1.0"
		const val glide				= "4.9.0"
		const val dragListView		= "1.6.6"
		const val gifDrawable		= "1.2.19"

		const val dagger			= "2.25.2"
		const val rxKotlin			= "2.4.0"
		const val rxAndroid			= "2.1.1"
		const val rxRetrofit		= "2.4.0"
		const val retrofit			= "2.6.0"
		const val gson				= retrofit

		const val javaxAnnotations	= "3.1.1"
	}

	const val kotlinStdLib		= "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
	const val appCompat			= "androidx.appcompat:appcompat:${Versions.jetpack}"
	const val ktxCore			= "androidx.core:core-ktx:${Versions.ktx}"
	const val fragment			= "androidx.fragment:fragment-ktx:${Versions.fragment}"
	const val lifeCycle			= "android.arch.lifecycle:extensions:${Versions.lifeCycle}"

	const val constraintLayout	= "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
	const val material			= "com.google.android.material:material:${Versions.material}"
	const val recyclerView		= "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
	const val glide				= "com.github.bumptech.glide:glide:${Versions.glide}"
	const val glideCompiler		= "com.github.bumptech.glide:compiler:${Versions.glide}"
	const val dragListView		= "com.github.woxthebox:draglistview:${Versions.dragListView}"
	const val gifDrawable		= "pl.droidsonroids.gif:android-gif-drawable:${Versions.gifDrawable}"

	const val rxKotlin			= "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
	const val rxAndroid			= "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"

	const val rxRetrofit		= "com.squareup.retrofit2:adapter-rxjava2:${Versions.rxRetrofit}"
	const val retrofit			= "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
	const val gson				= "com.squareup.retrofit2:converter-gson:${Versions.gson}"

	const val dagger			= "com.google.dagger:dagger:${Versions.dagger}"
	const val daggerCompiler	= "com.google.dagger:dagger-compiler:${Versions.dagger}"
	const val javaxAnnotation	= "org.glassfish:javax.annotation:${Versions.javaxAnnotations}"
}

object TestLibraries {

	private object Versions {
		const val junit4 = "4.12"
		const val espresso = "3.1.0"
		const val reflect = "1.3.50"
	}

	const val junit4	= "junit:junit:${Versions.junit4}"
	const val espresso	= "androidx.test.espresso:espresso-core:${Versions.espresso}"
	const val reflect	= "org.jetbrains.kotlin:kotlin-reflect:${Versions.reflect}"
}
