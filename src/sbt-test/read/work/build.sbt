import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
// cannot use the addSbtPlugin method cause it will add additional attributes which causes problems resolving.
// we way want to use it as a global plugin when use it within enterprise.

// this should work because sbt-assembly is published consistently
// addSbtCons("com.eed3si9n" % "sbt-assembly" % "1.2.0")
// import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
addConsistentSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.2.0")
//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.2.0")

TaskKey[Unit]("check") := {
  val updateResult = update.result.value
  assert(
    updateResult.toEither.isRight,
    s"Expected success, got: ${updateResult}",
  )
  updateResult.toEither.toSeq.flatMap(_.toVector)
    .find(_.toString.contains("sbt-assembly")) match {
    case None => sys
        .error(s"Could not find sbt-assembly artifact, have: ${updateResult}")
    case Some(artifact)
        if artifact.toString.contains(
          """sbt-assembly_sbt1_2.12/1.2.0/sbt-assembly_sbt1_2.12-1.2.0.jar""",
        ) => ()
    case Some(artifact) => sys.error(s"Unexpected artifact found: ${artifact}")
  }
}
