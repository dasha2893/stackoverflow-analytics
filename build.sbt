name := """stackoverflow-analytics"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.typesafe.play" % "anorm_2.11" % "2.4.0-M3",
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" % "requirejs" % "2.1.11-1"
)
