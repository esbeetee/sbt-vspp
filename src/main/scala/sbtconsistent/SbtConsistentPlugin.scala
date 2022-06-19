package sbtconsistent

import sbt.*
import sbt.Keys.*

object SbtConsistentPlugin extends AutoPlugin {
  // override def requires = sbt.plugins.SbtPlugin
  override def trigger = allRequirements

  object autoImport {
    //// create the sbtCrossVersion for the current for plugin.
    lazy val sbtCrossVersion = settingKey[String]("Scala and SBT cross version for plugin project")
    lazy val sbtCrossArtifactName = settingKey[String]("Scala and SBT crossed name for plugin project")
    lazy val makeProperPom = taskKey[File]("Make a proper POM for plugin project")
    lazy val moduleIdTransformer = settingKey[Seq[ModuleID=>ModuleID]]("List of transforms to add a plugin")
  }

  import autoImport.*

  override lazy val projectSettings: Seq[Setting[?]] = Seq(

    moduleIdTransformer := Seq(
      (m: ModuleID) => {
        val sbtV = (pluginCrossBuild / sbtBinaryVersion).value
        val scalaV = (update / scalaBinaryVersion).value
        val originalName = m.name;
        val name = originalName.stripSuffix(s"_$sbtV").stripSuffix(s"_$scalaV")
        m.withName(s"${name}_${scalaV}_${sbtV}" )
          .withExtraAttributes(Map.empty)
      }
    ),

    sbtCrossVersion := {
      val scala = scalaBinaryVersion.value
      val sbt = sbtBinaryVersion.value
      s"${scala}_$sbt"
    },

    sbtCrossArtifactName := {
      val artifactName = artifact.value.name
      val crossVersion = sbtCrossVersion.value
      s"${artifactName}_$crossVersion"
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

    publishM2 / packagedArtifacts := Map(
            ((Compile / packageBin / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageBin).value),
            ((Compile / packageDoc / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageDoc).value),
            ((Compile / packageSrc / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageSrc).value),
            ((makeProperPom / artifact).value -> makeProperPom.value)),

    publish / packagedArtifacts :=  (publishM2 / packagedArtifacts).value

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
