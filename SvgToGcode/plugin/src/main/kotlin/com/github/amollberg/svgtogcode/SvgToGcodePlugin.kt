package com.github.amollberg.svgtogcode

import com.github.amollberg.inkscapeexec.InkscapeExecPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class SvgToGcodePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.plugins.apply(InkscapeExecPlugin::class.java)

    project.tasks.withType(ConvertSvgToGcode::class.java).configureEach {
      it.dependsOn("unzipInkscape")
    }
  }
}
