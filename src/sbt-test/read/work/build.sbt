import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
// cannot use the addSbtPlugin method cause it will add additional attributes which causes problems resolving.
// we way want to use it as a global plugin when use it within enterprise.

// this should work because sbt-assembly is published consistently
// addSbtCons("com.eed3si9n" % "sbt-assembly" % "1.2.0")
// import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
addConsistentSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.2.0")

TaskKey[Unit]("check") := {
  val updateResult = update.result.value
  assert(updateResult.toEither.isRight, s"Expected success, got: ${updateResult}")
  ()
}
