package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class PetHistory(private var _id: Int, private var _history_change_time: Timestamp, private var _petId: Int, private var _name: String, private var _birthDate: Date, private var _ownerId: Int, private var _petTypeId: Int) {

  private var id_persisted: Int = id
  def idPersisted: Int = id_persisted

  def id: Int = _id
  def id_=(newId: Int)(implicit session: SlickSession): Any = if (newId != id) {

    _id = newId
  }

  private var history_change_time_persisted: DateTime = history_change_time
  def history_change_timePersisted: DateTime = history_change_time_persisted

  def history_change_time: DateTime = new org.joda.time.DateTime(_history_change_time)
  def history_change_time_=(newHistory_change_time: DateTime)(implicit session: SlickSession): Any = if (newHistory_change_time != history_change_time) {

    if (newHistory_change_time == null) throw PET_HISTORY_HISTORY__CHANGE__TIME_IS_REQUIRED

    _history_change_time = new java.sql.Timestamp(newHistory_change_time.getMillis)
  }

  private var petId_persisted: Int = petId
  def petIdPersisted: Int = petId_persisted

  def petId: Int = _petId
  def petId_=(newPetId: Int)(implicit session: SlickSession): Any = if (newPetId != petId) {

    _petId = newPetId
  }

  private var name_persisted: String = name
  def namePersisted: String = name_persisted

  def name: String = _name
  def name_=(newName: String)(implicit session: SlickSession): Any = if (newName != name) {

    if (newName == null) throw PET_HISTORY_NAME_IS_REQUIRED
    if (newName.size < 0) throw PET_HISTORY_NAME_MIN_SIZE
    if (newName.size > 1024) throw PET_HISTORY_NAME_MAX_SIZE

    _name = newName
  }

  private var birthDate_persisted: DateTime = birthDate
  def birthDatePersisted: DateTime = birthDate_persisted

  def birthDate: DateTime = new org.joda.time.DateTime(_birthDate)
  def birthDate_=(newBirthDate: DateTime)(implicit session: SlickSession): Any = if (newBirthDate != birthDate) {

    if (newBirthDate == null) throw PET_HISTORY_BIRTH_DATE_IS_REQUIRED

    _birthDate = new java.sql.Date(newBirthDate.getMillis)
  }

  private var ownerId_persisted: Int = ownerId
  def ownerIdPersisted: Int = ownerId_persisted

  def ownerId: Int = _ownerId
  def ownerId_=(newOwnerId: Int)(implicit session: SlickSession): Any = if (newOwnerId != ownerId) {

    _ownerId = newOwnerId
  }

  private var petTypeId_persisted: Int = petTypeId
  def petTypeIdPersisted: Int = petTypeId_persisted

  def petTypeId: Int = _petTypeId
  def petTypeId_=(newPetTypeId: Int)(implicit session: SlickSession): Any = if (newPetTypeId != petTypeId) {

    _petTypeId = newPetTypeId
  }
  def pet(implicit session: SlickSession): Pet = TableQuery[Pets].filter(_.id === petId).first
  def pet_=(pet: Pet)(implicit session: SlickSession) = petId = pet.id

  def owner(implicit session: SlickSession): owner = TableQuery[owners].filter(_.id === ownerId).first
  def owner_=(owner: owner)(implicit session: SlickSession) = ownerId = owner.id

  def petType(implicit session: SlickSession): PetType = TableQuery[PetTypes].filter(_.id === petTypeId).first
  def petType_=(petType: PetType)(implicit session: SlickSession) = petTypeId = petType.id

  def this(entity: PetHistory) = this(entity._id, entity._history_change_time, entity._petId, entity._name, entity._birthDate, entity._ownerId, entity._petTypeId)

  def this() = this(0, new java.sql.Timestamp(DateTime.now(DateTimeZone.UTC).getMillis), 0, "", new java.sql.Date(DateTime.now(DateTimeZone.UTC).getMillis), 0, 0)

  def this(history_change_time: DateTime, petId: Int, name: String, birthDate: DateTime, ownerId: Int, petTypeId: Int)(implicit session: SlickSession) = {
    this()
    this.history_change_time_=(history_change_time)(session)
    this.petId_=(petId)(session)
    this.name_=(name)(session)
    this.birthDate_=(birthDate)(session)
    this.ownerId_=(ownerId)(session)
    this.petTypeId_=(petTypeId)(session)
  }

  def this(history_change_time: DateTime, pet: Pet, name: String, birthDate: DateTime, owner: owner, petType: PetType)(implicit session: SlickSession) = {
    this()
    this.history_change_time_=(history_change_time)(session)
    this.pet_=(pet)(session)
    this.name_=(name)(session)
    this.birthDate_=(birthDate)(session)
    this.owner_=(owner)(session)
    this.petType_=(petType)(session)
  }

  def persisted() = {
    id_persisted = id
    history_change_time_persisted = history_change_time
    petId_persisted = petId
    name_persisted = name
    birthDate_persisted = birthDate
    ownerId_persisted = ownerId
    petTypeId_persisted = petTypeId
  }
}

