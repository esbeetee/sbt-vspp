ThisBuild / organization := "com.scalawilliam.esbeetee"

enablePlugins(SbtPlugin)

name := "sbt-vspp"

scriptedLaunchOpts := {
  scriptedLaunchOpts.value ++
    Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}

scriptedBufferLog := false

Global / onChangedBuildSource := ReloadOnSourceChanges

description := "SBT plugin for publishing maven compatible POM for SBT plugins"
developers := List(
  Developer(
    "ScalaWilliam",
    "ScalaWilliam",
    "hello@scalawilliam.com",
    url("https://github.com/scalawilliam/"),
  ),
  Developer(
    "wudong",
    "Wudong Liu",
    "wudong@users.noreply.github.com",
    url("https://github.com/wudong"),
  ),
)

scmInfo := Some(ScmInfo(
  url("https://github.com/esbeetee/sbt-consistent"),
  "git@github.com:esbeetee/sbt-consistent.git",
))

homepage := Some(url("https://github.com/esbeetee/sbt-consistent"))
GlobalScope / licenses += "Apache License 2.0" ->
  url("https://github.com/spray/sbt-revolver/raw/master/LICENSE")

publishMavenStyle := true
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")

publishTo := Some {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) "snapshots" at s"${nexus}content/repositories/snapshots"
  else "releases" at s"${nexus}service/local/staging/deploy/maven2"
}
