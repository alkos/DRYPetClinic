package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class UserService(val userDao: UserDao, val eventBus: EventBus) extends Logging {

  def save(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(user: $user)")

    userDao.save(user)

    eventBus.publish(UserCreateEvent(user.id, user.roleId, user.email, user.passwordHash, user.facebookId, user.googleId))
    eventBus.publish(user.id ,UserCreateEvent(user.id, user.roleId, user.email, user.passwordHash, user.facebookId, user.googleId))
  }

  def update(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(user: $user)")

    val roleIdChange = if (user.roleId.equals(user.roleIdPersisted)) None else Some(user.roleId, user.roleIdPersisted)
    val emailChange = if (user.email.equals(user.emailPersisted)) None else Some(user.email, user.emailPersisted)
    val passwordHashChange = if (user.passwordHash.equals(user.passwordHashPersisted)) None else Some(user.passwordHash, user.passwordHashPersisted)
    val facebookIdChange = if (user.facebookId.equals(user.facebookIdPersisted)) None else Some(user.facebookId, user.facebookIdPersisted)
    val googleIdChange = if (user.googleId.equals(user.googleIdPersisted)) None else Some(user.googleId, user.googleIdPersisted)

    userDao.update(user)

    if (emailChange.isDefined) {
       eventBus.publish(UserEmailUpdateEvent(user.id, emailChange.get))
    }
    if (passwordHashChange.isDefined) {
       eventBus.publish(UserPasswordHashUpdateEvent(user.id, passwordHashChange.get))
       eventBus.publish(user.roleId ,UserPasswordHashUpdateEvent(user.id, passwordHashChange.get))
    }

    eventBus.publish(UserUpdateEvent(user.id, roleIdChange, emailChange, passwordHashChange, facebookIdChange, googleIdChange))
    eventBus.publish(user.email ,UserUpdateEvent(user.id, roleIdChange, emailChange, passwordHashChange, facebookIdChange, googleIdChange))
  }

  def delete(user: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(user: $user)")

    userDao.deleteById(user.id)

    eventBus.publish(UserDeleteEvent(user.id, user.roleId, user.email, user.passwordHash, user.facebookId, user.googleId))
  }

}
