package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class PetHistoryService(val petHistoryDao: PetHistoryDao) extends Logging {

  def save(petHistory: PetHistory)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(petHistory: $petHistory)")

    petHistoryDao.save(petHistory)
  }

  def update(petHistory: PetHistory)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(petHistory: $petHistory)")

    petHistoryDao.update(petHistory)
  }

  def delete(petHistory: PetHistory)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(petHistory: $petHistory)")

    petHistoryDao.deleteById(petHistory.id)
  }

}
