name := """paidy-admin"""

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.191",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "com.github.nscala-time" %% "nscala-time" % "2.12.0",
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

libraryDependencies += evolutions

coverageExcludedPackages := "<empty>;router.*;views.*;com.paidy.controllers.*"

coverageExcludedFiles := "target/.*"
