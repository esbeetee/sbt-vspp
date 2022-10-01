package sbtconsistent

import sbt.{Def, _}
import sbt.Keys._

object SbtConsistentPlugin extends AutoPlugin {
  // override def requires = sbt.plugins.SbtPlugin
  override def trigger = allRequirements
  val SbtCrossName = "2.12_1.0"

  object autoImport {
    lazy val sbtCrossArtifactName = settingKey[String]("Scala and SBT crossed name for plugin project")
    lazy val sbtCrossModuleName = settingKey[String]("Scala and SBT crossed module name for plugin project")
    lazy val makeProperPom = taskKey[File]("Make a proper POM for plugin project")
    lazy val moduleIdTransformer = settingKey[Seq[ModuleID=>ModuleID]]("List of transforms to add a plugin")
    lazy val consistentPluginArtifacts = taskKey[Map[Artifact, File]]("Packages all artifacts for consistently publishing.")
  }

  import autoImport._

  override lazy val globalSettings: Seq[Setting[_]] = Seq(
    moduleIdTransformer := Seq(
      (m: ModuleID) => {
        val sbtV = (pluginCrossBuild / sbtBinaryVersion).value
        val scalaV = (update / scalaBinaryVersion).value
        val originalName = m.name
        val name = originalName.stripSuffix(s"_$sbtV").stripSuffix(s"_$scalaV")
        m.withName(s"${name}_$SbtCrossName")
          .withExtraAttributes(Map.empty)
      }
    ),
  )

  override lazy val projectSettings: Seq[Setting[_]] = Seq(

      sbtCrossArtifactName := {
        val artifactName = artifact.value.name
        s"${artifactName}_$SbtCrossName"
      },

      makeProperPom := {
        val originalPomFile = makePom.value
        val fileNew = (makeProperPom / artifactPath).value
        val artifactName = artifact.value.name
        val crossedName = sbtCrossArtifactName.value

        IO.writeLines(
          fileNew,
          IO.readLines(originalPomFile)
            .map(_.replace(s"<artifactId>$artifactName</artifactId>", s"<artifactId>$crossedName</artifactId>"))
        )

        streams.value.log.info(s"Created a proper POM file: $fileNew")
        fileNew
      },

      makeProperPom / artifactPath := {
        val originalPomFile = (makePom / artifactPath).value// crossVersion
        val fileName =  sbtCrossArtifactName.value + ".pom"
        originalPomFile.getParentFile / fileName
      },

      makeProperPom / artifact := {
        val name = (makePom / artifact).value.name
        val newName = sbtCrossArtifactName.value
        (makePom / artifact).value.withName(newName)
      },

      // add the new pom to the list of artifact to be published.
      artifacts += (makeProperPom / artifact).value,

    consistentPluginArtifacts := Def.taskDyn {
      (if (sbtPlugin.value) Def.task {
        Map(
          (Compile / packageBin / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageBin).value,
          (Compile / packageDoc / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageDoc).value,
          (Compile / packageSrc / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageSrc).value,
          (makeProperPom / artifact).value -> makeProperPom.value)

      } else Def.task {
        Map.empty[Artifact, File]
      })
    }.value,
    packagedArtifacts ++= consistentPluginArtifacts.value,
    )

  // an method help to resolve dependencies with consistent POM.
  def addConsistentSbtPlugin(dependency: ModuleID): Setting[Seq[ModuleID]] = {
    libraryDependencies += {
      moduleIdTransformer.value.foldLeft(dependency) {
        (accumulator, func)=> func.apply(accumulator)
      }
    }
  }
}
