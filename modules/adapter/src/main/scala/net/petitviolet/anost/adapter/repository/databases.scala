package net.petitviolet.anost.adapter.repository

import com.typesafe.config.Config
import com.zaxxer.hikari.HikariDataSource
import net.petitviolet.anost.adapter.support.ConfigContainer
import net.petitviolet.anost.support.Id
import scalikejdbc._
import scalikejdbc.config._
import skinny.orm.SkinnyCRUDMapperWithId

/**
 * DBの共通部分
 */
sealed abstract class Database(val dbName: Symbol) extends DBs with TypesafeConfigReader with TypesafeConfig {
  import Database.OptConfig
  lazy val config: Config = ConfigContainer.config

  def readSession: DBSession = ReadOnlyNamedAutoSession(dbName)
  def writeSession: DBSession = NamedAutoSession(dbName)

  private[repository] def close(): Unit = source.close()

  // HikariCPの設定を反映する
  // closeする際にDataSourceだとcloseが呼べないのでHikariDataSourceのままにしておく
  private[repository] lazy val source: HikariDataSource = {
    val _conf = config.getConfig(s"db.${dbName.name}")

    val ds = new HikariDataSource()

    // must
    ds.setUsername(_conf.getString("user"))
    ds.setJdbcUrl(_conf.getString("url"))
    ds.setPassword(_conf.getString("password"))
    ds.setPoolName(_conf.getString("poolName"))

    val driver = _conf.getString("driver")
    Class.forName(driver)
    ds.setDriverClassName(driver)

    // optional(not implemented all yet...)
    _conf.optionalLong("connectionTimeout") foreach ds.setConnectionTimeout
    _conf.optionalLong("idleTimeout") foreach ds.setIdleTimeout
    _conf.optionalLong("maxLifetime") foreach ds.setMaxLifetime

    _conf.optionalInt("maximumPoolSize") foreach ds.setMaximumPoolSize
    _conf.optionalBool("autoCommit") foreach ds.setAutoCommit
    _conf.optionalBool("registerMbeans") foreach ds.setRegisterMbeans

    ds
  }

  final def setup(): Unit = {
    //    super.setup(dbName)
    ConnectionPool.add(dbName, new DataSourceConnectionPool(source))
  }
}

object Database {
  private implicit class OptConfig(val config: Config) extends AnyVal {
    def optional(key: String): Option[AnyRef] = if (config.hasPath(key)) Some(config.getAnyRef(key)) else None
    def optionalLong(key: String): Option[Long] = if (config.hasPath(key)) Some(config.getLong(key)) else None
    def optionalInt(key: String): Option[Int] = if (config.hasPath(key)) Some(config.getInt(key)) else None
    def optionalString(key: String): Option[String] = if (config.hasPath(key)) Some(config.getString(key)) else None
    def optionalBool(key: String): Option[Boolean] = if (config.hasPath(key)) Some(config.getBoolean(key)) else None
  }

  def setup() = {
    scalikejdbc.config.DBs.loadGlobalSettings()

    dbs foreach (_.setup())
  }

  def shutDown() = {
    dbs foreach { _.close() }
  }

  private val dbs: Seq[Database] = Anost :: Nil

  case object Anost extends Database('anost)
}

/**
 * DBに対するORM
 * @tparam T
 */
sealed trait DatabaseMapper[T] extends SkinnyCRUDMapperWithId[Id[T], T] {
  val db: Database

  final override def connectionPoolName: Any = db.dbName

  override def autoSession: DBSession = db.readSession

  override def idToRawValue(id: Id[T]): Any = id.value

  override def rawValueToId(value: Any): Id[T] = value match {
    case s: String => Id(s)
    case _ => sys.error("unknown column")
  }

  override def useExternalIdGenerator: Boolean = true
}

/**
 * DB:anostを使用する場合はこちらをextendsする
 * @tparam T
 */
trait AnostMapper[T] extends DatabaseMapper[T] {
  lazy val db = Database.Anost

}