object PetHistory {
  val ID: String = "id"
  val HISTORY_CHANGE_TIME: String = "history_change_time"
  val PETID: String = "petId"
  val NAME: String = "name"
  val BIRTHDATE: String = "birthDate"
  val OWNERID: String = "ownerId"
  val PETTYPEID: String = "petTypeId"
}

object PET_HISTORY_HISTORY__CHANGE__TIME_IS_REQUIRED extends DataConstraintException("PET_HISTORY_HISTORY__CHANGE__TIME_IS_REQUIRED")

object PET_HISTORY_NAME_IS_REQUIRED extends DataConstraintException("PET_HISTORY_NAME_IS_REQUIRED")

object PET_HISTORY_NAME_MIN_SIZE extends DataConstraintException("PET_HISTORY_NAME_MIN_SIZE")

object PET_HISTORY_NAME_MAX_SIZE extends DataConstraintException("PET_HISTORY_NAME_MAX_SIZE")

object PET_HISTORY_BIRTH_DATE_IS_REQUIRED extends DataConstraintException("PET_HISTORY_BIRTH_DATE_IS_REQUIRED")

object PETHISTORY_DOESNT_EXIST extends DataConstraintException("PETHISTORY_DOESNT_EXIST")

object PET_HISTORY_ID_IS_NOT_UNIQUE extends DataConstraintException("PET_HISTORY_ID_IS_NOT_UNIQUE")

class PetHistorys(tag: Tag) extends Table[PetHistory](tag, "PetHistory") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def history_change_time = column[Timestamp]("history_change_time")
  def petId = column[Int]("petId")
  def name = column[String]("name")
  def birthDate = column[Date]("birthDate")
  def ownerId = column[Int]("ownerId")
  def petTypeId = column[Int]("petTypeId")

  val create = PetHistory.apply _
  def * = (id, history_change_time, petId, name, birthDate, ownerId, petTypeId) <> (create.tupled, PetHistory.unapply)
  def ? = (id.?, history_change_time.?, petId.?, name.?, birthDate.?, ownerId.?, petTypeId.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  def pet= foreignKey("PETHISTORY_PET_FK", petId, TableQuery[Pets])(_.id)
  def owner= foreignKey("PETHISTORY_OWNER_FK", ownerId, TableQuery[owners])(_.id)
  def petType= foreignKey("PETHISTORY_PETTYPE_FK", petTypeId, TableQuery[PetTypes])(_.id)
}

class PetHistoryDao extends GenericSlickDao[PetHistory] {

  def save(entity: PetHistory)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[PetHistorys]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[PetHistory])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[PetHistorys]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: PetHistory)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[PetHistorys]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[PetHistory] = {
    logger.trace(s".findAll()")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): PetHistory = {
    logger.trace(s".getById(id: $id)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw PETHISTORY_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[PetHistory] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByHistory_change_time(history_change_time: DateTime)(implicit session: SlickSession): List[PetHistory] = {
    logger.trace(s".findByHistory_change_time(history_change_time: $history_change_time)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.history_change_time === new java.sql.Timestamp(history_change_time.getMillis))

    query.list
  }

  def findByPetId(petId: Int)(implicit session: SlickSession): List[PetHistory] = {
    logger.trace(s".findByPetId(petId: $petId)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.petId === petId)

    query.list
  }

  def findByName(name: String)(implicit session: SlickSession): List[PetHistory] = {
    logger.trace(s".findByName(name: $name)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.name === name)

    query.list
  }

  def findByBirthDate(birthDate: DateTime)(implicit session: SlickSession): List[PetHistory] = {
    logger.trace(s".findByBirthDate(birthDate: $birthDate)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.birthDate === new java.sql.Date(birthDate.getMillis))

    query.list
  }

  def findByOwnerId(ownerId: Int)(implicit session: SlickSession): List[PetHistory] = {
    logger.trace(s".findByOwnerId(ownerId: $ownerId)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.ownerId === ownerId)

    query.list
  }

  def findByPetTypeId(petTypeId: Int)(implicit session: SlickSession): List[PetHistory] = {
    logger.trace(s".findByPetTypeId(petTypeId: $petTypeId)")

    var query: Query[PetHistorys, PetHistorys#TableElementType, Seq] = TableQuery[PetHistorys]
    query = query.filter(_.petTypeId === petTypeId)

    query.list
  }

}
