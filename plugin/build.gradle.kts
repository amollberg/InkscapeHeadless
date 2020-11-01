plugins {
  `java-gradle-plugin`
  `maven-publish`
  id("org.jetbrains.kotlin.jvm").version("1.3.61")
}

repositories {
  jcenter()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

project.version = "0.1"

gradlePlugin {
  val inkscapeHeadless by plugins.creating {
    id = "com.github.amollberg.inkscapeheadless"
    implementationClass =
      "com.github.amollberg.inkscapeheadless.InkscapeHeadlessPlugin"
  }
}

publishing {
  publications {
    create<MavenPublication>("InkscapeHeadless") {
      groupId = "com.github.amollberg"
      artifactId = "inkscapeheadless"
      version = project.version.toString()

      from(components["java"])
    }
  }
}
