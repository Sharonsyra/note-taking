// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.6.2")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.34")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.10.7"

addSbtPlugin("com.tapad" % "sbt-docker-compose" % "1.0.35")
