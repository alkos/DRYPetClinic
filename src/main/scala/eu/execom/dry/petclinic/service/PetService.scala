package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class PetService(val petDao: PetDao) extends Logging {

  def save(pet: Pet)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(pet: $pet)")

    petDao.save(pet)
  }

  def update(pet: Pet)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(pet: $pet)")

    petDao.update(pet)
  }

  def delete(pet: Pet)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(pet: $pet)")

    petDao.deleteById(pet.id)
  }

}
