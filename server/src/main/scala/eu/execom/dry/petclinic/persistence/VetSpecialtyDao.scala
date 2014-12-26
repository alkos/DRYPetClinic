package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class VetSpecialty(private var _id: Int, private var _name: String) {

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

    if (newName == null) throw VETSPECIALTY_NAME_IS_REQUIRED
    if (newName.size < 3) throw VETSPECIALTY_NAME_MIN_SIZE
    if (newName.size > 50) throw VETSPECIALTY_NAME_MAX_SIZE

    _name = newName
  }


  def this(entity: VetSpecialty) = this(entity._id, entity._name)

  def this() = this(0, "")

  def this(name: String)(implicit session: SlickSession) = {
    this()
    this.name_=(name)(session)
  }

  def persisted() = {
    id_persisted = id
    name_persisted = name
  }
}

object VetSpecialty {
  val ID: String = "_id"
  val NAME: String = "_name"
}

object VETSPECIALTY_NAME_MIN_SIZE extends DataConstraintException("VETSPECIALTY_NAME_MIN_SIZE")

object VETSPECIALTY_NAME_MAX_SIZE extends DataConstraintException("VETSPECIALTY_NAME_MAX_SIZE")

object VETSPECIALTY_NAME_IS_REQUIRED extends DataConstraintException("VETSPECIALTY_NAME_IS_REQUIRED")

object VETSPECIALTY_DOESNT_EXIST extends DataConstraintException("VETSPECIALTY_DOESNT_EXIST")

object VETSPECIALTY_ID_IS_NOT_UNIQUE extends DataConstraintException("VETSPECIALTY_ID_IS_NOT_UNIQUE")

class VetSpecialtys(tag: Tag) extends Table[VetSpecialty](tag, "VetSpecialty") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  val create = VetSpecialty.apply _
  def * = (id, name) <> (create.tupled, VetSpecialty.unapply)
  def ? = (id.?, name.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))


}

class VetSpecialtyDao extends GenericSlickDao[VetSpecialty] {

  def save(entity: VetSpecialty)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[VetSpecialtys]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[VetSpecialty])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[VetSpecialtys]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: VetSpecialty)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[VetSpecialtys]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[VetSpecialty] = {
    logger.trace(s".findAll()")

    var query: Query[VetSpecialtys, VetSpecialtys#TableElementType, Seq] = TableQuery[VetSpecialtys]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[VetSpecialtys, VetSpecialtys#TableElementType, Seq] = TableQuery[VetSpecialtys]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): VetSpecialty = {
    logger.trace(s".getById(id: $id)")

    var query: Query[VetSpecialtys, VetSpecialtys#TableElementType, Seq] = TableQuery[VetSpecialtys]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw VETSPECIALTY_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[VetSpecialtys, VetSpecialtys#TableElementType, Seq] = TableQuery[VetSpecialtys]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[VetSpecialty] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[VetSpecialtys, VetSpecialtys#TableElementType, Seq] = TableQuery[VetSpecialtys]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByName(name: String)(implicit session: SlickSession): List[VetSpecialty] = {
    logger.trace(s".findByName(name: $name)")

    var query: Query[VetSpecialtys, VetSpecialtys#TableElementType, Seq] = TableQuery[VetSpecialtys]
    query = query.filter(_.name === name)

    query.list
  }

}
