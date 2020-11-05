package com.github.amollberg.inkscapeheadless

import groovy.util.XmlParser
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec

public class InkscapeHeadlessPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    // Repo for a single file in a public HTTPS repo, no metadata files required
    project.repositories.add(project.repositories.ivy {
      it.url = project.uri("https://media.inkscape.org/")
      it.patternLayout {
        it.artifact(
          "/dl/resources/file/[module]-[revision]-[classifier].[ext]"
        )
      }
      it.metadataSources { it.artifact() }
    })
    project.repositories.add(project.repositories.ivy {
      it.url = project.uri("dependencies/")
      it.patternLayout {
        it.artifact("/[module][revision]-extra.[ext]")
      }
      it.metadataSources { it.artifact() }
    })
    val compileConf =
      project.configurations.create("inkscapeexec-compile") { c ->
        c.defaultDependencies { d ->
          d.add(project.dependencies.create("org.7-zip:7z:1900@zip"))
          d.add(project.dependencies.create("org.inkscape:inkscape:1.0.1:x64@7z"))
        }
      }

    val buildDir = project.file("${project.buildDir}/inkscapeexec")
    project.tasks.register("unzip7z", Copy::class.java) {
      it.dependsOn(compileConf)
      val sevenZipArchive = compileConf.find {
        it.name.startsWith("7z")
      }!!
      it.from(project.zipTree(sevenZipArchive))
      it.into("$buildDir/7z")
    }

    project.tasks.register("unzipInkscape", Exec::class.java) {
      it.dependsOn("unzip7z")

      it.outputs.upToDateWhen {
        project.file("$buildDir/inkscape/bin/inkscape.exe").exists()
      }

      val inkscapeArtifact = compileConf.find {
        it.name.startsWith("inkscape")
      }!!

      it.executable = "$buildDir/7z/7za.exe"
      it.args(
        "x", inkscapeArtifact.absolutePath, "-y",
        "-o${buildDir.absolutePath}"
      )
    }

    project.tasks.withType(InkscapeExec::class.java).configureEach {
      it.dependsOn(
        "unzipInkscape",
        "createInkscapeConfigDir"
      )
    }
  }
}
