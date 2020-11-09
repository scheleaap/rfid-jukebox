name := "rfid-jukebox"

scalaVersion := "2.13.1"

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies ++= {
  Seq(
    "com.diozero" % "diozero-core" % "0.13",
    "com.github.pureconfig" %% "pureconfig" % "0.12.2",
    "io.monix" %% "monix" % "3.1.0",

    // Logging
    "org.tinylog" % "tinylog-api" % "2.2.0",
    "org.tinylog" % "tinylog-impl" % "2.2.0",
    "org.tinylog" % "slf4j-tinylog" % "2.2.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

    // Testing
    "org.scalatest" %% "scalatest" % "3.1.0" % "test",
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
