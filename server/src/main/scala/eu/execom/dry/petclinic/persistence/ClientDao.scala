package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class Client(private var _id: Int, private var _userId: Int, private var _clientId: Option[String], private var _accessToken: String, private var _accessTokenExpires: Date, private var _refreshToken: String, private var _refreshTokenExpires: Date) {

  private var id_persisted: Int = id
  def idPersisted: Int = id_persisted

  def id: Int = _id
  def id_=(newId: Int)(implicit session: SlickSession): Any = if (newId != id) {

    _id = newId
  }

  private var userId_persisted: Int = userId
  def userIdPersisted: Int = userId_persisted

  def userId: Int = _userId
  def userId_=(newUserId: Int)(implicit session: SlickSession): Any = if (newUserId != userId) {

    _userId = newUserId
  }

  private var clientId_persisted: Option[String] = clientId
  def clientIdPersisted: Option[String] = clientId_persisted

  def clientId: Option[String] = _clientId
  def clientId_=(newClientId: Option[String])(implicit session: SlickSession): Any = if (newClientId != clientId) {
    if (newClientId.isDefined) {
      if (TableQuery[Clients].filter(_.clientId === newClientId.get).exists.run) throw CLIENT_CLIENT_ID_IS_NOT_UNIQUE
      if (newClientId.get.size < 0) throw CLIENT_CLIENT_ID_MIN_SIZE
      if (newClientId.get.size > 128) throw CLIENT_CLIENT_ID_MAX_SIZE
    }
    _clientId = newClientId
  }

  private var accessToken_persisted: String = accessToken
  def accessTokenPersisted: String = accessToken_persisted

  def accessToken: String = _accessToken
  def accessToken_=(newAccessToken: String)(implicit session: SlickSession): Any = if (newAccessToken != accessToken) {

    if (newAccessToken == null) throw CLIENT_ACCESS_TOKEN_IS_REQUIRED
    if (TableQuery[Clients].filter(_.accessToken === newAccessToken).exists.run) throw CLIENT_ACCESS_TOKEN_IS_NOT_UNIQUE
    if (newAccessToken.size < 0) throw CLIENT_ACCESS_TOKEN_MIN_SIZE
    if (newAccessToken.size > 128) throw CLIENT_ACCESS_TOKEN_MAX_SIZE

    _accessToken = newAccessToken
  }

  private var accessTokenExpires_persisted: DateTime = accessTokenExpires
  def accessTokenExpiresPersisted: DateTime = accessTokenExpires_persisted

  def accessTokenExpires: DateTime = new org.joda.time.DateTime(_accessTokenExpires)
  def accessTokenExpires_=(newAccessTokenExpires: DateTime)(implicit session: SlickSession): Any = if (newAccessTokenExpires != accessTokenExpires) {

    if (newAccessTokenExpires == null) throw CLIENT_ACCESS_TOKEN_EXPIRES_IS_REQUIRED

    _accessTokenExpires = new java.sql.Date(newAccessTokenExpires.getMillis)
  }

  private var refreshToken_persisted: String = refreshToken
  def refreshTokenPersisted: String = refreshToken_persisted

  def refreshToken: String = _refreshToken
  def refreshToken_=(newRefreshToken: String)(implicit session: SlickSession): Any = if (newRefreshToken != refreshToken) {

    if (newRefreshToken == null) throw CLIENT_REFRESH_TOKEN_IS_REQUIRED
    if (TableQuery[Clients].filter(_.refreshToken === newRefreshToken).exists.run) throw CLIENT_REFRESH_TOKEN_IS_NOT_UNIQUE
    if (newRefreshToken.size < 0) throw CLIENT_REFRESH_TOKEN_MIN_SIZE
    if (newRefreshToken.size > 128) throw CLIENT_REFRESH_TOKEN_MAX_SIZE

    _refreshToken = newRefreshToken
  }

  private var refreshTokenExpires_persisted: DateTime = refreshTokenExpires
  def refreshTokenExpiresPersisted: DateTime = refreshTokenExpires_persisted

  def refreshTokenExpires: DateTime = new org.joda.time.DateTime(_refreshTokenExpires)
  def refreshTokenExpires_=(newRefreshTokenExpires: DateTime)(implicit session: SlickSession): Any = if (newRefreshTokenExpires != refreshTokenExpires) {

    if (newRefreshTokenExpires == null) throw CLIENT_REFRESH_TOKEN_EXPIRES_IS_REQUIRED

    _refreshTokenExpires = new java.sql.Date(newRefreshTokenExpires.getMillis)
  }
  def user(implicit session: SlickSession): User = TableQuery[Users].filter(_.id === userId).first
  def user_=(user: User)(implicit session: SlickSession) = userId = user.id

  def this(entity: Client) = this(entity._id, entity._userId, entity._clientId, entity._accessToken, entity._accessTokenExpires, entity._refreshToken, entity._refreshTokenExpires)

  def this() = this(0, 0, None, "", new java.sql.Date(DateTime.now(DateTimeZone.UTC).getMillis), "", new java.sql.Date(DateTime.now(DateTimeZone.UTC).getMillis))

  def this(userId: Int, clientId: Option[String], accessToken: String, accessTokenExpires: DateTime, refreshToken: String, refreshTokenExpires: DateTime)(implicit session: SlickSession) = {
    this()
    this.userId_=(userId)(session)
    this.clientId_=(clientId)(session)
    this.accessToken_=(accessToken)(session)
    this.accessTokenExpires_=(accessTokenExpires)(session)
    this.refreshToken_=(refreshToken)(session)
    this.refreshTokenExpires_=(refreshTokenExpires)(session)
  }

  def this(user: User, clientId: Option[String], accessToken: String, accessTokenExpires: DateTime, refreshToken: String, refreshTokenExpires: DateTime)(implicit session: SlickSession) = {
    this()
    this.user_=(user)(session)
    this.clientId_=(clientId)(session)
    this.accessToken_=(accessToken)(session)
    this.accessTokenExpires_=(accessTokenExpires)(session)
    this.refreshToken_=(refreshToken)(session)
    this.refreshTokenExpires_=(refreshTokenExpires)(session)
  }

  def persisted() = {
    id_persisted = id
    userId_persisted = userId
    clientId_persisted = clientId
    accessToken_persisted = accessToken
    accessTokenExpires_persisted = accessTokenExpires
    refreshToken_persisted = refreshToken
    refreshTokenExpires_persisted = refreshTokenExpires
  }
}

