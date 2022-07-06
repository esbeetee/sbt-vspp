import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
// cannot use the addSbtPlugin method cause it will add additional attributes which causes problems resolving.
// we way want to use it as a global plugin when use it within enterprise.

lazy val currentV = "0.0.412345156"

ThisBuild / version := currentV

lazy val testPlugin = project.enablePlugins(SbtPlugin)
  .enablePlugins(sbtconsistent.SbtConsistentPlugin).settings(
    organization := "com.test",
    name := "test-plugin",
    publishMavenStyle := true,
  )

lazy val usePlugin = project.settings(
  addSbtPlugin("com.test" % "test-plugin" % currentV),
  resolvers += Resolver.mavenLocal,
)

TaskKey[Unit]("pub") := (testPlugin / publishM2).value

TaskKey[Unit]("check") := {
  val updateResult = (usePlugin / update).result.value
  assert(updateResult.toEither.isRight, s"Expected success, got: $updateResult")
}

