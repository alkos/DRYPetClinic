package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class UserService(val userDao: UserDao, val eventBus: EventBus) extends Logging {

  def save(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(user: $user)")

    userDao.save(user)

    eventBus.publish(UserCreateEvent(user.id, user.authenticationCode, user.role, user.username, user.passwordHash))
  }

  def update(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(user: $user)")

    val authenticationCodeChange = if (user.authenticationCode.equals(user.authenticationCode_persisted)) None else Some(user.authenticationCode, user.authenticationCode_persisted)
    val roleChange = if (user.role.equals(user.role_persisted)) None else Some(user.role, user.role_persisted)
    val usernameChange = if (user.username.equals(user.username_persisted)) None else Some(user.username, user.username_persisted)
    val passwordHashChange = if (user.passwordHash.equals(user.passwordHash_persisted)) None else Some(user.passwordHash, user.passwordHash_persisted)

    userDao.update(user)

    if (passwordHashChange.isDefined)  eventBus.publish(UserPasswordHashUpdateEvent(user.id, passwordHashChange.get))

    eventBus.publish(UserUpdateEvent(user.id, authenticationCodeChange, roleChange, usernameChange, passwordHashChange))
  }

  def delete(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(user: $user)")

    userDao.deleteById(user.id)

    eventBus.publish(UserDeleteEvent(user.id, user.authenticationCode, user.role, user.username, user.passwordHash))
  }

}
