package com.note

import sbt.Keys._
import sbt._

object NoPublish extends AutoPlugin {
  override def requires: Plugins = plugins.JvmPlugin

  override def projectSettings = Seq(publishArtifact := false, skip in publish := true)
}
