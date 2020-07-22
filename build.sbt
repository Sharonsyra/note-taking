import com.lightbend.lagom.core.LagomVersion

enablePlugins(DockerComposePlugin)
dockerImageCreationTask := (Docker / publishLocal in `note`).value

organization in ThisBuild := "com.sharonsyra"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.1"

val lagomPb = "io.superflat" %% "lagompb-core" % "0.4.0"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8" % Test
val lagomScaladslAkkaDiscovery = "com.lightbend.lagom" %% "lagom-scaladsl-akka-discovery-service-locator" % LagomVersion.current
val akkaDiscovery = "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % "1.0.5"
val akkaServiceLocator = "com.lightbend.lagom" %% "lagom-scaladsl-akka-discovery-service-locator" % LagomVersion.current

lagomCassandraEnabled in ThisBuild := false

lazy val `note-taking-service` = (project in file("."))
  .aggregate(
    `note-api`,
    `note`,
    `note-common`
  )
  .settings(
    publishArtifact := false,
    skip in publish := true
  )

lazy val `note-common` = (project in file("note-common"))
  .enablePlugins(ProtoRuntime)
  .settings(
    name := "note-common",
    PB.protoSources in Compile := Seq(file("note-common/src/main/protobuf")),
    PB.targets in Compile := Seq(scalapb.gen() -> (sourceManaged in Compile).value)
  )

lazy val `note-api` = (project in file("note-api"))
  .enablePlugins(LagomAkka)
  .enablePlugins(LagomApi)
  .settings(
    name := "note-api",
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomPb
    )
  )
  .dependsOn(`note-common`)

lazy val `note` = (project in file("note"))
  .enablePlugins(LagomScala, JavaAgent)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(PlayAkkaHttp2Support)
  .enablePlugins(LagomImpl)
  .enablePlugins(LagomAkka)
  .settings(
    name := "note"
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`note-api`, `note-common`)
