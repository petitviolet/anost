val projectName = "anost"

val libVersion = "0.1.0"

val scala = "2.12.1"

val organizationId = "net.petitviolet"

val outputDir = "bin"

val commonScalacOptions = Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard"
)

val akka = "10.0.9"

val akkaHttpDependencies = Seq(
  "com.typesafe.akka" %% "akka-http" % akka,
  "com.typesafe.akka" %% "akka-http-core" % akka,
  "com.typesafe.akka" %% "akka-http-spray-json" % akka,
  "com.typesafe.akka" %% "akka-http-testkit" % akka % "test"
)
val SKINNY_VERSION = "2.3.7"
val dbDependencies = Seq(
  "org.skinny-framework" %% "skinny-orm" % SKINNY_VERSION,
  "org.skinny-framework" %% "skinny-task" % SKINNY_VERSION,
  "org.scalikejdbc" %% "scalikejdbc-jsr310" % "2.5.0",
  "mysql" % "mysql-connector-java" % "5.1.41",
  "com.zaxxer" % "HikariCP" % "2.6.1"
)

val loggerDependencies = Seq(
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "ch.qos.logback" % "logback-core" % "1.1.7"
)

val commonDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "com.github.pureconfig" %% "pureconfig" % "0.7.2",
  "com.lihaoyi" %% "sourcecode" % "0.1.3",
  "com.beachape" %% "enumeratum" % "1.5.12",
  "net.petitviolet" %% "operator" % "0.2.2",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
) ++ loggerDependencies ++ dbDependencies


val commonResolvers = Seq(
  "M2" at "http://download.java.net/maven/2",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

def commonSettings(projectName: String) = Seq(
  name := projectName,
  organization := organizationId,
  version := libVersion,
  scalaVersion := scala,
  scalacOptions ++= Seq("-Xfatal-warnings", "-deprecation"),
  javacOptions ++= Seq("-encoding", "UTF-8"),
  resolvers ++= commonResolvers,
  libraryDependencies ++= commonDependencies,
  parallelExecution in Test := false
)

lazy val root = (project in file("."))
  .settings(commonSettings("anost"))
  .dependsOn(adapter, usecase, domain, support)

lazy val adapter = (project in file("modules/adapter"))
  .settings(commonSettings("adapter"))
  .settings(
    libraryDependencies ++= akkaHttpDependencies ++
      Seq(
        "commons-daemon" % "commons-daemon" % "1.0.15"
      )
  )
  .dependsOn(support, usecase, domain, conf)

lazy val usecase = (project in file("modules/usecase"))
  .settings(commonSettings("usecase"))
  .dependsOn(support, domain)

lazy val domain = (project in file("modules/domain"))
  .settings(commonSettings("domain"))
  .dependsOn(support)

lazy val support = (project in file("modules/support"))
  .settings(commonSettings("support"))

lazy val conf = (project in file("modules/conf"))
  .settings(commonSettings("conf"))

