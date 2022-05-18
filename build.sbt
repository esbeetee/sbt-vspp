ThisBuild / organization := "io.github.esbeetee"
lazy val root = (project in file(".")).enablePlugins(SbtPlugin).settings(
  name := "sbt-consistent",
  scriptedLaunchOpts := {
    scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
  },
  scriptedBufferLog := false,
)

Global / onChangedBuildSource := ReloadOnSourceChanges
