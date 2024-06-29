name := """exam-manager"""
organization := "uonoko"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
  guice,
  "org.playframework.twirl" %% "twirl-api" % "2.0.5",
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "mysql" % "mysql-connector-java" % "8.0.27",
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
  "org.wvlet.airframe" %% "airframe-ulid" % "23.4.2"
)

dependencyOverrides ++= Seq(
  "org.playframework.twirl" %% "twirl-api" % "2.0.5",
  "org.scala-lang.modules" %% "scala-xml" % "2.2.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "uonoko.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "uonoko.binders._"
