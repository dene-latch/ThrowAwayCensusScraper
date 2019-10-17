import Dependencies._

enablePlugins(JavaAppPackaging)

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.1"
ThisBuild / organization     := "latch"
ThisBuild / organizationName := "latch"

lazy val root = (project in file("."))
  .settings(
    name := "ThrowAwayCensusScraper",
    libraryDependencies ++= Seq(
      "org.jsoup" % "jsoup" % "1.12.1",
      scalaTest % Test
    )
  )

ThisBuild / resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
ThisBuild / resolvers += "CDP S3 Respository" at "s3://cdp-repo"
ThisBuild / publishMavenStyle := true
ThisBuild / publishTo := Some("CDP Releases" at "s3://cdp-repo")
ThisBuild / javaOptions += "-Dscala.concurrent.context.maxThreads=20"

