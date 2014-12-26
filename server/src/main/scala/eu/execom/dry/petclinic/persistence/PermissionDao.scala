package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class Permission(private var _id: Int, private var _roleId: Int, private var _accessRight: String) {

  private var id_persisted: Int = id
  def idPersisted: Int = id_persisted

  def id: Int = _id
  def id_=(newId: Int)(implicit session: SlickSession): Any = if (newId != id) {

    _id = newId
  }

  private var roleId_persisted: Int = roleId
  def roleIdPersisted: Int = roleId_persisted

  def roleId: Int = _roleId
  def roleId_=(newRoleId: Int)(implicit session: SlickSession): Any = if (newRoleId != roleId) {

    _roleId = newRoleId
  }

  private var accessRight_persisted: AccessRight = accessRight
  def accessRightPersisted: AccessRight = accessRight_persisted

  def accessRight: AccessRight = AccessRight.withName(_accessRight)
  def accessRight_=(newAccessRight: AccessRight)(implicit session: SlickSession): Any = if (newAccessRight != accessRight) {

    if (newAccessRight == null) throw PERMISSION_ACCESS_RIGHT_IS_REQUIRED

    _accessRight = newAccessRight.name
  }
  def role(implicit session: SlickSession): Role = TableQuery[Roles].filter(_.id === roleId).first
  def role_=(role: Role)(implicit session: SlickSession) = roleId = role.id

  def this(entity: Permission) = this(entity._id, entity._roleId, entity._accessRight)

  def this() = this(0, 0, AccessRight.ADD_USER.name)

  def this(roleId: Int, accessRight: AccessRight)(implicit session: SlickSession) = {
    this()
    this.roleId_=(roleId)(session)
    this.accessRight_=(accessRight)(session)
  }

  def this(role: Role, accessRight: AccessRight)(implicit session: SlickSession) = {
    this()
    this.role_=(role)(session)
    this.accessRight_=(accessRight)(session)
  }

  def persisted() = {
    id_persisted = id
    roleId_persisted = roleId
    accessRight_persisted = accessRight
  }
}

object Permission {
  val ID: String = "_id"
  val ROLEID: String = "_roleId"
  val ACCESSRIGHT: String = "_accessRight"
}

object PERMISSION_ACCESS_RIGHT_IS_REQUIRED extends DataConstraintException("PERMISSION_ACCESS_RIGHT_IS_REQUIRED")

object PERMISSION_DOESNT_EXIST extends DataConstraintException("PERMISSION_DOESNT_EXIST")

object PERMISSION_ID_IS_NOT_UNIQUE extends DataConstraintException("PERMISSION_ID_IS_NOT_UNIQUE")

object PERMISSION_ROLE_ID_ACCESS_RIGHT_IS_NOT_UNIQUE extends DataConstraintException("PERMISSION_ROLE_ID_ACCESS_RIGHT_IS_NOT_UNIQUE")

class Permissions(tag: Tag) extends Table[Permission](tag, "Permission") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def roleId = column[Int]("roleId")
  def accessRight = column[String]("accessRight")

  val create = Permission.apply _
  def * = (id, roleId, accessRight) <> (create.tupled, Permission.unapply)
  def ? = (id.?, roleId.?, accessRight.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  def role= foreignKey("PERMISSION_ROLE_FK", roleId, TableQuery[Roles])(_.id)
}

class PermissionDao extends GenericSlickDao[Permission] {

  def save(entity: Permission)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[Permissions]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[Permission])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[Permissions]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: Permission)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[Permissions]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[Permission] = {
    logger.trace(s".findAll()")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): Permission = {
    logger.trace(s".getById(id: $id)")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw PERMISSION_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[Permission] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByRoleId(roleId: Int)(implicit session: SlickSession): List[Permission] = {
    logger.trace(s".findByRoleId(roleId: $roleId)")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]
    query = query.filter(_.roleId === roleId)

    query.list
  }

  def findByAccessRight(accessRight: AccessRight)(implicit session: SlickSession): List[Permission] = {
    logger.trace(s".findByAccessRight(accessRight: $accessRight)")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]
    query = query.filter(_.accessRight === accessRight.name)

    query.list
  }

  def findByRoleIdAccessRight(roleId: Int, accessRight: AccessRight)(implicit session: SlickSession): Option[Permission] = {
    logger.trace(s".findByRoleIdAccessRight(roleId: $roleId, accessRight: $accessRight)")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]
    query = query.filter(_.roleId === roleId)
    query = query.filter(_.accessRight === accessRight.name)

    query.firstOption
  }

  def findByRoleAccessRights(role: Int, accessRights: List[AccessRight])(implicit session: SlickSession): Option[Permission] = {
    logger.trace(s".findByRoleAccessRights(role: $role, accessRights: $accessRights)")

    var query: Query[Permissions, Permissions#TableElementType, Seq] = TableQuery[Permissions]
    query = query.filter(_.roleId === role)
    query = query.filter(_.accessRight inSetBind accessRights.map(_.name))

    query.firstOption
  }

}
