name := "rfid-jukebox"

scalaVersion := "2.13.3"

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies ++= {
  val sttpClient = "2.0.0-RC5"
  Seq(
    "com.diozero" % "diozero-core" % "0.14",
    "com.github.pureconfig" %% "pureconfig" % "0.14.0",
    "com.softwaremill.sttp.client" %% "core" % sttpClient,
    "com.softwaremill.sttp.client" %% "play-json" % sttpClient,
    "com.softwaremill.sttp.client" %% "async-http-client-backend-monix" % sttpClient, // TODO Replace with httpclient-backend below once it contains Monix support
    //"com.softwaremill.sttp.client" %% "httpclient-backend" % sttpClient,
    "io.monix" %% "monix" % "3.3.0",
    "javazoom" % "jlayer" % "1.0.1",

    // Logging
    "org.tinylog" % "tinylog-api" % "2.2.0",
    "org.tinylog" % "tinylog-impl" % "2.2.0",
    "org.tinylog" % "slf4j-tinylog" % "2.2.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

    // Testing
    "org.scalatest" %% "scalatest" % "3.2.3" % "test",
  )
}

javacOptions ++= Seq(
  "-source", "1.8",
  "-target", "1.8",
  "-Xlint"
)

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

maintainer in Linux := "scheleaap"

packageSummary in Linux := "RFID Jukebox"

packageDescription := "An RFID-based jukebox client for Mopidy with Spotify"

debianPackageDependencies in Debian := Seq("openjdk-8-jre-headless")
