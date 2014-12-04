package eu.execom.dry.petclinic

import java.io.FileInputStream
import java.util.Properties
import javax.servlet.ServletContext

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.rest.HttpApi
import org.scalatra.LifeCycle
import org.slf4j.LoggerFactory

class AppConfiguration extends LifeCycle with ApiConfiguration {

  val logConfigurationFile = System.getenv("PETCLINIC_HOME") + "/logback.xml"
  val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
  val jc: JoranConfigurator = new JoranConfigurator()
  context.reset()
  context.putProperty("application-name", "Petclinic")
  jc.setContext(context)
  jc.doConfigure(logConfigurationFile)

  lazy val appPropertyFilePath = System.getenv("PETCLINIC_HOME") + "/app.properties"
  lazy val properties: Properties = {
    val properties = new Properties()
    properties.load(new FileInputStream(appPropertyFilePath))
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
  lazy val hazelcastGroupName: String = properties.getProperty("hazelcast.group.name")
  lazy val hazelcastGroupPassword: String = properties.getProperty("hazelcast.group.password")
  lazy val smtpUrl: String = properties.getProperty("smtp.url")
  lazy val smtpPort: String = properties.getProperty("smtp.port")
  lazy val smtpUserName: String = properties.getProperty("smtp.username")
  lazy val smtpPassword: String = properties.getProperty("smtp.password")
  lazy val appEmail: String = properties.getProperty("app.email")
  lazy val appName: String = properties.getProperty("app.name")
  lazy val appUrl: String = properties.getProperty("app.url")

  def initialize():Unit = {
    //TODO do initialization
  }

  def cleanup():Unit = {
    //TODO do cleanup
  }

  lazy val httpAPI = new HttpApi(slickDb, authenticationApi, userApi)

  override def init(context: ServletContext) {

    context.mount(httpAPI, "/api/*") // mount servlets
    initialize()
  }

  override def destroy(context: ServletContext): Unit = {
    cleanup()
  }

}
