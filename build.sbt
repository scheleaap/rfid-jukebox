Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.13.15"

ThisBuild / javacOptions ++= Seq(
  "-source",
  "11",
  "-target",
  "11",
  "-Xlint"
)

ThisBuild / scalacOptions ++= ScalacOptions.scalac213Options

ThisBuild / libraryDependencies ++= {
  val sttpClient = "3.10.2"
  val tinylog = "2.3.2"
  Seq(
    "com.diozero" % "diozero-core" % "0.14",
    "com.github.pureconfig" %% "pureconfig" % "0.17.8",
    "com.softwaremill.sttp.client3" %% "cats" % sttpClient,
    "com.softwaremill.sttp.client3" %% "core" % sttpClient,
    "com.softwaremill.sttp.client3" %% "play-json" % sttpClient,
    "org.typelevel" %% "cats-effect" % "3.5.7",
    // Logging
    "org.tinylog" % "tinylog-api" % tinylog,
    "org.tinylog" % "tinylog-impl" % tinylog,
    "org.tinylog" % "slf4j-tinylog" % tinylog,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    // Testing
    "org.scalatest" %% "scalatest" % "3.2.19" % "test"
  )
}

// Packaging
enablePlugins(
  JavaServerAppPackaging,
  SystemdPlugin,
  DebianPlugin
)

Linux / maintainer := "scheleaap"

Linux / packageSummary := "RFID Jukebox"

Linux / packageDescription := "An RFID-based jukebox client for Mopidy with Spotify"

Debian / debianPackageDependencies := Seq("openjdk-11-jre-headless")
