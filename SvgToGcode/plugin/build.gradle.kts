import com.github.amollberg.inkscapeexec.InkscapeExec

plugins {
  `java-gradle-plugin`
  `maven-publish`
  java

  id("org.jetbrains.kotlin.jvm").version("1.3.61")
  id("com.github.amollberg.inkscapeexec")
}

repositories {
  jcenter()
  mavenLocal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation(gradleApi())
  implementation("com.github.amollberg:inkscapeexec:1.0")
}

project.version = "1.0"

gradlePlugin {
  val svgToGcode by plugins.creating {
    id = "com.github.amollberg.svgtogcode"
    implementationClass =
      "com.github.amollberg.svgtogcode.SvgToGcodePlugin"
  }
}

publishing {
  publications {
    create<MavenPublication>("SvgToGcode") {
      groupId = "com.github.amollberg"
      artifactId = "svgtogcode"
      version = project.version.toString()

      from(components["java"])
    }
  }
}
