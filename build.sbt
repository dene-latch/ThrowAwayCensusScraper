import Dependencies._

enablePlugins(JavaAppPackaging)

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.1"
ThisBuild / organization     := "latch"
ThisBuild / organizationName := "latch"

val Versions = new {
  lazy val jsoupVersion = "1.12.1"
  lazy val snakeyamlVersion = "1.11"
  lazy val logbackVersion = "1.2.3"
  lazy val scalikeJDBCVersion = "3.3.5"
  lazy val pureCSVVersion = "0.3.3"
  lazy val postgresqlVersion = "42.2.8"
  lazy val scalaTest =  "3.0.8"
  lazy val scalaTestPlus = "3.1.0.0-RC2"
}


lazy val root = (project in file("."))
  .settings(
    name := "ThrowAwayCensusScraper",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.7.4",
      "au.com.bytecode" % "opencsv" % "2.4",
      "org.yaml" % "snakeyaml" % "1.11",
      "org.jsoup" % "jsoup" % "1.12.1",
      "io.spray" % "spray-json_2.12" % "1.3.5",
      "io.circe" %% "circe-yaml" % "0.11.0-M1",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9",
      "org.specs2" %% "specs2-core" % "4.6.0" % "test",
      "org.scalikejdbc" %% "scalikejdbc" % scalikeJDBCVersion,
      "io.kontainers" %% "purecsv" % pureCSVVersion,
      "ch.qos.logback"  %  "logback-classic" % logbackVersion,
      "org.postgresql" % "postgresql" % postgresqlVersion,
      "org.scalatest" %% "scalatest" % Versions.scalaTest % Test,
      "org.scalatestplus" %% "scalatestplus-scalacheck" % Versions.scalaTestPlus % Test
    )
  )

val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

ThisBuild / resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
ThisBuild / resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
ThisBuild / resolvers += "CDP S3 Respository" at "s3://cdp-repo"
ThisBuild / publishMavenStyle := true
ThisBuild / publishTo := Some("CDP Releases" at "s3://cdp-repo")
ThisBuild / javaOptions += "-Dscala.concurrent.context.maxThreads=20"