object Client {
  val ID: String = "_id"
  val USERID: String = "_userId"
  val CLIENTID: String = "_clientId"
  val ACCESSTOKEN: String = "_accessToken"
  val ACCESSTOKENEXPIRES: String = "_accessTokenExpires"
  val REFRESHTOKEN: String = "_refreshToken"
  val REFRESHTOKENEXPIRES: String = "_refreshTokenExpires"
}

object CLIENT_CLIENT_ID_MIN_SIZE extends DataConstraintException("CLIENT_CLIENT_ID_MIN_SIZE")

object CLIENT_CLIENT_ID_MAX_SIZE extends DataConstraintException("CLIENT_CLIENT_ID_MAX_SIZE")

object CLIENT_ACCESS_TOKEN_IS_REQUIRED extends DataConstraintException("CLIENT_ACCESS_TOKEN_IS_REQUIRED")

object CLIENT_ACCESS_TOKEN_MIN_SIZE extends DataConstraintException("CLIENT_ACCESS_TOKEN_MIN_SIZE")

object CLIENT_ACCESS_TOKEN_MAX_SIZE extends DataConstraintException("CLIENT_ACCESS_TOKEN_MAX_SIZE")

object CLIENT_ACCESS_TOKEN_EXPIRES_IS_REQUIRED extends DataConstraintException("CLIENT_ACCESS_TOKEN_EXPIRES_IS_REQUIRED")

object CLIENT_REFRESH_TOKEN_IS_REQUIRED extends DataConstraintException("CLIENT_REFRESH_TOKEN_IS_REQUIRED")

object CLIENT_REFRESH_TOKEN_MIN_SIZE extends DataConstraintException("CLIENT_REFRESH_TOKEN_MIN_SIZE")

