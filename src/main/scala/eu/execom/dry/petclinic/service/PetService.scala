package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util.Logging
import org.joda.time.{DateTimeZone, DateTime}

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

class PetService(val petDao: PetDao, val petHistoryService: PetHistoryService) extends Logging {

  def save(pet: Pet)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(pet: $pet)")

    petDao.save(pet)

    petHistoryService.save(new PetHistory(DateTime.now(DateTimeZone.UTC), pet.id, pet.name, pet.birthDate, pet.ownerId, pet.petTypeId))
  }

  def update(pet: Pet)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(pet: $pet)")

    val nameChange = if (pet.name.equals(pet.namePersisted)) None else Some(pet.name, pet.namePersisted)
    val birthDateChange = if (pet.birthDate.equals(pet.birthDatePersisted)) None else Some(pet.birthDate, pet.birthDatePersisted)
    val ownerIdChange = if (pet.ownerId.equals(pet.ownerIdPersisted)) None else Some(pet.ownerId, pet.ownerIdPersisted)
    val petTypeIdChange = if (pet.petTypeId.equals(pet.petTypeIdPersisted)) None else Some(pet.petTypeId, pet.petTypeIdPersisted)

    petDao.update(pet)

    if (nameChange.isDefined || birthDateChange.isDefined || ownerIdChange.isDefined || petTypeIdChange.isDefined)
      petHistoryService.save(new PetHistory(DateTime.now(DateTimeZone.UTC), pet.id, pet.name, pet.birthDate, pet.ownerId, pet.petTypeId))
  }

  def delete(pet: Pet)(implicit session: SlickSession): Unit = {
    logger.trace(s".delete(pet: $pet)")

    petDao.deleteById(pet.id)

    petHistoryService.save(new PetHistory(DateTime.now(DateTimeZone.UTC), pet.id, pet.name, pet.birthDate, pet.ownerId, pet.petTypeId))
  }

}
