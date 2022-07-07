organization := "com.scalawilliam.esbeetee"
name := "sample-plugin"
version := "0.0.2"
enablePlugins(SbtPlugin)
enablePlugins(sbtconsistent.SbtConsistentPlugin)
publishMavenStyle := true
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")
publishTo := Some {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) "snapshots" at s"${nexus}content/repositories/snapshots"
  else "releases" at s"${nexus}service/local/staging/deploy/maven2"
}

isSnapshot := false

Test / packageDoc / publishArtifact := false

Compile / doc / sources := Seq.empty

description := "A sample plug-in to test dual mode publishing"
developers := List(
  Developer(
    "ScalaWilliam",
    "ScalaWilliam",
    "hello@scalawilliam.com",
    url("https://github.com/scalawilliam/"),
  ),
  Developer(
    "Wudong",
    "Wudong",
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
