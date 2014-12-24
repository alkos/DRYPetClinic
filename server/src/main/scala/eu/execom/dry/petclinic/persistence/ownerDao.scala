package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class owner(private var _id: Int, private var _firstName: String, private var _lastName: String, private var _address: String, private var _city: String, private var _telephone: String) {

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

    if (newFirstName == null) throw OWNER_FIRSTNAME_IS_REQUIRED
    if (newFirstName.size < 1) throw OWNER_FIRSTNAME_MIN_SIZE
    if (newFirstName.size > 40) throw OWNER_FIRSTNAME_MAX_SIZE

    _firstName = newFirstName
  }

  private var lastName_persisted: String = lastName
  def lastNamePersisted: String = lastName_persisted

  def lastName: String = _lastName
  def lastName_=(newLastName: String)(implicit session: SlickSession): Any = if (newLastName != lastName) {

    if (newLastName == null) throw OWNER_LASTNAME_IS_REQUIRED
    if (newLastName.size < 1) throw OWNER_LASTNAME_MIN_SIZE
    if (newLastName.size > 60) throw OWNER_LASTNAME_MAX_SIZE

    _lastName = newLastName
  }

  private var address_persisted: String = address
  def addressPersisted: String = address_persisted

  def address: String = _address
  def address_=(newAddress: String)(implicit session: SlickSession): Any = if (newAddress != address) {

    if (newAddress == null) throw OWNER_ADDRESS_IS_REQUIRED
    if (newAddress.size < 5) throw OWNER_ADDRESS_MIN_SIZE
    if (newAddress.size > 100) throw OWNER_ADDRESS_MAX_SIZE

    _address = newAddress
  }

  private var city_persisted: String = city
  def cityPersisted: String = city_persisted

  def city: String = _city
  def city_=(newCity: String)(implicit session: SlickSession): Any = if (newCity != city) {

    if (newCity == null) throw OWNER_CITY_IS_REQUIRED
    if (newCity.size < 2) throw OWNER_CITY_MIN_SIZE
    if (newCity.size > 50) throw OWNER_CITY_MAX_SIZE

    _city = newCity
  }

  private var telephone_persisted: String = telephone
  def telephonePersisted: String = telephone_persisted

  def telephone: String = _telephone
  def telephone_=(newTelephone: String)(implicit session: SlickSession): Any = if (newTelephone != telephone) {

    if (newTelephone == null) throw OWNER_TELEPHONE_IS_REQUIRED
    if (newTelephone.size < 5) throw OWNER_TELEPHONE_MIN_SIZE
    if (newTelephone.size > 15) throw OWNER_TELEPHONE_MAX_SIZE

    _telephone = newTelephone
  }


  def this(entity: owner) = this(entity._id, entity._firstName, entity._lastName, entity._address, entity._city, entity._telephone)

  def this() = this(0, "", "", "", "", "")

  def this(firstName: String, lastName: String, address: String, city: String, telephone: String)(implicit session: SlickSession) = {
    this()
    this.firstName_=(firstName)(session)
    this.lastName_=(lastName)(session)
    this.address_=(address)(session)
    this.city_=(city)(session)
    this.telephone_=(telephone)(session)
  }

  def persisted() = {
    id_persisted = id
    firstName_persisted = firstName
    lastName_persisted = lastName
    address_persisted = address
    city_persisted = city
    telephone_persisted = telephone
  }
}

object owner {
  val ID: String = "_id"
  val FIRSTNAME: String = "_firstName"
  val LASTNAME: String = "_lastName"
  val ADDRESS: String = "_address"
  val CITY: String = "_city"
  val TELEPHONE: String = "_telephone"
}

object OWNER_FIRSTNAME_MIN_SIZE extends DataConstraintException("OWNER_FIRSTNAME_MIN_SIZE")

object OWNER_FIRSTNAME_MAX_SIZE extends DataConstraintException("OWNER_FIRSTNAME_MAX_SIZE")

