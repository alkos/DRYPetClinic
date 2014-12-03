package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class PetTypeService(val petTypeDao: PetTypeDao) extends Logging {

  def save(petType: PetType)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(petType: $petType)")

    petTypeDao.save(petType)
  }

  def update(petType: PetType)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(petType: $petType)")

    petTypeDao.update(petType)
  }

  def delete(petType: PetType)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(petType: $petType)")

    petTypeDao.deleteById(petType.id)
  }

}
