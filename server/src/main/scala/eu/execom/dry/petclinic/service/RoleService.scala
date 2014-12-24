package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class RoleService(val roleDao: RoleDao) extends Logging {

  def save(role: Role)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(role: $role)")

    roleDao.save(role)
  }

  def update(role: Role)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(role: $role)")

    roleDao.update(role)
  }

  def delete(role: Role)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(role: $role)")

    roleDao.deleteById(role.id)
  }

}
