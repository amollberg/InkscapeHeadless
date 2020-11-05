package com.github.amollberg.inkscapeheadless

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File

open class InkscapeExec : DefaultTask() {
  @get:Input
  open lateinit var operations: String

  @get:[InputFile Optional]
  open var preferenceFile: File? = null

  private val configDir: File get() = project.file(
    "${project.buildDir}/inkscapeexec/inkscape-config"
  )

  @TaskAction
  fun execute() {
    val stdin = operations.trimIndent()

    preferenceFile?.let {prefs ->
      project.logger.info("Using preference file: $prefs")
      project.copy {
        it.from(prefs)
        it.into(configDir)
        it.rename { "preferences.xml" }
      }
    }

    project.logger.info("Sending to Inkscape stdin: '$stdin'")
    project.exec {
      it.standardInput = java.io.ByteArrayInputStream(
        stdin.toByteArray()
      )
      it.executable =
        "${project.buildDir}/inkscapeexec/inkscape/bin/inkscape.exe"

      it.args("--with-gui", "--shell")
      it.environment(mapOf(
        "INKSCAPE_PROFILE_DIR" to configDir.absolutePath)
      )
    }.assertNormalExitValue()
  }
}
