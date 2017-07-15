val projectName = "anost"

val libVersion = "0.1.0"

val scala = "2.12.1"

val akka = "10.0.9"

val commonDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "com.typesafe.akka" %% "akka-http" % akka,
  "com.github.pureconfig" %% "pureconfig" % "0.7.2",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)


def commonSettings(name: String) = Seq(
  scalaVersion := scala,
  version := libVersion,
  libraryDependencies ++= commonDependencies
)


lazy val root = (project in file("."))
  .settings(commonSettings("anost"))
  .dependsOn(adapter, usecase, domain, support)

lazy val adapter = (project in file("modules/adapter"))
  .settings(commonSettings("adapter"))
  .dependsOn(support, usecase, domain)

lazy val usecase = (project in file("modules/usecase"))
  .settings(commonSettings("usecase"))
  .dependsOn(support, domain)

lazy val domain = (project in file("modules/domain"))
  .settings(commonSettings("domain"))
  .dependsOn(support)

lazy val support = (project in file("modules/support"))
  .settings(commonSettings("support"))
