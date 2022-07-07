import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
// cannot use the addSbtPlugin method cause it will add additional attributes which causes problems resolving.
// we way want to use it as a global plugin when use it within enterprise.

// cannot use a random number here, it appear each step in the test script,
// starts with a new sbt process, cause we got different random number between publish / check.
lazy val currentV = "0.0.312345156"

ThisBuild / version := currentV

lazy val testPlugin = project.enablePlugins(SbtPlugin)
  .enablePlugins(sbtconsistent.SbtConsistentPlugin).settings(
    organization := "com.test",
    name := "test-plugin",
    publishMavenStyle := true,
  )

lazy val usePlugin = project.settings(
  addConsistentSbtPlugin("com.test" % "test-plugin" % currentV),
  resolvers += Resolver.mavenLocal,
)

TaskKey[Unit]("pub") := (testPlugin / publishM2).value

TaskKey[Unit]("check") := {
  val updateResult = (usePlugin / update).result.value
  assert(updateResult.toEither.isRight, s"Expected success, got: $updateResult")
  updateResult.toEither.toSeq.flatMap(_.toVector)
    .find(_.toString.contains("test-plugin")) match {
    case None => sys
        .error(s"Could not find test-plugin artifact, have: $updateResult")
    case Some(artifact)
        if artifact.toString.contains(
          s"""test-plugin_2.12_1.0/${currentV}/test-plugin_2.12_1.0-${currentV}.jar""",
        ) => ()
    case Some(artifact) => sys.error(s"Unexpected artifact found: $artifact")
  }
}
