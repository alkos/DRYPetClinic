package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class Pet(private var _id: Int, private var _name: String, private var _birthDate: Date, private var _ownerId: Int, private var _petTypeId: Int) {

  private var id_persisted: Int = id
  def idPersisted: Int = id_persisted

  def id: Int = _id
  def id_=(newId: Int)(implicit session: SlickSession): Any = if (newId != id) {

    _id = newId
  }

  private var name_persisted: String = name
  def namePersisted: String = name_persisted

  def name: String = _name
  def name_=(newName: String)(implicit session: SlickSession): Any = if (newName != name) {

    if (newName == null) throw PET_NAME_IS_REQUIRED
    if (newName.size < 2) throw PET_NAME_MIN_SIZE
    if (newName.size > 40) throw PET_NAME_MAX_SIZE

    _name = newName
  }

  private var birthDate_persisted: DateTime = birthDate
  def birthDatePersisted: DateTime = birthDate_persisted

  def birthDate: DateTime = new org.joda.time.DateTime(_birthDate)
  def birthDate_=(newBirthDate: DateTime)(implicit session: SlickSession): Any = if (newBirthDate != birthDate) {

    if (newBirthDate == null) throw PET_BIRTHDATE_IS_REQUIRED

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
  def owner(implicit session: SlickSession): owner = TableQuery[owners].filter(_.id === ownerId).first
  def owner_=(owner: owner)(implicit session: SlickSession) = ownerId = owner.id

  def petType(implicit session: SlickSession): PetType = TableQuery[PetTypes].filter(_.id === petTypeId).first
  def petType_=(petType: PetType)(implicit session: SlickSession) = petTypeId = petType.id

  def this(entity: Pet) = this(entity._id, entity._name, entity._birthDate, entity._ownerId, entity._petTypeId)

  def this() = this(0, "", new java.sql.Date(DateTime.now(DateTimeZone.UTC).getMillis), 0, 0)

  def this(name: String, birthDate: DateTime, ownerId: Int, petTypeId: Int)(implicit session: SlickSession) = {
    this()
    this.name_=(name)(session)
    this.birthDate_=(birthDate)(session)
    this.ownerId_=(ownerId)(session)
    this.petTypeId_=(petTypeId)(session)
  }

  def this(name: String, birthDate: DateTime, owner: owner, petType: PetType)(implicit session: SlickSession) = {
    this()
    this.name_=(name)(session)
    this.birthDate_=(birthDate)(session)
    this.owner_=(owner)(session)
    this.petType_=(petType)(session)
  }

  def persisted() = {
    id_persisted = id
    name_persisted = name
    birthDate_persisted = birthDate
    ownerId_persisted = ownerId
    petTypeId_persisted = petTypeId
  }
}

object Pet {
  val ID: String = "_id"
  val NAME: String = "_name"
  val BIRTHDATE: String = "_birthDate"
  val OWNERID: String = "_ownerId"
  val PETTYPEID: String = "_petTypeId"
}

object PET_NAME_MIN_SIZE extends DataConstraintException("PET_NAME_MIN_SIZE")

object PET_NAME_MAX_SIZE extends DataConstraintException("PET_NAME_MAX_SIZE")

object PET_NAME_IS_REQUIRED extends DataConstraintException("PET_NAME_IS_REQUIRED")

object PET_BIRTHDATE_IS_REQUIRED extends DataConstraintException("PET_BIRTHDATE_IS_REQUIRED")

object PET_DOESNT_EXIST extends DataConstraintException("PET_DOESNT_EXIST")

object PET_ID_IS_NOT_UNIQUE extends DataConstraintException("PET_ID_IS_NOT_UNIQUE")

class Pets(tag: Tag) extends Table[Pet](tag, "Pet") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def birthDate = column[Date]("birthDate")
  def ownerId = column[Int]("ownerId")
  def petTypeId = column[Int]("petTypeId")

  val create = Pet.apply _
  def * = (id, name, birthDate, ownerId, petTypeId) <> (create.tupled, Pet.unapply)
  def ? = (id.?, name.?, birthDate.?, ownerId.?, petTypeId.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  def owner= foreignKey("PET_OWNER_FK", ownerId, TableQuery[owners])(_.id)
  def petType= foreignKey("PET_PETTYPE_FK", petTypeId, TableQuery[PetTypes])(_.id)
}

class PetDao extends GenericSlickDao[Pet] {

  def save(entity: Pet)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[Pets]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[Pet])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[Pets]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: Pet)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[Pets]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[Pet] = {
    logger.trace(s".findAll()")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): Pet = {
    logger.trace(s".getById(id: $id)")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw PET_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[Pet] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByName(name: String)(implicit session: SlickSession): List[Pet] = {
    logger.trace(s".findByName(name: $name)")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]
    query = query.filter(_.name === name)

    query.list
  }

  def findByBirthDate(birthDate: DateTime)(implicit session: SlickSession): List[Pet] = {
    logger.trace(s".findByBirthDate(birthDate: $birthDate)")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]
    query = query.filter(_.birthDate === new java.sql.Date(birthDate.getMillis))

    query.list
  }

  def findByOwnerId(ownerId: Int)(implicit session: SlickSession): List[Pet] = {
    logger.trace(s".findByOwnerId(ownerId: $ownerId)")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]
    query = query.filter(_.ownerId === ownerId)

    query.list
  }

  def findByPetTypeId(petTypeId: Int)(implicit session: SlickSession): List[Pet] = {
    logger.trace(s".findByPetTypeId(petTypeId: $petTypeId)")

    var query: Query[Pets, Pets#TableElementType, Seq] = TableQuery[Pets]
    query = query.filter(_.petTypeId === petTypeId)

    query.list
  }

}
