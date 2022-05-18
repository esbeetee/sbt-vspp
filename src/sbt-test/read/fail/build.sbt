// this should fail because this is not published consistently
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.9.3")

TaskKey[Unit]("check") := {
  val updateResult = update.result.value
  assert(updateResult.toEither.isLeft, s"Expected failure, got: ${updateResult}")
  ()
}
