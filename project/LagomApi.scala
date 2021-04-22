package com.note

import com.note.Dependencies.{Compile, Runtime}
import sbt._
import sbt.Keys.libraryDependencies
import com.lightbend.lagom.sbt.LagomImport.{lagomScaladslApi, lagomScaladslKafkaBroker, lagomScaladslServer}

object LagomApi extends AutoPlugin {
  override def requires: Plugins = plugins.JvmPlugin

  override def projectSettings = Seq(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer % Optional,
      lagomScaladslKafkaBroker,
      Compile.lagompb,
      Runtime.lagompbRuntime
    )
  )
}
