plugins {
  id("org.jetbrains.kotlin.jvm").version("1.3.61")
}

repositories {
  jcenter()
}

dependencies {
  implementation(gradleApi())
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}
