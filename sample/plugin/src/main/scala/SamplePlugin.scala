import sbt.{Def, *}
import sbt.Keys.*

object SamplePlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    lazy val sampleOfSomeString = "Sample value"
  }
}
