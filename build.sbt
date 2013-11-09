organization := "uk.co.deku"

name := "wiremock-scala-examples"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(  
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.specs2"    %% "specs2"    % "2.2.3",
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
  "com.github.tomakehurst" % "wiremock" % "1.33" % "test"
)

parallelExecution in Test := false