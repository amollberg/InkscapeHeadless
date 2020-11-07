import com.github.amollberg.svgtogcode.ConvertSvgToGcode

plugins {
  id("com.github.amollberg.svgtogcode") version "0.1"
}

tasks {
  register("demoConvertSvgToGcode", ConvertSvgToGcode::class) {
    svgFile = file("test.svg")
    gcodeFile = file("test.gcode")
  }

  register("build") {
    dependsOn("demoConvertSvgToGcode")
  }
}
