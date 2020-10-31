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

  register("copySourceSvg", Copy::class) {
    from(file("test.svg"))
    into("$buildDir/sources/")
  }

  val addGcodeToolsConfiguration by registering(Exec::class) {
    dependsOn(
      "unzipInkscape",
      "createInkscapeConfigDir",
      "copySourceSvg")
    val inputFile = "$buildDir/sources/test.svg"
    inputs.file(inputFile)

    val outputFile = "$buildDir/configured/test.svg"
    outputs.file(outputFile)

    standardInput = java.io.ByteArrayInputStream("""
      verb:ZoomPage
      verb:ToolNode
      verb:EditSelectAll
      verb:ObjectToPath
      verb:ru.cnc-club.filter.gcodetools_tools_library_no_options_no_preferences.noprefs
      verb:ru.cnc-club.filter.gcodetools_orientation_no_options_no_preferences.noprefs
      export-type:svg
      export-filename:$outputFile
      export-do
      file-close
      quit-inkscape
      quit
    """.trimIndent().toByteArray())
    executable = "$buildDir/inkscape/bin/inkscape.exe"
    args(inputFile, "--with-gui", "--shell")
    environment(
      "INKSCAPE_PROFILE_DIR" to file("$buildDir/config").absolutePath)
  }

  register("exportToGcode", Exec::class) {
    inputs.files(addGcodeToolsConfiguration.get().outputs)

    standardInput = java.io.ByteArrayInputStream("""
      verb:ZoomPage
      verb:EditSelectAll
      verb:ru.cnc-club.filter.gcodetools_ptg.noprefs
      file-close
      quit-inkscape
      quit
    """.trimIndent().toByteArray())
    executable = "$buildDir/inkscape/bin/inkscape.exe"
    args(inputs.files.singleFile, "--with-gui", "--shell")
    environment(
      "INKSCAPE_PROFILE_DIR" to file("$buildDir/config").absolutePath)
  }

  register("build") {
    dependsOn("exportToGcode")
  }
}
