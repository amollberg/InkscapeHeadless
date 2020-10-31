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
  ivy {
    url = uri("dependencies/")
    patternLayout {
      artifact("/[module][revision]-extra.[ext]")
    }
    metadataSources { artifact() }
  }
}

dependencies {
  "compile"("org.inkscape:inkscape:1.0.1:x64@7z")
  "compile"("org.7-zip:7z:1900@zip")
}

tasks {
  register("unzip7z", Copy::class) {
    dependsOn(configurations["compile"])
    val sevenZipArchive = configurations["compile"].find {
      it.name.startsWith("7z")
    }!!

    from(zipTree(sevenZipArchive))
    into(file("$buildDir/7z"))
  }

  register("unzipInkscape", Exec::class) {
    dependsOn("unzip7z")

    outputs.upToDateWhen { file("$buildDir/inkscape/bin/inkscape.exe").exists() }

    val inkscapeArtifact = configurations["compile"].find {
      it.name.startsWith("inkscape")
    }!!

    executable = "$buildDir/7z/7za.exe"
    args(
      "x", inkscapeArtifact.absolutePath, "-y",
      "-o${buildDir.absolutePath}"
    )
  }

  register("createInkscapeConfigDir", Copy::class) {
    from(file("config"))
    into("$buildDir/config/")
  }

  register("build", Exec::class) {
    dependsOn(
      "unzipInkscape",
      "createInkscapeConfigDir")

    executable = "$buildDir/inkscape/bin/inkscape.exe"
    args(
      "test.svg",
      "--verb", "EditSelectAll",
      "--export-filename", "test.png"
    )
    environment(
      "INKSCAPE_PROFILE_DIR" to file("$buildDir/config").absolutePath)
  }

}
