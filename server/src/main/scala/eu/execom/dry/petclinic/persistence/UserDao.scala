package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class User(private var _id: Int, private var _roleId: Int, private var _email: String, private var _passwordHash: Option[String], private var _facebookId: Option[String], private var _googleId: Option[String]) {

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

  private var email_persisted: String = email
  def emailPersisted: String = email_persisted

  def email: String = _email
  def email_=(newEmail: String)(implicit session: SlickSession): Any = if (newEmail != email) {

    if (newEmail == null) throw USER_EMAIL_IS_REQUIRED
    if (TableQuery[Users].filter(_.email === newEmail).exists.run) throw USER_EMAIL_IS_NOT_UNIQUE
    if (newEmail.size < 0) throw USER_EMAIL_MIN_SIZE
    if (newEmail.size > 1024) throw USER_EMAIL_MAX_SIZE

    _email = newEmail
  }

  private var passwordHash_persisted: Option[String] = passwordHash
  def passwordHashPersisted: Option[String] = passwordHash_persisted

  def passwordHash: Option[String] = _passwordHash
  def passwordHash_=(newPasswordHash: Option[String])(implicit session: SlickSession): Any = if (newPasswordHash != passwordHash) {
    if (newPasswordHash.isDefined) {
      if (newPasswordHash.get.size < 0) throw USER_PASSWORD_HASH_MIN_SIZE
      if (newPasswordHash.get.size > 1024) throw USER_PASSWORD_HASH_MAX_SIZE
    }
    _passwordHash = newPasswordHash
  }

  private var facebookId_persisted: Option[String] = facebookId
  def facebookIdPersisted: Option[String] = facebookId_persisted

  def facebookId: Option[String] = _facebookId
  def facebookId_=(newFacebookId: Option[String])(implicit session: SlickSession): Any = if (newFacebookId != facebookId) {
    if (newFacebookId.isDefined) {
      if (TableQuery[Users].filter(_.facebookId === newFacebookId.get).exists.run) throw USER_FACEBOOK_ID_IS_NOT_UNIQUE
      if (newFacebookId.get.size < 0) throw USER_FACEBOOK_ID_MIN_SIZE
      if (newFacebookId.get.size > 50) throw USER_FACEBOOK_ID_MAX_SIZE
    }
    _facebookId = newFacebookId
  }

  private var googleId_persisted: Option[String] = googleId
  def googleIdPersisted: Option[String] = googleId_persisted

  def googleId: Option[String] = _googleId
  def googleId_=(newGoogleId: Option[String])(implicit session: SlickSession): Any = if (newGoogleId != googleId) {
    if (newGoogleId.isDefined) {
      if (TableQuery[Users].filter(_.googleId === newGoogleId.get).exists.run) throw USER_GOOGLE_ID_IS_NOT_UNIQUE
      if (newGoogleId.get.size < 0) throw USER_GOOGLE_ID_MIN_SIZE
      if (newGoogleId.get.size > 50) throw USER_GOOGLE_ID_MAX_SIZE
    }
    _googleId = newGoogleId
  }
  def role(implicit session: SlickSession): Role = TableQuery[Roles].filter(_.id === roleId).first
  def role_=(role: Role)(implicit session: SlickSession) = roleId = role.id

  def this(entity: User) = this(entity._id, entity._roleId, entity._email, entity._passwordHash, entity._facebookId, entity._googleId)

  def this() = this(0, 0, "", None, None, None)

  def this(roleId: Int, email: String, passwordHash: Option[String], facebookId: Option[String], googleId: Option[String])(implicit session: SlickSession) = {
    this()
    this.roleId_=(roleId)(session)
    this.email_=(email)(session)
    this.passwordHash_=(passwordHash)(session)
    this.facebookId_=(facebookId)(session)
    this.googleId_=(googleId)(session)
  }

  def this(role: Role, email: String, passwordHash: Option[String], facebookId: Option[String], googleId: Option[String])(implicit session: SlickSession) = {
    this()
    this.role_=(role)(session)
    this.email_=(email)(session)
    this.passwordHash_=(passwordHash)(session)
    this.facebookId_=(facebookId)(session)
    this.googleId_=(googleId)(session)
  }

  def persisted() = {
    id_persisted = id
    roleId_persisted = roleId
    email_persisted = email
    passwordHash_persisted = passwordHash
    facebookId_persisted = facebookId
    googleId_persisted = googleId
  }
}

object User {
  val ID: String = "_id"
  val ROLEID: String = "_roleId"
  val EMAIL: String = "_email"
  val PASSWORDHASH: String = "_passwordHash"
  val FACEBOOKID: String = "_facebookId"
  val GOOGLEID: String = "_googleId"
}

object USER_EMAIL_IS_REQUIRED extends DataConstraintException("USER_EMAIL_IS_REQUIRED")

