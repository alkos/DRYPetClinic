package eu.execom.dry.petclinic

import java.util.Properties

import eu.execom.dry.petclinic.api._
import org.subethamail.wiser.Wiser

object AppTestConfiguration extends ApiConfiguration {

  lazy val testPropertyFileName = "/app_test.properties"

  lazy val properties: Properties = {
    val properties = new Properties()
    properties.load(getClass.getResourceAsStream(testPropertyFileName))
    properties
  }

  lazy val mysqlUsername: String = properties.getProperty("mysql.username")
  lazy val mysqlPassword: String = properties.getProperty("mysql.password")
  lazy val mysqlDatabaseName: String = properties.getProperty("mysql.database.name")
  lazy val mysqlServerName: String = properties.getProperty("mysql.server.name")
  lazy val mysqlCachePrepStmts: Boolean = properties.getProperty("mysql.cachePrepStmts").toBoolean
  lazy val mysqlPrepStmtCacheSize: Int = properties.getProperty("mysql.prepStmtCacheSize").toInt
  lazy val mysqlPrepStmtCacheSqlLimit: Int = properties.getProperty("mysql.prepStmtCacheSqlLimit").toInt
  lazy val mysqlUseServerPrepStmts: Boolean = properties.getProperty("mysql.useServerPrepStmts").toBoolean
  lazy val hazelcastGroupName: String = properties.getProperty("hazelcast.group.name")
  lazy val hazelcastGroupPassword: String = properties.getProperty("hazelcast.group.password")
  lazy val smtpUrl: String = properties.getProperty("smtp.url")
  lazy val smtpPort: Int = properties.getProperty("smtp.port").toInt
  lazy val smtpUserName: String = properties.getProperty("smtp.username")
  lazy val smtpPassword: String = properties.getProperty("smtp.password")
  lazy val appEmail: String = properties.getProperty("app.email")
  lazy val appName: String = properties.getProperty("app.name")
  lazy val appUrl: String = properties.getProperty("app.url")

  //start mock SMTP
  val wiser = new Wiser()
  wiser.setHostname(smtpUrl)
  wiser.setPort(smtpPort.toInt)
  wiser.start()
}
