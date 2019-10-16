name := "ThrowAwayCensusScraper"

version := "0.1"

scalaVersion := "2.13.1"

val scalaJSVersion = sys.env.getOrElse("SCALAJS_VERSION", "0.6.20")

organization := "com.github.latch"

//sbtPlugin := true

// https://mvnrepository.com/artifact/org.jsoup/jsoup
libraryDependencies += "org.jsoup" % "jsoup" % "1.12.1"

