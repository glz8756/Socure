name := "socure"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-fs2",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic-extras",
  "io.circe" %% "circe-refined"
).map(_ % "0.13.0")
