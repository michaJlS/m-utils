name := "m-utils"

version := "0.2"

scalaVersion := "3.0.0"

val circeVersion = "0.14.1"
val scalaTestVersion = "3.2.9"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalatest" %% "scalatest-funspec" % scalaTestVersion % "test",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)