object OWNER_FIRSTNAME_IS_REQUIRED extends BadRequestException("OWNER_FIRSTNAME_IS_REQUIRED")

object OWNER_LASTNAME_MIN_SIZE extends DataConstraintException("OWNER_LASTNAME_MIN_SIZE")

object OWNER_LASTNAME_MAX_SIZE extends DataConstraintException("OWNER_LASTNAME_MAX_SIZE")

object OWNER_LASTNAME_IS_REQUIRED extends BadRequestException("OWNER_LASTNAME_IS_REQUIRED")

object OWNER_ADDRESS_MIN_SIZE extends DataConstraintException("OWNER_ADDRESS_MIN_SIZE")

object OWNER_ADDRESS_MAX_SIZE extends DataConstraintException("OWNER_ADDRESS_MAX_SIZE")

object OWNER_ADDRESS_IS_REQUIRED extends BadRequestException("OWNER_ADDRESS_IS_REQUIRED")

object OWNER_CITY_MIN_SIZE extends DataConstraintException("OWNER_CITY_MIN_SIZE")

object OWNER_CITY_MAX_SIZE extends DataConstraintException("OWNER_CITY_MAX_SIZE")

object OWNER_CITY_IS_REQUIRED extends BadRequestException("OWNER_CITY_IS_REQUIRED")

object OWNER_TELEPHONE_MIN_SIZE extends DataConstraintException("OWNER_TELEPHONE_MIN_SIZE")

object OWNER_TELEPHONE_MAX_SIZE extends DataConstraintException("OWNER_TELEPHONE_MAX_SIZE")

object OWNER_TELEPHONE_IS_REQUIRED extends BadRequestException("OWNER_TELEPHONE_IS_REQUIRED")

object OWNER_DOESNT_EXIST extends DataConstraintException("OWNER_DOESNT_EXIST")

object OWNER_ID_IS_NOT_UNIQUE extends DataConstraintException("OWNER_ID_IS_NOT_UNIQUE")

class owners(tag: Tag) extends Table[owner](tag, "owner") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def firstName = column[String]("firstName")
  def lastName = column[String]("lastName")
  def address = column[String]("address")
  def city = column[String]("city")
  def telephone = column[String]("telephone")

  val create = owner.apply _
  def * = (id, firstName, lastName, address, city, telephone) <> (create.tupled, owner.unapply)
  def ? = (id.?, firstName.?, lastName.?, address.?, city.?, telephone.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))


}

class ownerDao extends GenericSlickDao[owner] {

  def save(entity: owner)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[owners]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[owner])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[owners]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: owner)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[owners]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[owner] = {
    logger.trace(s".findAll()")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): owner = {
    logger.trace(s".getById(id: $id)")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw OWNER_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[owner] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByFirstName(firstName: String)(implicit session: SlickSession): List[owner] = {
    logger.trace(s".findByFirstName(firstName: $firstName)")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]
    query = query.filter(_.firstName === firstName)

    query.list
  }

  def findByLastName(lastName: String)(implicit session: SlickSession): List[owner] = {
    logger.trace(s".findByLastName(lastName: $lastName)")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]
    query = query.filter(_.lastName === lastName)

    query.list
  }

  def findByAddress(address: String)(implicit session: SlickSession): List[owner] = {
    logger.trace(s".findByAddress(address: $address)")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]
    query = query.filter(_.address === address)

    query.list
  }

  def findByCity(city: String)(implicit session: SlickSession): List[owner] = {
    logger.trace(s".findByCity(city: $city)")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]
    query = query.filter(_.city === city)

    query.list
  }

  def findByTelephone(telephone: String)(implicit session: SlickSession): List[owner] = {
    logger.trace(s".findByTelephone(telephone: $telephone)")

    var query: Query[owners, owners#TableElementType, Seq] = TableQuery[owners]
    query = query.filter(_.telephone === telephone)

    query.list
  }

}
