rootProject.name = "SvgToGcode"

pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    mavenCentral()
    jcenter()
  }
  plugins {
    id("com.github.amollberg.inkscapeheadless") version "0.1"
  }
}

include("plugin")
