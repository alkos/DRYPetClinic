package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class Visit(private var _id: Int, private var _date: Date, private var _description: String, private var _petId: Int) {

  private var id_persisted: Int = id
  def idPersisted: Int = id_persisted

  def id: Int = _id
  def id_=(newId: Int)(implicit session: SlickSession): Any = if (newId != id) {

    _id = newId
  }

  private var date_persisted: DateTime = date
  def datePersisted: DateTime = date_persisted

  def date: DateTime = new org.joda.time.DateTime(_date)
  def date_=(newDate: DateTime)(implicit session: SlickSession): Any = if (newDate != date) {

    if (newDate == null) throw VISIT_DATE_IS_REQUIRED

    _date = new java.sql.Date(newDate.getMillis)
  }

  private var description_persisted: String = description
  def descriptionPersisted: String = description_persisted

  def description: String = _description
  def description_=(newDescription: String)(implicit session: SlickSession): Any = if (newDescription != description) {

    if (newDescription == null) throw VISIT_DESCRIPTION_IS_REQUIRED
    if (newDescription.size < 0) throw VISIT_DESCRIPTION_MIN_SIZE
    if (newDescription.size > 1024) throw VISIT_DESCRIPTION_MAX_SIZE

    _description = newDescription
  }

  private var petId_persisted: Int = petId
  def petIdPersisted: Int = petId_persisted

  def petId: Int = _petId
  def petId_=(newPetId: Int)(implicit session: SlickSession): Any = if (newPetId != petId) {

    _petId = newPetId
  }
  def pet(implicit session: SlickSession): Pet = TableQuery[Pets].filter(_.id === petId).first
  def pet_=(pet: Pet)(implicit session: SlickSession) = petId = pet.id

  def this(entity: Visit) = this(entity._id, entity._date, entity._description, entity._petId)

  def this() = this(0, new java.sql.Date(DateTime.now(DateTimeZone.UTC).getMillis), "", 0)

  def this(date: DateTime, description: String, petId: Int)(implicit session: SlickSession) = {
    this()
    this.date_=(date)(session)
    this.description_=(description)(session)
    this.petId_=(petId)(session)
  }

  def this(date: DateTime, description: String, pet: Pet)(implicit session: SlickSession) = {
    this()
    this.date_=(date)(session)
    this.description_=(description)(session)
    this.pet_=(pet)(session)
  }

  def persisted() = {
    id_persisted = id
    date_persisted = date
    description_persisted = description
    petId_persisted = petId
  }
}

object Visit {
  val ID: String = "_id"
  val DATE: String = "_date"
  val DESCRIPTION: String = "_description"
  val PETID: String = "_petId"
}

object VISIT_DATE_IS_REQUIRED extends DataConstraintException("VISIT_DATE_IS_REQUIRED")

object VISIT_DESCRIPTION_MIN_SIZE extends DataConstraintException("VISIT_DESCRIPTION_MIN_SIZE")

object VISIT_DESCRIPTION_MAX_SIZE extends DataConstraintException("VISIT_DESCRIPTION_MAX_SIZE")

object VISIT_DESCRIPTION_IS_REQUIRED extends DataConstraintException("VISIT_DESCRIPTION_IS_REQUIRED")

object VISIT_DOESNT_EXIST extends DataConstraintException("VISIT_DOESNT_EXIST")

object VISIT_ID_IS_NOT_UNIQUE extends DataConstraintException("VISIT_ID_IS_NOT_UNIQUE")

class Visits(tag: Tag) extends Table[Visit](tag, "Visit") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def date = column[Date]("date")
  def description = column[String]("description")
  def petId = column[Int]("petId")

  val create = Visit.apply _
  def * = (id, date, description, petId) <> (create.tupled, Visit.unapply)
  def ? = (id.?, date.?, description.?, petId.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  def pet= foreignKey("VISIT_PET_FK", petId, TableQuery[Pets])(_.id)
}

class VisitDao extends GenericSlickDao[Visit] {

  def save(entity: Visit)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[Visits]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[Visit])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[Visits]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: Visit)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[Visits]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[Visit] = {
    logger.trace(s".findAll()")

    var query: Query[Visits, Visits#TableElementType, Seq] = TableQuery[Visits]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[Visits, Visits#TableElementType, Seq] = TableQuery[Visits]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): Visit = {
    logger.trace(s".getById(id: $id)")

    var query: Query[Visits, Visits#TableElementType, Seq] = TableQuery[Visits]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw VISIT_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[Visits, Visits#TableElementType, Seq] = TableQuery[Visits]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[Visit] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[Visits, Visits#TableElementType, Seq] = TableQuery[Visits]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByDate(date: DateTime)(implicit session: SlickSession): List[Visit] = {
    logger.trace(s".findByDate(date: $date)")

    var query: Query[Visits, Visits#TableElementType, Seq] = TableQuery[Visits]
    query = query.filter(_.date === new java.sql.Date(date.getMillis))

    query.list
  }

  def findByDescription(description: String)(implicit session: SlickSession): List[Visit] = {
    logger.trace(s".findByDescription(description: $description)")

    var query: Query[Visits, Visits#TableElementType, Seq] = TableQuery[Visits]
    query = query.filter(_.description === description)

    query.list
  }

  def findByPetId(petId: Int)(implicit session: SlickSession): List[Visit] = {
    logger.trace(s".findByPetId(petId: $petId)")

    var query: Query[Visits, Visits#TableElementType, Seq] = TableQuery[Visits]
    query = query.filter(_.petId === petId)

    query.list
  }

}
