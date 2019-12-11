name := "jukebox"

scalaVersion := "2.13.1"

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies ++= Seq(
  "com.diozero" % "diozero-core" % "0.11" exclude("org.tinylog", "tinylog"),
  "io.monix" %% "monix" % "3.1.0",

  // Logging
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
)
