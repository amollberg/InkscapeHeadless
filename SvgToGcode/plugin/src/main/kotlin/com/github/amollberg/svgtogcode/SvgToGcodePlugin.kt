package com.github.amollberg.svgtogcode

import com.github.amollberg.inkscapeheadless.InkscapeHeadlessPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class SvgToGcodePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.plugins.apply(InkscapeHeadlessPlugin::class.java)

    project.tasks.withType(ConvertSvgToGcode::class.java).configureEach {
      it.dependsOn("unzipInkscape")
    }
  }
}
