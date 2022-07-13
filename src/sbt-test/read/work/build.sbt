import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
// cannot use the addSbtPlugin method cause it will add additional attributes which causes problems resolving.
// we way want to use it as a global plugin when use it within enterprise.

// this should work because sbt-assembly is published consistently
// addSbtCons("com.eed3si9n" % "sbt-assembly" % "1.2.0")
// import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
addConsistentSbtPlugin("com.scalawilliam.esbeetee" % "sbt-vspp" % "0.4.11")

TaskKey[Unit]("check") := {
  val updateResult = update.result.value
  assert(
    updateResult.toEither.isRight,
    s"Expected success, got: ${updateResult}",
  )
  updateResult.toEither.toSeq.flatMap(_.toVector)
    .find(_.toString.contains("sbt-vspp")) match {
    case None => sys
        .error(s"Could not find sbt-vspp artifact, have: ${updateResult}")
    case Some(artifact)
        if artifact.toString.contains(
          """sbt-vspp_2.12_1.0/0.4.11/sbt-vspp_2.12_1.0-0.4.11.jar""",
        ) => ()
    case Some(artifact) => sys.error(s"Unexpected artifact found: ${artifact}")
  }
}
