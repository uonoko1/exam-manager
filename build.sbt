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
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
  "org.wvlet.airframe" %% "airframe-ulid" % "24.3.0",
  "org.typelevel" %% "cats-core" % "2.6.1"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "uonoko.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "uonoko.binders._"
