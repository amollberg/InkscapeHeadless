package com.github.amollberg.svgtogcode

import com.github.amollberg.inkscapeheadless.InkscapeExec
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import java.io.File
import javax.inject.Inject

abstract class ConvertSvgToGcode
@Inject constructor(projectLayout: ProjectLayout) : InkscapeExec() {

  private val buildDir = projectLayout.buildDirectory

  private val defaultPrefsFile: File
    get() = File(buildDir.asFile.get().absolutePath + "/preferences.xml")

  @get:InputFile
  abstract var svgFile: File

  @get:OutputFile
  abstract var gcodeFile: File

  private fun writePreferences() {
    preferenceFile = preferenceFile ?: defaultPrefsFile

    InkscapePreferences(
      preferenceFile!!,
      gcodeDirectory = gcodeFile.parent,
      gcodeFilename = gcodeFile.name)
      .write()
  }

  override var operations: String = ""
    get() = """
      window-close
      file-open:$svgFile
      window-open
      verb:ZoomPage
      verb:ToolNode
      verb:EditSelectAll
      verb:ObjectToPath
      verb:ru.cnc-club.filter.gcodetools_tools_library_no_options_no_preferences.noprefs
      verb:ru.cnc-club.filter.gcodetools_orientation_no_options_no_preferences.noprefs
      verb:EditSelectAll
      verb:ru.cnc-club.filter.gcodetools_ptg.noprefs
      file-close
      quit-inkscape
      quit
      """

  override fun execute() {
    writePreferences()
    super.execute()
  }
}

private data class InkscapePreferences(
  private val xmlFile: File,
  var gcodeDirectory: String,
  var gcodeFilename: String
) {
  fun write() {
    assert(xmlFile.parentFile.mkdirs())
    xmlFile.writeText(
      """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <inkscape
    xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd"
    xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape"
    version="1">
    <group
    id="extensions"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.Zoffset="0"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.Zsafe="5"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.Zscale="1"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.active-tab="area_fill"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.area-fill-angle="45"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.area-fill-method="spiral"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.area-fill-shift="0"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.area-find-artefacts-diameter="1"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.area-inkscape-radius="50"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.area-tool-overlap="0.1"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.biarc-max-split-depth="4"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.biarc-tolerance="1"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.directory="../../../../../gcode"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.max-area-curves="40"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.min-arc-radius="0.05"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.path-to-gcode-order="subpath by subpath"
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.postprocessor=" "
    ru.cnc-club.filter.gcodetools_area_area_fill_area_artefacts_ptg.unit="G21 (All units in mm)"
    ru.cnc-club.filter.gcodetools_orientation_no_options_no_preferences.Zdepth="-1"
    ru.cnc-club.filter.gcodetools_orientation_no_options_no_preferences.Zsurface="0"
    ru.cnc-club.filter.gcodetools_orientation_no_options_no_preferences.unit="G21 (All units in mm)"
    ru.cnc-club.filter.gcodetools_ptg.Zoffset="-1"
    ru.cnc-club.filter.gcodetools_ptg.Zsafe="5"
    ru.cnc-club.filter.gcodetools_ptg.Zscale="1"
    ru.cnc-club.filter.gcodetools_ptg.__live_effect__="0"
    ru.cnc-club.filter.gcodetools_ptg.active-tab="path-to-gcode"
    ru.cnc-club.filter.gcodetools_ptg.biarc-max-split-depth="4"
    ru.cnc-club.filter.gcodetools_ptg.biarc-tolerance="1"
    ru.cnc-club.filter.gcodetools_ptg.directory="$gcodeDirectory"
    ru.cnc-club.filter.gcodetools_ptg.filename="$gcodeFilename"
    ru.cnc-club.filter.gcodetools_ptg.min-arc-radius="0.05"
    ru.cnc-club.filter.gcodetools_ptg.path-to-gcode-depth-function="1"
    ru.cnc-club.filter.gcodetools_ptg.path-to-gcode-order="subpath by subpath"
    ru.cnc-club.filter.gcodetools_ptg.path-to-gcode-sort-paths="1"
    ru.cnc-club.filter.gcodetools_ptg.postprocessor=" "
    ru.cnc-club.filter.gcodetools_ptg.unit="G21 (All units in mm)"
    ru.cnc-club.filter.gcodetools_tools_library_no_options_no_preferences.tools-library-type="cylinder cutter"
    ru.cnc-club.filter.gcodetools_ptg.add-numeric-suffix-to-filename="0" />
    </inkscape>
    """)
  }
}
