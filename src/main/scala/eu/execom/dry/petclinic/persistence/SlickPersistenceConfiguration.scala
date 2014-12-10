package eu.execom.dry.petclinic.persistence

import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor

import scala.slick.jdbc.JdbcBackend._

trait SlickPersistenceConfiguration {

  def mysqlDriver: String
  def mysqlURL: String
  def mysqlUserName: String
  def mysqlPassword: String
  def mysqlCachePrepStmts: Boolean
  def mysqlPrepStmtCacheSize: Int
  def mysqlPrepStmtCacheSqlLimit: Int
  def mysqlUseServerPrepStmts: Boolean

  val hikariConfig = new HikariConfig()
  hikariConfig.setDriverClassName(mysqlDriver)
  hikariConfig.setJdbcUrl(mysqlURL)
  hikariConfig.setUsername(mysqlUserName)
  hikariConfig.setPassword(mysqlPassword)
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
