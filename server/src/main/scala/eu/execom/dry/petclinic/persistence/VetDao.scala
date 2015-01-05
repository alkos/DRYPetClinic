package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class Vet(private var _id: Int, private var _firstName: String, private var _lastName: String) {

  private var id_persisted: Int = id
  def idPersisted: Int = id_persisted

  def id: Int = _id
  def id_=(newId: Int)(implicit session: SlickSession): Any = if (newId != id) {

    _id = newId
  }

  private var firstName_persisted: String = firstName
  def firstNamePersisted: String = firstName_persisted

  def firstName: String = _firstName
  def firstName_=(newFirstName: String)(implicit session: SlickSession): Any = if (newFirstName != firstName) {

    if (newFirstName == null) throw VET_FIRST_NAME_IS_REQUIRED
    if (newFirstName.size < 1) throw VET_FIRST_NAME_MIN_SIZE
    if (newFirstName.size > 40) throw VET_FIRST_NAME_MAX_SIZE

    _firstName = newFirstName
  }

  private var lastName_persisted: String = lastName
  def lastNamePersisted: String = lastName_persisted

  def lastName: String = _lastName
  def lastName_=(newLastName: String)(implicit session: SlickSession): Any = if (newLastName != lastName) {

    if (newLastName == null) throw VET_LAST_NAME_IS_REQUIRED
    if (newLastName.size < 1) throw VET_LAST_NAME_MIN_SIZE
    if (newLastName.size > 60) throw VET_LAST_NAME_MAX_SIZE

    _lastName = newLastName
  }


  def this(entity: Vet) = this(entity._id, entity._firstName, entity._lastName)

  def this() = this(0, "", "")

  def this(firstName: String, lastName: String)(implicit session: SlickSession) = {
    this()
    this.firstName_=(firstName)(session)
    this.lastName_=(lastName)(session)
  }

  def persisted() = {
    id_persisted = id
    firstName_persisted = firstName
    lastName_persisted = lastName
  }
}

object Vet {
  val ID: String = "_id"
  val FIRSTNAME: String = "_firstName"
  val LASTNAME: String = "_lastName"
}

object VET_FIRST_NAME_IS_REQUIRED extends DataConstraintException("VET_FIRST_NAME_IS_REQUIRED")

object VET_FIRST_NAME_MIN_SIZE extends DataConstraintException("VET_FIRST_NAME_MIN_SIZE")

object VET_FIRST_NAME_MAX_SIZE extends DataConstraintException("VET_FIRST_NAME_MAX_SIZE")

object VET_LAST_NAME_IS_REQUIRED extends DataConstraintException("VET_LAST_NAME_IS_REQUIRED")

object VET_LAST_NAME_MIN_SIZE extends DataConstraintException("VET_LAST_NAME_MIN_SIZE")

object VET_LAST_NAME_MAX_SIZE extends DataConstraintException("VET_LAST_NAME_MAX_SIZE")

object VET_DOESNT_EXIST extends DataConstraintException("VET_DOESNT_EXIST")

object VET_ID_IS_NOT_UNIQUE extends DataConstraintException("VET_ID_IS_NOT_UNIQUE")

class Vets(tag: Tag) extends Table[Vet](tag, "Vet") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("firstName")
  def lastName = column[String]("lastName")

  val create = Vet.apply _
  def * = (id, firstName, lastName) <> (create.tupled, Vet.unapply)
  def ? = (id.?, firstName.?, lastName.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))


}

class VetDao extends GenericSlickDao[Vet] {

  def save(entity: Vet)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[Vets]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[Vet])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[Vets]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: Vet)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[Vets]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[Vet] = {
    logger.trace(s".findAll()")

    var query: Query[Vets, Vets#TableElementType, Seq] = TableQuery[Vets]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[Vets, Vets#TableElementType, Seq] = TableQuery[Vets]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): Vet = {
    logger.trace(s".getById(id: $id)")

    var query: Query[Vets, Vets#TableElementType, Seq] = TableQuery[Vets]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw VET_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[Vets, Vets#TableElementType, Seq] = TableQuery[Vets]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[Vet] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[Vets, Vets#TableElementType, Seq] = TableQuery[Vets]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByFirstName(firstName: String)(implicit session: SlickSession): List[Vet] = {
    logger.trace(s".findByFirstName(firstName: $firstName)")

    var query: Query[Vets, Vets#TableElementType, Seq] = TableQuery[Vets]
    query = query.filter(_.firstName === firstName)

    query.list
  }

  def findByLastName(lastName: String)(implicit session: SlickSession): List[Vet] = {
    logger.trace(s".findByLastName(lastName: $lastName)")

    var query: Query[Vets, Vets#TableElementType, Seq] = TableQuery[Vets]
    query = query.filter(_.lastName === lastName)

    query.list
  }

}
