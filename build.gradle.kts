
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

tasks.register("prepare") {
  dependsOn(configurations["compile"])
  doLast {
    println(configurations["compile"].map { file -> file.name})
  }
}
