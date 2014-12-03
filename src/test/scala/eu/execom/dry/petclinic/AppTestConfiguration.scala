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

  lazy val mysqlDriver: String = properties.getProperty("mysql.driver")
  lazy val mysqlURL: String = properties.getProperty("mysql.url")
  lazy val mysqlUserName: String = properties.getProperty("mysql.username")
  lazy val mysqlPassword: String = properties.getProperty("mysql.password")
  lazy val mysqlCachePrepStmts: String = properties.getProperty("mysql.cachePrepStmts")
  lazy val mysqlPrepStmtCacheSize: String = properties.getProperty("mysql.prepStmtCacheSize")
  lazy val mysqlPrepStmtCacheSqlLimit: String = properties.getProperty("mysql.prepStmtCacheSqlLimit")
  lazy val mysqlUseServerPrepStmts: String = properties.getProperty("mysql.useServerPrepStmts")
  lazy val smtpUrl: String = properties.getProperty("smtp.url")
  lazy val smtpPort: String = properties.getProperty("smtp.port")
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
