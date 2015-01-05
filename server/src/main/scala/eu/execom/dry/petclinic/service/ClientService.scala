package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class ClientService(val clientDao: ClientDao) extends Logging {

  def save(client: Client)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(client: $client)")

    clientDao.save(client)
  }

  def update(client: Client)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(client: $client)")

    clientDao.update(client)
  }

  def delete(client: Client)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(client: $client)")

    clientDao.deleteById(client.id)
  }

}
