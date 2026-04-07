// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false

    // 👇 ADICIONA ISSO
    id("org.sonarqube") version "7.2.3.7755"
}

// 👇 ADICIONA ISSO
sonar {
    properties {
        property("sonar.projectKey", "carlosaoribeiro_theclosetselect")
        property("sonar.organization", "carlosaoribeiro")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}