import eu.emundo.gradle.sevenz.UnSevenZ

buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("gradle.plugin.eu.emundo:7z-gradle-plugin:1.0.5")
  }
}

configurations {
  create("compile")
}

repositories {
  // Repo for a single file in a public HTTPS repo, no metadata files required
  ivy {
    url = uri("https://media.inkscape.org/")
    patternLayout {
      artifact("/dl/resources/file/[module]-[revision]-[classifier].[ext]")
    }
    metadataSources { artifact() }
  }
}

dependencies {
  "compile"("org.inkscape:inkscape:1.0.1:x64@7z")
}

tasks.register("unzipInkscape", UnSevenZ::class) {
  dependsOn(configurations["compile"])
  val zipPath = configurations["compile"].find { it.isFile }!!

  sourceFile = zipPath
  outputDir = file("$buildDir/inkscape")

  doLast {
    println(outputs.files.map { it.absolutePath })
  }
}

tasks.register("build") {
  dependsOn("unzipInkscape")
}
