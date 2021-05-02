Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.13.5"

ThisBuild / javacOptions ++= Seq(
  "-source",
  "1.8",
  "-target",
  "1.8",
  "-Xlint"
)

ThisBuild / scalacOptions ++= ScalacOptions.scalac213Options

ThisBuild / libraryDependencies ++= {
  val sttpClient = "2.2.9"
  val tinylog = "2.3.1"
  Seq(
    "com.diozero" % "diozero-core" % "0.14",
    "com.github.pureconfig" %% "pureconfig" % "0.15.0",
    "com.softwaremill.sttp.client" %% "core" % sttpClient,
    "com.softwaremill.sttp.client" %% "play-json" % sttpClient,
    "com.softwaremill.sttp.client" %% "async-http-client-backend-monix" % sttpClient, // TODO Replace with httpclient-backend below once it contains Monix support
    //"com.softwaremill.sttp.client" %% "httpclient-backend" % sttpClient,
    "io.monix" %% "monix" % "3.3.0",
    // Logging
    "org.tinylog" % "tinylog-api" % tinylog,
    "org.tinylog" % "tinylog-impl" % tinylog,
    "org.tinylog" % "slf4j-tinylog" % tinylog,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
    // Testing
    "org.scalatest" %% "scalatest" % "3.2.8" % "test"
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

Debian / debianPackageDependencies := Seq("openjdk-8-jre-headless")