object CLIENT_REFRESH_TOKEN_MAX_SIZE extends DataConstraintException("CLIENT_REFRESH_TOKEN_MAX_SIZE")

object CLIENT_REFRESH_TOKEN_EXPIRES_IS_REQUIRED extends DataConstraintException("CLIENT_REFRESH_TOKEN_EXPIRES_IS_REQUIRED")

object CLIENT_DOESNT_EXIST extends DataConstraintException("CLIENT_DOESNT_EXIST")

object CLIENT_ID_IS_NOT_UNIQUE extends DataConstraintException("CLIENT_ID_IS_NOT_UNIQUE")

object CLIENT_CLIENT_ID_IS_NOT_UNIQUE extends DataConstraintException("CLIENT_CLIENT_ID_IS_NOT_UNIQUE")

object CLIENT_ACCESS_TOKEN_IS_NOT_UNIQUE extends DataConstraintException("CLIENT_ACCESS_TOKEN_IS_NOT_UNIQUE")

object CLIENT_REFRESH_TOKEN_IS_NOT_UNIQUE extends DataConstraintException("CLIENT_REFRESH_TOKEN_IS_NOT_UNIQUE")

class Clients(tag: Tag) extends Table[Client](tag, "Client") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("userId")
  def clientId = column[Option[String]]("clientId")
  def accessToken = column[String]("accessToken")
  def accessTokenExpires = column[Date]("accessTokenExpires")
  def refreshToken = column[String]("refreshToken")
  def refreshTokenExpires = column[Date]("refreshTokenExpires")

  val create = Client.apply _
  def * = (id, userId, clientId, accessToken, accessTokenExpires, refreshToken, refreshTokenExpires) <> (create.tupled, Client.unapply)
  def ? = (id.?, userId.?, clientId, accessToken.?, accessTokenExpires.?, refreshToken.?, refreshTokenExpires.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  def user= foreignKey("CLIENT_USER_FK", userId, TableQuery[Users])(_.id)
}

class ClientDao extends GenericSlickDao[Client] {

  def save(entity: Client)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[Clients]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[Client])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[Clients]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: Client)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[Clients]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[Client] = {
    logger.trace(s".findAll()")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): Client = {
    logger.trace(s".getById(id: $id)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw CLIENT_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[Client] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByUserId(userId: Int)(implicit session: SlickSession): List[Client] = {
    logger.trace(s".findByUserId(userId: $userId)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    query = query.filter(_.userId === userId)

    query.list
  }

  def findByClientId(clientId: Option[String])(implicit session: SlickSession): Option[Client] = {
    logger.trace(s".findByClientId(clientId: $clientId)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    clientId match {
      case Some(value) => query = query.filter(_.clientId === value)
      case None => query = query.filter(_.clientId.isEmpty)
    }

    query.firstOption
  }

  def findByAccessToken(accessToken: String)(implicit session: SlickSession): Option[Client] = {
    logger.trace(s".findByAccessToken(accessToken: $accessToken)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    query = query.filter(_.accessToken === accessToken)

    query.firstOption
  }

  def findByAccessTokenExpires(accessTokenExpires: DateTime)(implicit session: SlickSession): List[Client] = {
    logger.trace(s".findByAccessTokenExpires(accessTokenExpires: $accessTokenExpires)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    query = query.filter(_.accessTokenExpires === new java.sql.Date(accessTokenExpires.getMillis))

    query.list
  }

  def findByRefreshToken(refreshToken: String)(implicit session: SlickSession): Option[Client] = {
    logger.trace(s".findByRefreshToken(refreshToken: $refreshToken)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    query = query.filter(_.refreshToken === refreshToken)

    query.firstOption
  }

  def findByRefreshTokenExpires(refreshTokenExpires: DateTime)(implicit session: SlickSession): List[Client] = {
    logger.trace(s".findByRefreshTokenExpires(refreshTokenExpires: $refreshTokenExpires)")

    var query: Query[Clients, Clients#TableElementType, Seq] = TableQuery[Clients]
    query = query.filter(_.refreshTokenExpires === new java.sql.Date(refreshTokenExpires.getMillis))

    query.list
  }

}
