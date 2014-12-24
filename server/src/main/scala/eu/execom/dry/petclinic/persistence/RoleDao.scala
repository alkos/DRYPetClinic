package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class Role(private var _id: Int, private var _name: String) {

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

    if (newName == null) throw ROLE_NAME_IS_REQUIRED
    if (TableQuery[Roles].filter(_.name === newName).exists.run) throw ROLE_NAME_IS_NOT_UNIQUE
    if (newName.size < 40) throw ROLE_NAME_MIN_SIZE
    if (newName.size > 20) throw ROLE_NAME_MAX_SIZE

    _name = newName
  }


  def this(entity: Role) = this(entity._id, entity._name)

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

object Role {
  val ID: String = "_id"
  val NAME: String = "_name"
}

object ROLE_NAME_MIN_SIZE extends DataConstraintException("ROLE_NAME_MIN_SIZE")

object ROLE_NAME_MAX_SIZE extends DataConstraintException("ROLE_NAME_MAX_SIZE")

object ROLE_NAME_IS_REQUIRED extends BadRequestException("ROLE_NAME_IS_REQUIRED")

object ROLE_DOESNT_EXIST extends DataConstraintException("ROLE_DOESNT_EXIST")

object ROLE_ID_IS_NOT_UNIQUE extends DataConstraintException("ROLE_ID_IS_NOT_UNIQUE")

object ROLE_NAME_IS_NOT_UNIQUE extends DataConstraintException("ROLE_NAME_IS_NOT_UNIQUE")

class Roles(tag: Tag) extends Table[Role](tag, "Role") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  val create = Role.apply _
  def * = (id, name) <> (create.tupled, Role.unapply)
  def ? = (id.?, name.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))


}

class RoleDao extends GenericSlickDao[Role] {

  def save(entity: Role)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[Roles]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[Role])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[Roles]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: Role)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[Roles]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[Role] = {
    logger.trace(s".findAll()")

    var query: Query[Roles, Roles#TableElementType, Seq] = TableQuery[Roles]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[Roles, Roles#TableElementType, Seq] = TableQuery[Roles]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): Role = {
    logger.trace(s".getById(id: $id)")

    var query: Query[Roles, Roles#TableElementType, Seq] = TableQuery[Roles]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw ROLE_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[Roles, Roles#TableElementType, Seq] = TableQuery[Roles]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[Role] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[Roles, Roles#TableElementType, Seq] = TableQuery[Roles]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByName(name: String)(implicit session: SlickSession): Option[Role] = {
    logger.trace(s".findByName(name: $name)")

    var query: Query[Roles, Roles#TableElementType, Seq] = TableQuery[Roles]
    query = query.filter(_.name === name)

    query.firstOption
  }

}
