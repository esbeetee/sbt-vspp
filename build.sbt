ThisBuild / organization := "io.github.esbeetee"
lazy val root = (project in file(".")).enablePlugins(SbtPlugin).settings(
  name := "sbt-consistent",
  version := "0.1.0",
  scriptedLaunchOpts := {
    scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
  },
  scriptedBufferLog := false,
  // TODO to remove, only for testing.
  publishM2Configuration:=publishM2Configuration.value.withOverwrite(true),
)

Global / onChangedBuildSource := ReloadOnSourceChanges


// settings and tasks for the sbt-consistent plugin
// TODO add here for test purpose for now, will migrate it proper src directory once the plugin works fine

// create the sbtCrossVersion for the current for plugin.
lazy val sbtCrossVersion = settingKey[String]("Scala and SBT cross version for plugin project")
lazy val sbtCrossArtifactName = settingKey[String]("Scala and SBT crossed name for plugin project")

sbtCrossVersion := {
  val scala = scalaBinaryVersion.value
  val sbt = sbtBinaryVersion.value
  s"${scala}_$sbt"
}

sbtCrossArtifactName:={
  val artifactName = artifact.value.name
  val crossVersion = sbtCrossVersion.value
  s"${artifactName}_$crossVersion"
}

lazy val makeProperPom = taskKey[File]("Make a proper POM for plugin project")
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
}

makeProperPom / artifactPath := {
  val originalPomFile = (makePom / artifactPath).value// crossVersion
  val fileName =  sbtCrossArtifactName.value + ".pom"
  originalPomFile.getParentFile / fileName
}

makeProperPom / artifact := {
  val name = (makePom / artifact).value.name
  val newName = sbtCrossArtifactName.value
  (makePom / artifact).value.withName(newName)
}

// add the new pom to the list of artifact to be published.
artifacts += (makeProperPom / artifact).value

// TODO, how to for loop this?
(publishM2 / packagedArtifacts) +=  ((Compile / packageBin / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageBin).value)
(publishM2 / packagedArtifacts) +=  ((Compile / packageDoc / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageDoc).value)
(publishM2 / packagedArtifacts) +=  ((Compile / packageSrc / artifact).value.withName(sbtCrossArtifactName.value) -> (Compile / packageSrc).value)
(publishM2 / packagedArtifacts) +=  ((makeProperPom / artifact).value -> makeProperPom.value)
// republish the jar to a new artifact.

// (publish / packagedArtifacts) += ((makeProperPom / artifact).value -> makeProperPom.value)


