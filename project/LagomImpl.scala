package com.note

import com.note.Dependencies.{Compile, Runtime}
import sbt._
import sbt.Keys.{libraryDependencies, version}
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.dockerBaseImage
import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslApi, lagomScaladslKafkaBroker, _}

object LagomImpl extends AutoPlugin {
  override def requires: Plugins = plugins.JvmPlugin

  override def projectSettings = Seq(
    version := sys.env.getOrElse("VERSION", "development"),
    dockerBaseImage := "openjdk:11",
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      lagomScaladslAkkaDiscovery,
      lagomScaladslPersistenceJdbc,
      lagomScaladslCluster,
      Compile.lagompb,
      Runtime.lagompbRuntime,
      Runtime.scalapbCommonProtos
    )
  )
}
