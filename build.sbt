name := """exam-manager"""
organization := "uonoko"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.4.1"

ThisBuild / scalafmtOnCompile := true

libraryDependencies ++= Seq(
  guice,
  "org.playframework" %% "play-slick" % "6.1.0",
  "org.playframework" %% "play-slick-evolutions" % "6.1.0",
  "mysql" % "mysql-connector-java" % "8.0.33",
  "org.playframework" %% "play" % "3.0.4",
  "org.playframework" %% "play-test" % "3.0.4" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
  "org.mockito" % "mockito-core" % "5.3.1" % Test,
  "org.wvlet.airframe" %% "airframe-ulid" % "23.4.1",
  "org.typelevel" %% "cats-core" % "2.6.1",
  "com.h2database" % "h2" % "1.3.148" % Test,
  "org.apache.pekko" %% "pekko-actor" % "1.1.0-M1",
  "org.apache.pekko" %% "pekko-testkit" % "1.1.0-M1",
  "org.apache.pekko" %% "pekko-actor-typed" % "1.1.0-M1",
  "org.apache.pekko" %% "pekko-protobuf-v3" % "1.1.0-M1",
  "org.apache.pekko" %% "pekko-slf4j" % "1.1.0-M1",
  "org.apache.pekko" %% "pekko-serialization-jackson" % "1.1.0-M1",
  "org.apache.pekko" %% "pekko-stream" % "1.1.0-M1"
)

Test / javaOptions += "-Dconfig.file=conf/test.conf"
