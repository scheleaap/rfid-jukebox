name := "jukebox"

scalaVersion := "2.13.1"

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies ++= Seq(
  "com.diozero" % "diozero-core" % "0.11",

  // Logging
  //  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.tinylog" % "slf4j-binding" % "1.3.6",
)
