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
  lazy val hazelcastLoggingType: String = properties.getProperty("hazelcast.logging.type")
  lazy val smtpUrl: String = properties.getProperty("smtp.url")
  lazy val smtpPort: Int = properties.getProperty("smtp.port").toInt
  lazy val smtpUserName: String = properties.getProperty("smtp.username")
  lazy val smtpPassword: String = properties.getProperty("smtp.password")
  lazy val smtpSslOnConnect: Boolean = properties.getProperty("smtp.sslonconnect").toBoolean
  lazy val appEmail: String = properties.getProperty("app.email")
  lazy val appName: String = properties.getProperty("app.name")
  lazy val appUrl: String = properties.getProperty("app.url")
  lazy val googleWebClientId: String = properties.getProperty("google.webclient.id")
  lazy val googleWebClientSecret: String = properties.getProperty("google.clientweb.secret")

  def initialize():Unit = {
    //TODO do initialization
  }

  def cleanup():Unit = {
    //TODO do cleanup
  }

  lazy val httpAPI = new HttpApi(slickDb, eventBus, authenticationApi, userApi)

  override def init(context: ServletContext) {

    context.mount(httpAPI, "/api/*") // mount servlets
    initialize()
  }

  override def destroy(context: ServletContext): Unit = {
    cleanup()
  }

}
