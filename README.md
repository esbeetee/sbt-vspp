# sbt-consistent

[![Join the chat at https://gitter.im/esbeetee/sbt-consistent](https://badges.gitter.im/esbeetee/sbt-consistent.svg)](https://gitter.im/esbeetee/sbt-consistent?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

SBT plug-in to enable Maven-compliant SBT plug-ins, especially for the corporate world.

# How this plug-in will be used

To an SBT project, add `project/project/plugins.sbt` (as it's a plug-in that loads before other plug-ins)

```
libraryDependencies += "io.github.esbeetee" %% "sbt-consistent" % "0.0.2"
```

Once this plug-in is added, automatically the consumer will be unable to load inconsistent plug-ins.
In order to verify this, we have an SBT plug-in test to verify, as per below;

https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html

# Run tests in `src/sbt-test`

```
sbt scripted
sbt "scripted read/work"
sbt "scripted read/fail"
sbt "scripted write/work"
```

# Target aspects of the plug-in

1. For consumers & for our test cases: disable loading of inconsistent plug-ins (rewrite `addSbtPlugin` to not include
   extra attributes, and use proper JAR file)
2. For producers, produce consistent POM+JAR file when publishing a plugin;
3. For producers, produce the consistent & inconsistent together so that plug-in authors simply need to add in one more
   dependency to support both new and world consumers