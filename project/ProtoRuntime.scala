package com.note

import sbt._
import sbt.Keys.libraryDependencies
import com.note.Dependencies.{Compile, Runtime}

object ProtoRuntime extends AutoPlugin {
  override def requires: Plugins = plugins.JvmPlugin

  override def projectSettings =
    Seq(
      libraryDependencies ++= Seq(
        Compile.lagompb,
        Compile.scalapbCommon,
        Runtime.lagompbRuntime,
        Runtime.scalapbCommonProtos
      )
    )
}