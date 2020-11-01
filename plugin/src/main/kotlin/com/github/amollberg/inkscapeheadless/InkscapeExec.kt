package com.github.amollberg.inkscapeheadless

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class InkscapeExec : DefaultTask() {
  @get:Input
  open lateinit var operations: String

  @TaskAction
  fun execute() {
    val stdin = operations.trimIndent()
    project.logger.info("Sending to Inkscape stdin: '$stdin'")
    project.exec {
      it.standardInput = java.io.ByteArrayInputStream(
        stdin.toByteArray()
      )
      it.executable =
        "${project.buildDir}/inkscapeexec/inkscape/bin/inkscape.exe"

      it.args("--with-gui", "--shell")
      it.environment(mapOf(
        "INKSCAPE_PROFILE_DIR" to project.file(
          "${project.buildDir}/inkscapeexec/inkscape-config").absolutePath)
      )
    }.assertNormalExitValue()
  }
}
