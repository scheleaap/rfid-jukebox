name := "rfid-jukebox"

scalaVersion := "2.13.1"

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies ++= {
  val sttpClient = "2.0.0-RC5"
  Seq(
    "com.diozero" % "diozero-core" % "0.11" exclude ("org.tinylog", "tinylog"),
    "com.softwaremill.sttp.client" %% "core" % sttpClient,
    "com.softwaremill.sttp.client" %% "play-json" % sttpClient,
    "com.softwaremill.sttp.client" %% "async-http-client-backend-monix" % sttpClient, // TODO Replace with httpclient-backend below once it contains Monix support
    //"com.softwaremill.sttp.client" %% "httpclient-backend" % sttpClient,
    "io.monix" %% "monix" % "3.1.0",

    // Logging
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

    // Testing
    "org.scalatest" %% "scalatest" % "3.1.0" % "test",
  )
}

scalacOptions ++= Seq(
  "-deprecation",
  "-language:higherKinds",
)

// Packaging
enablePlugins(
  JavaServerAppPackaging,
  SystemdPlugin,
  DebianPlugin
)

version := "1.0"

maintainer in Linux := "scheleaap"

packageSummary in Linux := "An RFID-based jukebox client for Mopidy with Spotify"

packageDescription := "An RFID-based jukebox client for Mopidy with Spotify"

debianPackageDependencies in Debian := Seq("openjdk-8-jre-headless")
