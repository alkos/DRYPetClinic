package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class VetSpecialtiesService(val vetSpecialtiesDao: VetSpecialtiesDao) extends Logging {

  def save(vetSpecialties: VetSpecialties)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(vetSpecialties: $vetSpecialties)")

    vetSpecialtiesDao.save(vetSpecialties)
  }

  def update(vetSpecialties: VetSpecialties)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(vetSpecialties: $vetSpecialties)")

    vetSpecialtiesDao.update(vetSpecialties)
  }

  def delete(vetSpecialties: VetSpecialties)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(vetSpecialties: $vetSpecialties)")

    vetSpecialtiesDao.deleteById(vetSpecialties.id)
  }

}
