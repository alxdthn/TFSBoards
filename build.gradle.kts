// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
	repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath (BuildPlugins.androidGradlePlugin)
        classpath (BuildPlugins.kotlinGradlePlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
	}
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://maven.google.com/")
            name = "Google"
        }
    }
}

tasks.register("clean").configure {
    delete("build")
}