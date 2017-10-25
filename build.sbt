name := "m-utils"

version := "0.1"

scalaVersion := "2.12.4"

val circeVersion = "0.8.0"
val scalaTestVersion = "3.0.4"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)
