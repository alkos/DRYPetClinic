package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class ownerService(val ownerDao: ownerDao) extends Logging {

  def save(owner: owner)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(owner: $owner)")

    ownerDao.save(owner)
  }

  def update(owner: owner)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(owner: $owner)")

    ownerDao.update(owner)
  }

  def delete(owner: owner)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(owner: $owner)")

    ownerDao.deleteById(owner.id)
  }

}
