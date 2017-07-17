flywayUser := sys.env.getOrElse("ANOST_DB_USER", "root")
flywayPassword := sys.env.getOrElse("ANOST_DB_PASSWORD", "")
lazy val host = sys.env.getOrElse("ANOST_DB_HOST", "localhost")
lazy val port = sys.env.getOrElse("ANOST_DB_PORT", 3306)
flywayUrl := s"jdbc:mysql://$host:$port/anost"

flywayLocations := Seq("filesystem:./modules/adapter/src/main/resources/db/migration")