rootProject.name = "kafka-workshops"

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        maven {
            name = "JCenter Gradle Plugins"
            url = uri("https://dl.bintray.com/gradle/gradle-plugins")
        }
    }
}