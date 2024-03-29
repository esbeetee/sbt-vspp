# 🚩🚩🟠 This plug-in is no longer needed as of Mar 9 2023. 🚩 SWITCH TO SBT 1.9.0-M1+ 🟠🚩🚩

# sbt-vspp - Valid SBT POM Plugin [![Join the chat at https://gitter.im/esbeetee/sbt-consistent](https://badges.gitter.im/esbeetee/sbt-consistent.svg)](https://gitter.im/esbeetee/sbt-consistent?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**_Please read this README in full_** - as you would never experience such an issue if you had not worked in enterprise environments.

SBT plugins don't work at many companies, making Scala a lot harder to use there (no coverage tools, scalafmt plugins,
sbt-native-packager, sbt-revolver, Scala.js, ...).

This is because SBT publishes them with an invalid POM, and security scanning systems don't understand
the invalid/inconsistent POM.

This plug-in appends a valid POM to your SBT plugin publishing process, thereby enabling SBT & Scala to be fully
utilised in large companies (think tens of thousands of software engineers).

# How to use this plug-in

To an SBT plug-in project, add `project/plugins.sbt`. We will be hugely grateful

```
addSbtPlugin("com.scalawilliam.esbeetee" % "sbt-vspp" % "0.4.11")
```

Once you add this one-line change to your plug-in, and publish it, you have our eternal gratitude because this is really
saving Scala. Please, of course, inform us, so we can share the good news!

![image](https://user-images.githubusercontent.com/2464813/178601503-ca92bd0d-088d-4cdb-8bcb-9b87519dc33d.png)



# Background

SBT since its beginning published plugins to its own 'Ivy' repository, but after Bintray was shut down in 2021, they had
to be published to Maven Central. The 'Ivy' convention was kept to keep compatibility.

A consistent/valid artifact is one that has the filename of POM as `<artifactId>-<version>.pom`, however SBT originally
publishes it a little differently from the standard. See two examples below:

## Example of an inconsistent/invalid POM

Maven Central / Sonatype accept invalid/inconsistent artifacts. For example here, the suffix that includes SBT and Scala
version, is missing in the `.pom` filename. Sadly, this does not follow the convention, and security scanning packages
will not fetch the JAR files of plug-ins, so you cannot use them in enterprise :anguished:.

![image](https://user-images.githubusercontent.com/2464813/178597966-df210914-bb8e-41c0-b9d0-7fd1cca5b0f8.png)

## Example of a consistent/valid POM

Because the artifact ID matches the `.pom` file, this is downloadable in enterprise environments :heavy_check_mark:.

![image](https://user-images.githubusercontent.com/2464813/178598043-37f5c4cb-f2d8-4066-943d-c10d806d3be5.png)

## This plug-in

This plug-in enables you to publish in *both* ways at the same time. If you are a publisher, see the example published
plug-in here, which can be accessed in enterprise environments:

- https://repo1.maven.org/maven2/com/scalawilliam/esbeetee/sample-plugin_2.12_1.0/0.0.2/

:four_leaf_clover: This plug-in *does not modify your original JAR files*; all it literally does is add an extra set of
files, that follow the Maven convention.

# How to use this plugin (and validly published plugins) in an enterprise

`addSbtPlugin` adds additional metadata that forces a fetch of the invalid format - so instead so you can use this
plugin as follows, in `project/plugins.sbt`:

```
// this plugin
libraryDependencies += "com.scalawilliam.esbeetee" % "sbt-vspp_2.12_1.0" % "0.4.11"

// a sample plugin
libraryDependencies += "com.scalawilliam.esbeetee" % "sample-plugin_2.12_1.0" % "0.0.2"
```

## How to use when testing locally

Publish your plug-in to your `.m2` with `publishM2`, and also add `resolvers += Resolver.mavenLocal` to `project/plugins.sbt` then consume it. This is mostly useful for double-checking and validation when you have a more complex set-up -- so is only included for completeness.

# Why hasn't this been done in SBT?

It is planned to be fixed in SBT 2.0; this plugin is a stop-gap between SBT 1.0 and SBT 2.0 to enable SBT plugins in
enterprises today.

Eugene Yukota has documented his idea on how to do it: https://eed3si9n.com/pom-consistency-for-sbt-plugins/
This idea is good in the long run, but right now for simplicity and compatibility, without having to migrate everything,
adding additional files with the expected/standard filenames - just works.

Eugene's article was prompted by this long GitHub issue discussion: https://github.com/sbt/sbt/issues/3410

However, it is possible for us to adapt this plug-in to Eugene's approach as well. Let us know.

Ideally this plug-in is made redundant and integrated into mainline SBT after enough testing and feedback.

# Are you definitely sure this can't be fixed by applying some configuration options?

Yes. Often it is not a case of configuration: the changes that would need to be made (whether for vendor or for in-house interceptors) are rather comprehensive to implement, ie see this https://github.com/sbt/sbt-maven-resolver/blob/master/src/main/java/org/apache/maven/repository/internal/SbtArtifactDescriptorReader.java and also this https://github.com/sbt/sbt/pull/1793/files

Justifying and getting this sort of changes approved is a multi-month effort (from experience) -- if it were for a hugely popular language it would be easier but Scala often doesn't exceed even 1% of an org's developer headcount. There's also a Catch 22: people gravitate to tools that work out of the box, thus keeping that %-age down. Especially relevant to Scala.js which only easily works nicely with SBT (and IMO is far more suitable to enterprise apps than any other front-end stack out there).

# What about plugins that depend on other plugins?

This is an edge case that is not supported due to potential resolution and conflict issues. VSPP is really about providing an interim fix for enabling the key parts of Scala/SBT to even be functional in enterprise environments -- before SBT 2 fixes the issue fully. Without these key plugins, users run out of  alternatives, and in fact it is much simpler for them to just move to use Kotlin/TypeScript instead. sbt-crossproject, sbt-web and playframework would be the main ones, however we are not targeting those as alternatives exist.

# Authors

- @Wudong
- @ScalaWilliam
