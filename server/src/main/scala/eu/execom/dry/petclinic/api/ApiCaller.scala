package eu.execom.dry.petclinic.api

import eu.execom.dry.petclinic.service.EventBus

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.slick.jdbc.JdbcBackend.Database
import scala.util.{Success, Failure, Try}

trait ApiCaller {
  def slickDb: Database
  def eventBus: EventBus

  def securityToken: String
  def securityToken_=(token: String)

  def session[T](f: SlickSession => T): T = slickDb.withSession(slickSession => {
    eventBus.startSession()
    f(slickSession)
  })

  def transaction[T](f: SlickSession => T): T = slickDb.withSession(slickSession => {
    eventBus.startTransaction()
    Try(f(slickSession)) match {
      case Failure(e) =>
      eventBus.rollback()
      throw e
    case Success(result) =>
      eventBus.publishPending()
      result
    }
  })
}
