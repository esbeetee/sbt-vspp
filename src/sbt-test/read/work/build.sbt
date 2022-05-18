// this should work because sbt-assembly is published consistently
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.2.0")
TaskKey[Unit]("check") := {
  val updateResult = update.result.value
  assert(updateResult.toEither.isRight, s"Expected success, got: ${updateResult}")
  ()
}
