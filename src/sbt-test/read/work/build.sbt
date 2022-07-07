import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
// cannot use the addSbtPlugin method cause it will add additional attributes which causes problems resolving.
// we way want to use it as a global plugin when use it within enterprise.

// this should work because sbt-assembly is published consistently
// addSbtCons("com.eed3si9n" % "sbt-assembly" % "1.2.0")
// import sbtconsistent.SbtConsistentPlugin.addConsistentSbtPlugin
addConsistentSbtPlugin("io.github.esbeetee" % "sbt-consistent" % "0.4.7")

// currently the sbt-consistent plugin is published to the following artifactory.
// will change to maven center later.
resolvers+= "artifactory" at "https://wudong.jfrog.io/artifactory/default-maven-local"

TaskKey[Unit]("check") := {
  val updateResult = update.result.value
  assert(
    updateResult.toEither.isRight,
    s"Expected success, got: ${updateResult}",
  )
  updateResult.toEither.toSeq.flatMap(_.toVector)
    .find(_.toString.contains("sbt-consistent")) match {
    case None => sys
        .error(s"Could not find sbt-consistent artifact, have: ${updateResult}")
    case Some(artifact)
        if artifact.toString.contains(
          """sbt-consistent_2.12_1.0/0.4.7/sbt-consistent_2.12_1.0-0.4.7.jar""",
        ) => ()
    case Some(artifact) => sys.error(s"Unexpected artifact found: ${artifact}")
  }
}
