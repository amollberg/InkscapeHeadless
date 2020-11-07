plugins {
  `java-gradle-plugin`
  `maven-publish`
  id("org.jetbrains.kotlin.jvm").version("1.3.61")
}

repositories {
  jcenter()
}

project.version = "0.1"

gradlePlugin {
  val inkscapeExec by plugins.creating {
    id = "com.github.amollberg.inkscapeexec"
    implementationClass =
      "com.github.amollberg.inkscapeexec.InkscapeExecPlugin"
  }
}

publishing {
  publications {
    create<MavenPublication>("InkscapeExec") {
      groupId = "com.github.amollberg"
      artifactId = "inkscapeexec"
      version = project.version.toString()

      from(components["java"])
    }
  }
}
