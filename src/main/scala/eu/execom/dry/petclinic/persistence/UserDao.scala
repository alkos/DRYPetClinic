package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class User(private var _id: Int, private var _authenticationCode: Option[String], private var _role: String, private var _username: String, private var _passwordHash: String) {

  var id_persisted: Int = id
  def id: Int = _id
  def id_=(newId: Int)(implicit session: SlickSession): Any = if (newId != id) {

    _id = newId
  }

  var authenticationCode_persisted: Option[String] = authenticationCode
  def authenticationCode: Option[String] = _authenticationCode
  def authenticationCode_=(newAuthenticationCode: Option[String])(implicit session: SlickSession): Any = if (newAuthenticationCode != authenticationCode) {
    if (newAuthenticationCode.isDefined) {
      if (TableQuery[Users].filter(_.authenticationCode === newAuthenticationCode.get).exists.run) throw USER_AUTHENTICATIONCODE_IS_NOT_UNIQUE
      if (newAuthenticationCode.get.size < 0) throw USER_AUTHENTICATIONCODE_MIN_SIZE
      if (newAuthenticationCode.get.size > 128) throw USER_AUTHENTICATIONCODE_MAX_SIZE
    }
    _authenticationCode = newAuthenticationCode
  }

  var role_persisted: UserRole = role
  def role: UserRole = UserRole.withName(_role)
  def role_=(newRole: UserRole)(implicit session: SlickSession): Any = if (newRole != role) {

    if (newRole == null) throw USER_ROLE_IS_REQUIRED

    _role = newRole.name
  }

  var username_persisted: String = username
  def username: String = _username
  def username_=(newUsername: String)(implicit session: SlickSession): Any = if (newUsername != username) {

    if (newUsername == null) throw USER_USERNAME_IS_REQUIRED
    if (TableQuery[Users].filter(_.username === newUsername).exists.run) throw USER_USERNAME_IS_NOT_UNIQUE
    if (newUsername.size < 0) throw USER_USERNAME_MIN_SIZE
    if (newUsername.size > 1024) throw USER_USERNAME_MAX_SIZE

    _username = newUsername
  }

  var passwordHash_persisted: String = passwordHash
  def passwordHash: String = _passwordHash
  def passwordHash_=(newPasswordHash: String)(implicit session: SlickSession): Any = if (newPasswordHash != passwordHash) {

    if (newPasswordHash == null) throw USER_PASSWORDHASH_IS_REQUIRED
    if (newPasswordHash.size < 0) throw USER_PASSWORDHASH_MIN_SIZE
    if (newPasswordHash.size > 1024) throw USER_PASSWORDHASH_MAX_SIZE

    _passwordHash = newPasswordHash
  }


  def this(entity: User) = this(entity._id, entity._authenticationCode, entity._role, entity._username, entity._passwordHash)

  def this() = this(0, None, UserRole.ADMIN.name, "", "")

  def this(authenticationCode: Option[String], role: UserRole, username: String, passwordHash: String)(implicit session: SlickSession) = {
    this()
    this.authenticationCode_=(authenticationCode)(session)
    this.role_=(role)(session)
    this.username_=(username)(session)
    this.passwordHash_=(passwordHash)(session)
  }

  def persisted() = {
    id_persisted = id
    authenticationCode_persisted = authenticationCode
    role_persisted = role
    username_persisted = username
    passwordHash_persisted = passwordHash
  }
}

object User {
  val ID: String = "_id"
  val AUTHENTICATIONCODE: String = "_authenticationCode"
  val ROLE: String = "_role"
  val USERNAME: String = "_username"
  val PASSWORDHASH: String = "_passwordHash"
}

object USER_AUTHENTICATIONCODE_MIN_SIZE extends DataConstraintException("USER_AUTHENTICATIONCODE_MIN_SIZE")

object USER_AUTHENTICATIONCODE_MAX_SIZE extends DataConstraintException("USER_AUTHENTICATIONCODE_MAX_SIZE")

object USER_ROLE_IS_REQUIRED extends BadRequestException("USER_ROLE_IS_REQUIRED")

object USER_USERNAME_MIN_SIZE extends DataConstraintException("USER_USERNAME_MIN_SIZE")

object USER_USERNAME_MAX_SIZE extends DataConstraintException("USER_USERNAME_MAX_SIZE")

object USER_USERNAME_IS_REQUIRED extends BadRequestException("USER_USERNAME_IS_REQUIRED")

object USER_PASSWORDHASH_MIN_SIZE extends DataConstraintException("USER_PASSWORDHASH_MIN_SIZE")

object USER_PASSWORDHASH_MAX_SIZE extends DataConstraintException("USER_PASSWORDHASH_MAX_SIZE")

object USER_PASSWORDHASH_IS_REQUIRED extends BadRequestException("USER_PASSWORDHASH_IS_REQUIRED")

object USER_DOESNT_EXIST extends DataConstraintException("USER_DOESNT_EXIST")

object USER_ID_IS_NOT_UNIQUE extends DataConstraintException("USER_ID_IS_NOT_UNIQUE")

object USER_AUTHENTICATIONCODE_IS_NOT_UNIQUE extends DataConstraintException("USER_AUTHENTICATIONCODE_IS_NOT_UNIQUE")

object USER_USERNAME_IS_NOT_UNIQUE extends DataConstraintException("USER_USERNAME_IS_NOT_UNIQUE")

class Users(tag: Tag) extends Table[User](tag, "User") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def authenticationCode = column[Option[String]]("authenticationCode")
  def role = column[String]("role")
  def username = column[String]("username")
  def passwordHash = column[String]("passwordHash")

  val create = User.apply _
  def * = (id, authenticationCode, role, username, passwordHash) <> (create.tupled, User.unapply)
  def ? = (id.?, authenticationCode, role.?, username.?, passwordHash.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))


}

class UserDao extends GenericSlickDao[User] {

  def save(entity: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[Users]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[User])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[Users]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: User)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[Users]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[User] = {
    logger.trace(s".findAll()")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): User = {
    logger.trace(s".getById(id: $id)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw USER_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[User] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByAuthenticationCode(authenticationCode: Option[String])(implicit session: SlickSession): Option[User] = {
    logger.trace(s".findByAuthenticationCode(authenticationCode: $authenticationCode)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    authenticationCode match {
      case Some(value) => query = query.filter(_.authenticationCode === value)
      case None => query = query.filter(_.authenticationCode.isEmpty)
    }

    query.firstOption
  }

  def findByRole(role: UserRole)(implicit session: SlickSession): List[User] = {
    logger.trace(s".findByRole(role: $role)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.role === role.name)

    query.list
  }

  def findByUsername(username: String)(implicit session: SlickSession): Option[User] = {
    logger.trace(s".findByUsername(username: $username)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.username === username)

    query.firstOption
  }

  def findByPasswordHash(passwordHash: String)(implicit session: SlickSession): List[User] = {
    logger.trace(s".findByPasswordHash(passwordHash: $passwordHash)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.passwordHash === passwordHash)

    query.list
  }

  def findAllPaged(from: Int, maxRowCount: Int)(implicit session: SlickSession): List[User] = {
    logger.trace(s".findAllPaged(from: $from, maxRowCount: $maxRowCount)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]

    query.drop(from).take(maxRowCount).list
  }

}
