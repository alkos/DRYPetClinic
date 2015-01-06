package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class UserSessionService(val userSessionDao: UserSessionDao) extends Logging {

  def save(userSession: UserSession)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(userSession: $userSession)")

    userSessionDao.save(userSession)
  }

  def update(userSession: UserSession)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(userSession: $userSession)")

    userSessionDao.update(userSession)
  }

  def delete(userSession: UserSession)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(userSession: $userSession)")

    userSessionDao.deleteById(userSession.id)
  }

}
