val projectName = "anost"

val libVersion = "0.1.0"

val scala = "2.12.2"

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
val SKINNY_VERSION = "2.4.0"
val dbDependencies = Seq(
  "org.skinny-framework" %% "skinny-orm" % SKINNY_VERSION,
  "org.skinny-framework" %% "skinny-task" % SKINNY_VERSION,
  "mysql" % "mysql-connector-java" % "5.1.40", // 6.0.6 not working...
  "com.zaxxer" % "HikariCP" % "2.6.3"
)

val loggerDependencies = Seq(
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.qos.logback" % "logback-core" % "1.2.3"
)

val commonDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "com.github.pureconfig" %% "pureconfig" % "0.7.2",
  "com.lihaoyi" %% "sourcecode" % "0.1.4",
  "com.beachape" %% "enumeratum" % "1.5.12",
  "net.petitviolet" %% "operator" % "0.2.3",
  "net.petitviolet" %% "logging" % "0.2.0",
  "net.petitviolet" %% "acase" % "0.4.0",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
) ++ loggerDependencies ++ dbDependencies


val commonResolvers = Seq(
  "M2" at "http://download.java.net/maven/2",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.bintrayIvyRepo("scalameta", "maven"),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M9" cross CrossVersion.full),
  scalacOptions ++= Seq(
    "-Xplugin-require:macroparadise",
    "-Ymacro-debug-lite"
  )
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
) ++ metaMacroSettings

lazy val root = (project in file("."))
  .settings(commonSettings("anost"))
  .aggregate(conf, usecase, domain, support, adapter)

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

