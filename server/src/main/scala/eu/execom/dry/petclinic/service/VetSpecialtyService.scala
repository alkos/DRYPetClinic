package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class VetSpecialtyService(val vetSpecialtyDao: VetSpecialtyDao) extends Logging {

  def save(vetSpecialty: VetSpecialty)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(vetSpecialty: $vetSpecialty)")

    vetSpecialtyDao.save(vetSpecialty)
  }

  def update(vetSpecialty: VetSpecialty)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(vetSpecialty: $vetSpecialty)")

    vetSpecialtyDao.update(vetSpecialty)
  }

  def delete(vetSpecialty: VetSpecialty)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(vetSpecialty: $vetSpecialty)")

    vetSpecialtyDao.deleteById(vetSpecialty.id)
  }

}