object USER_EMAIL_MIN_SIZE extends DataConstraintException("USER_EMAIL_MIN_SIZE")

object USER_EMAIL_MAX_SIZE extends DataConstraintException("USER_EMAIL_MAX_SIZE")

object USER_PASSWORD_HASH_MIN_SIZE extends DataConstraintException("USER_PASSWORD_HASH_MIN_SIZE")

object USER_PASSWORD_HASH_MAX_SIZE extends DataConstraintException("USER_PASSWORD_HASH_MAX_SIZE")

object USER_FACEBOOK_ID_MIN_SIZE extends DataConstraintException("USER_FACEBOOK_ID_MIN_SIZE")

object USER_FACEBOOK_ID_MAX_SIZE extends DataConstraintException("USER_FACEBOOK_ID_MAX_SIZE")

object USER_GOOGLE_ID_MIN_SIZE extends DataConstraintException("USER_GOOGLE_ID_MIN_SIZE")

object USER_GOOGLE_ID_MAX_SIZE extends DataConstraintException("USER_GOOGLE_ID_MAX_SIZE")

object USER_DOESNT_EXIST extends DataConstraintException("USER_DOESNT_EXIST")

object USER_ID_IS_NOT_UNIQUE extends DataConstraintException("USER_ID_IS_NOT_UNIQUE")

object USER_EMAIL_IS_NOT_UNIQUE extends DataConstraintException("USER_EMAIL_IS_NOT_UNIQUE")

object USER_FACEBOOK_ID_IS_NOT_UNIQUE extends DataConstraintException("USER_FACEBOOK_ID_IS_NOT_UNIQUE")

object USER_GOOGLE_ID_IS_NOT_UNIQUE extends DataConstraintException("USER_GOOGLE_ID_IS_NOT_UNIQUE")

class Users(tag: Tag) extends Table[User](tag, "User") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def roleId = column[Int]("roleId")
  def email = column[String]("email")
  def passwordHash = column[Option[String]]("passwordHash")
  def facebookId = column[Option[String]]("facebookId")
  def googleId = column[Option[String]]("googleId")

  val create = User.apply _
  def * = (id, roleId, email, passwordHash, facebookId, googleId) <> (create.tupled, User.unapply)
  def ? = (id.?, roleId.?, email.?, passwordHash, facebookId, googleId).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3.get, _4, _5, _6)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  def role= foreignKey("USER_ROLE_FK", roleId, TableQuery[Roles])(_.id)
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

  def findByRoleId(roleId: Int)(implicit session: SlickSession): List[User] = {
    logger.trace(s".findByRoleId(roleId: $roleId)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.roleId === roleId)

    query.list
  }

  def findByEmail(email: String)(implicit session: SlickSession): Option[User] = {
    logger.trace(s".findByEmail(email: $email)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.email === email)

    query.firstOption
  }

  def findByPasswordHash(passwordHash: Option[String])(implicit session: SlickSession): List[User] = {
    logger.trace(s".findByPasswordHash(passwordHash: $passwordHash)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    passwordHash match {
      case Some(value) => query = query.filter(_.passwordHash === value)
      case None => query = query.filter(_.passwordHash.isEmpty)
    }

    query.list
  }

  def findByFacebookId(facebookId: Option[String])(implicit session: SlickSession): Option[User] = {
    logger.trace(s".findByFacebookId(facebookId: $facebookId)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    facebookId match {
      case Some(value) => query = query.filter(_.facebookId === value)
      case None => query = query.filter(_.facebookId.isEmpty)
    }

    query.firstOption
  }

  def findByGoogleId(googleId: Option[String])(implicit session: SlickSession): Option[User] = {
    logger.trace(s".findByGoogleId(googleId: $googleId)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    googleId match {
      case Some(value) => query = query.filter(_.googleId === value)
      case None => query = query.filter(_.googleId.isEmpty)
    }

    query.firstOption
  }

  def findAllPaged(from: Int, maxRowCount: Int)(implicit session: SlickSession): List[User] = {
    logger.trace(s".findAllPaged(from: $from, maxRowCount: $maxRowCount)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]

    query.drop(from).take(maxRowCount).list
  }

  def findByRolePaged(role: Int, from: Int, maxRowCount: Int)(implicit session: SlickSession): List[User] = {
    logger.trace(s".findByRolePaged(role: $role, from: $from, maxRowCount: $maxRowCount)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.roleId === role)

    query.drop(from).take(maxRowCount).list
  }

  def countByRole(role: Int)(implicit session: SlickSession): Int = {
    logger.trace(s".countByRole(role: $role)")

    var query: Query[Users, Users#TableElementType, Seq] = TableQuery[Users]
    query = query.filter(_.roleId === role)

    query.length.run
  }

}
