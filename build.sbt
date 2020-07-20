organization in ThisBuild := "com.note"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.1"

val lagomPb = "io.superflat" %% "lagompb-core" % "0.4.0"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8" % Test
val akkaDiscovery = "com.typesafe.akka" %% "akka-discovery" % "2.6.5"

lazy val `note-taking-service` = (project in file("."))
  .aggregate(
    `note-api`,
    `note`,
    `note-common`
  )

lazy val `note-common` = (project in file("note-common"))
  .settings(
    name := "note-common",
    libraryDependencies ++= Seq(
      lagomScaladslApi
    ),
    PB.protoSources in Compile := Seq(file("note-common/src/main/protobuf")),
    PB.targets in Compile := Seq(scalapb.gen() -> (sourceManaged in Compile).value)
  )

lazy val `note-api` = (project in file("note-api"))
  .settings(
    name := "note-api",
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomPb
    )
  )
  .dependsOn(`note-common`)

lazy val `note` = (project in file("note"))
  .enablePlugins(LagomScala)
  .settings(
    name := "note",
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      lagomPb,
      macwire,
      scalaTest,
      akkaDiscovery,
      lagomScaladslAkkaDiscovery
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`note-api`, `note-common`)
