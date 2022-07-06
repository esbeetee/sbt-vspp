resolvers+= "Artifactory" at "https://wudong.jfrog.io/artifactory/default-maven-local"

libraryDependencies += {
  "io.github.esbeetee" % "sbt-consistent_2.12_1.0" % "0.4.5"
}

addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")