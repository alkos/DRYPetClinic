package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class UserService(val userDao: UserDao) extends Logging {

  def save(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(user: $user)")

    userDao.save(user)
  }

  def update(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(user: $user)")

    userDao.update(user)
  }

  def delete(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(user: $user)")

    userDao.deleteById(user.id)
  }

}
