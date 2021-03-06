package com.note

import sbt.librarymanagement.ModuleID
import sbt._

object Dependencies {

  object Versions {
    val scala213 = "2.13.1"
    val lagompbVersion = "0.5.0"
    val akkaVersion: String = "2.6.6"
    val scalapbCommonProtosVersion: String = "1.18.0-0"
    val silencerVersion = "1.6.0"
  }

  object Compile {
    val lagompb: ModuleID = "io.superflat" %% "lagompb-core" % Versions.lagompbVersion
    val lagompbReadSide = "io.superflat" %% "lagompb-readside" % Versions.lagompbVersion

    val scalapbCommon
    : ModuleID = "com.thesamet.scalapb.common-protos" %% "proto-google-common-protos-scalapb_0.10" % Versions.scalapbCommonProtosVersion
  }

  object Runtime {
    val lagompbRuntime: ModuleID = "io.superflat" %% "lagompb-core" % Versions.lagompbVersion % "protobuf"

    val scalapbCommonProtos
    : ModuleID = "com.thesamet.scalapb.common-protos" %% "proto-google-common-protos-scalapb_0.10" % Versions.scalapbCommonProtosVersion % "protobuf"
  }
}
