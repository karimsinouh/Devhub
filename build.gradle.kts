// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val compose_version by extra("1.0.1")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-rc01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        classpath ("com.google.gms:google-services:4.3.8")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.38.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}