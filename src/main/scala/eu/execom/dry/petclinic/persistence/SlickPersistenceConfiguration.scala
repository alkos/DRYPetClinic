package eu.execom.dry.petclinic.persistence

import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor

import scala.slick.jdbc.JdbcBackend._

trait SlickPersistenceConfiguration {

  def mysqlUsername: String
  def mysqlPassword: String
  def mysqlDatabaseName: String
  def mysqlServerName: String
  def mysqlCachePrepStmts: Boolean
  def mysqlPrepStmtCacheSize: Int
  def mysqlPrepStmtCacheSqlLimit: Int
  def mysqlUseServerPrepStmts: Boolean

  val hikariConfig = new HikariConfig()
  hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource")
  hikariConfig.addDataSourceProperty("databaseName", mysqlDatabaseName)
  hikariConfig.addDataSourceProperty("serverName", mysqlServerName)
  hikariConfig.addDataSourceProperty("user", mysqlUsername)
  hikariConfig.addDataSourceProperty("password", mysqlPassword)
  hikariConfig.addDataSourceProperty("cachePrepStmts", mysqlCachePrepStmts.toString)
  hikariConfig.addDataSourceProperty("prepStmtCacheSize", mysqlPrepStmtCacheSize.toString)
  hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", mysqlPrepStmtCacheSize.toString)
  hikariConfig.addDataSourceProperty("useServerPrepStmts", mysqlUseServerPrepStmts.toString)

  val dataSource = new HikariDataSource(hikariConfig)

  val liquibase = new Liquibase("META-INF/db/db-changelog.xml",
    new ClassLoaderResourceAccessor(), DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dataSource.getConnection)))
  liquibase.update("test, production")

  val slickDb = Database.forDataSource(dataSource)

  //DAO
  val userDao: UserDao = new UserDao
  val ownerDao: ownerDao = new ownerDao
  val petTypeDao: PetTypeDao = new PetTypeDao
  val petDao: PetDao = new PetDao
  val vetSpecialtyDao: VetSpecialtyDao = new VetSpecialtyDao
  val vetDao: VetDao = new VetDao
  val vetSpecialtiesDao: VetSpecialtiesDao = new VetSpecialtiesDao
  val visitDao: VisitDao = new VisitDao

}
