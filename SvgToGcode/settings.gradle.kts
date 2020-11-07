rootProject.name = "SvgToGcode"

pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    mavenCentral()
    jcenter()
  }
  plugins {
    id("com.github.amollberg.inkscapeexec") version "1.0"
  }
}

include("plugin")
