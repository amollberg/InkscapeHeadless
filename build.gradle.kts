import org.example.amollberg.InkscapeHeadlessPlugin
import org.example.amollberg.InkscapeExec

apply<InkscapeHeadlessPlugin>()

tasks {
  val addGcodeToolsConfiguration by registering(InkscapeExec::class) {
    val inputFile = file("test.svg")
    inputs.files(inputFile)

    val outputFile = file("$buildDir/svg-configured/test.svg")
    outputs.file(outputFile)

    operations = """
      window-close
      file-open:$inputFile
      window-open
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
    """
  }

  register("exportToGcode", InkscapeExec::class) {
    inputs.files(addGcodeToolsConfiguration.get().outputs)
    doFirst {
      file("$buildDir/gcode").mkdirs()
    }

    val inputFile = inputs.files.filter { it.isFile }.singleFile
    operations = """
      window-close
      file-open:$inputFile
      window-open
      verb:ZoomPage
      verb:EditSelectAll
      verb:ru.cnc-club.filter.gcodetools_ptg.noprefs
      file-close
      quit-inkscape
      quit
    """
  }

  register("build") {
    dependsOn("exportToGcode")
  }
}
