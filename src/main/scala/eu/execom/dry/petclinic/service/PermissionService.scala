package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class PermissionService(val permissionDao: PermissionDao) extends Logging {

  def save(permission: Permission)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(permission: $permission)")

    permissionDao.save(permission)
  }

  def update(permission: Permission)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(permission: $permission)")

    permissionDao.update(permission)
  }

  def delete(permission: Permission)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(permission: $permission)")

    permissionDao.deleteById(permission.id)
  }

}
