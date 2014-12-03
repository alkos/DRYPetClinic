package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class VisitService(val visitDao: VisitDao) extends Logging {

  def save(visit: Visit)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(visit: $visit)")

    visitDao.save(visit)
  }

  def update(visit: Visit)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(visit: $visit)")

    visitDao.update(visit)
  }

  def delete(visit: Visit)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(visit: $visit)")

    visitDao.deleteById(visit.id)
  }

}
