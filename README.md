# sbt-vspp - Valid SBT POM Plugin [![Join the chat at https://gitter.im/esbeetee/sbt-consistent](https://badges.gitter.im/esbeetee/sbt-consistent.svg)](https://gitter.im/esbeetee/sbt-consistent?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

SBT plugins don't work at many companies, making Scala a lot harder to use there (no coverage tools, scalafmt plugins, sbt-native-packager, sbt-revolver, Scala.js).

This is because SBT publishes them with an invalid POM, and security systems don't understand the invalid/inconsistent POM.

This plug-in appends a valid POM to your SBT plugin publishing process, thereby enabling SBT & Scala to be fully utilised in large companies (think tens of thousands of software engineers).

# How to use this plug-in

To an SBT plug-in project, add `project/plugins.sbt`.

```
addSbtPlugin("com.scalawilliam.esbeetee" % "sbt-vspp" % "0.4.10")
```

# Background

SBT since its beginning published plugins to its own 'Ivy' repository, but after Bintray was shut down in 2021, they had to be published to Maven Central. The 'Ivy'  convention was kept to keep compatibility.

A consistent/valid artifact is one that has the filename of POM as `<artifactId>-<version>.pom`, however SBT originally publishes it a little differently from the standard. See two examples below:

## Example of an inconsistent/invalid POM

Maven Central / Sonatype accept invalid/inconsistent artifacts. For example here, the suffix that includes SBT and Scala version, is missing in the `.pom` filename. Sadly, this does not follow the convention, and security scanning packages will not fetch the JAR files of plug-ins, so you cannot use them in enterprise :anguished:.

![image](https://user-images.githubusercontent.com/2464813/178597966-df210914-bb8e-41c0-b9d0-7fd1cca5b0f8.png)

## Example of a consistent/valid POM

Because the artifact ID matches the `.pom` file, this is downloadable in enterprise environments :heavy_check_mark:.

![image](https://user-images.githubusercontent.com/2464813/178598043-37f5c4cb-f2d8-4066-943d-c10d806d3be5.png)

## This plug-in

This plug-in enables you to publish in *both* ways at the same time. If you are a publisher, see the example published plug-in here, which can be accessed in enterprise environments:

- https://repo1.maven.org/maven2/com/scalawilliam/esbeetee/sample-plugin_2.12_1.0/0.0.2/

:four_leaf_clover: This plug-in *does not modify your original JAR files*; all it literally does is add an extra set of files, that follow the Maven convention.

# How to use this plugin (and validly published plugins) in an enterprise

`addSbtPlugin` adds additional metadata that forces a fetch of the invalid format - so instead so you can use this plugin as follows:

```
// this plugin
libraryDependencies += "com.scalawilliam.esbeetee" % "sbt-vspp_2.12_1.0" % "0.4.10"

// a sample plugin
libraryDependencies += "com.scalawilliam.esbeetee" % "sample-plugin_2.12_1.0" % "0.0.2"
```

# Why hasn't this been done in SBT?

It is planned to be fixed in SBT 2.0; this plugin is a stop-gap between SBT 1.0 and SBT 2.0 to enable Scala in enterprises.
