
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC6")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

resolvers += "Flyway" at "https://flywaydb.org/repo"
addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.2.0")
