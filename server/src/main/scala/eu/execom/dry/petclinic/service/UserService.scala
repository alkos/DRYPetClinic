package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class UserService(val userDao: UserDao, val eventBus: EventBus) extends Logging {

  def save(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(user: $user)")

    userDao.save(user)

    eventBus.publish(UserCreateEvent(user.id, user.roleId, user.username, user.passwordHash))
    eventBus.publish(user.id ,UserCreateEvent(user.id, user.roleId, user.username, user.passwordHash))
  }

  def update(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(user: $user)")

    val roleIdChange = if (user.roleId.equals(user.roleIdPersisted)) None else Some(user.roleId, user.roleIdPersisted)
    val usernameChange = if (user.username.equals(user.usernamePersisted)) None else Some(user.username, user.usernamePersisted)
    val passwordHashChange = if (user.passwordHash.equals(user.passwordHashPersisted)) None else Some(user.passwordHash, user.passwordHashPersisted)

    userDao.update(user)

    if (usernameChange.isDefined) {
       eventBus.publish(UserUsernameUpdateEvent(user.id, usernameChange.get))
    }
    if (passwordHashChange.isDefined) {
       eventBus.publish(UserPasswordHashUpdateEvent(user.id, passwordHashChange.get))
       eventBus.publish(user.roleId ,UserPasswordHashUpdateEvent(user.id, passwordHashChange.get))
    }

    eventBus.publish(UserUpdateEvent(user.id, roleIdChange, usernameChange, passwordHashChange))
    eventBus.publish(user.username ,UserUpdateEvent(user.id, roleIdChange, usernameChange, passwordHashChange))
  }

  def delete(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(user: $user)")

    userDao.deleteById(user.id)

    eventBus.publish(UserDeleteEvent(user.id, user.roleId, user.username, user.passwordHash))
  }

}