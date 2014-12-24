package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class VetService(val vetDao: VetDao) extends Logging {

  def save(vet: Vet)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(vet: $vet)")

    vetDao.save(vet)
  }

  def update(vet: Vet)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(vet: $vet)")

    vetDao.update(vet)
  }

  def delete(vet: Vet)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(vet: $vet)")

    vetDao.deleteById(vet.id)
  }

}
