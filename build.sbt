name := """exam-manager"""
organization := "uonoko"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.4.1"

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
  "org.typelevel" %% "cats-core" % "2.6.1"
)
