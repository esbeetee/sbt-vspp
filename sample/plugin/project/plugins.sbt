resolvers+= "Artifactory" at "https://wudong.jfrog.io/artifactory/default-maven-local"

// local
addSbtPlugin("io.github.esbeetee" % "sbt-consistent" % "0.4.9-SNAPSHOT")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")
