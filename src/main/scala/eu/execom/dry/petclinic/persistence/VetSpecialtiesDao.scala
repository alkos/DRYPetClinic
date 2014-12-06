package eu.execom.dry.petclinic.persistence

import java.sql.Blob
import java.sql.Timestamp
import java.sql.Date

import eu.execom.dry.petclinic.api._
import eu.execom.dry.petclinic.util._
import org.joda.time._

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

case class VetSpecialties(private var _id: Int, private var _vetId: Int, private var _specialtyId: Int) {

  private var id_persisted: Int = id
  def idPersisted: Int = id_persisted

  def id: Int = _id
  def id_=(newId: Int)(implicit session: SlickSession): Any = if (newId != id) {

    _id = newId
  }

  private var vetId_persisted: Int = vetId
  def vetIdPersisted: Int = vetId_persisted

  def vetId: Int = _vetId
  def vetId_=(newVetId: Int)(implicit session: SlickSession): Any = if (newVetId != vetId) {

    _vetId = newVetId
  }

  private var specialtyId_persisted: Int = specialtyId
  def specialtyIdPersisted: Int = specialtyId_persisted

  def specialtyId: Int = _specialtyId
  def specialtyId_=(newSpecialtyId: Int)(implicit session: SlickSession): Any = if (newSpecialtyId != specialtyId) {

    _specialtyId = newSpecialtyId
  }
  def vet(implicit session: SlickSession): Vet = TableQuery[Vets].filter(_.id === vetId).first
  def vet_=(vet: Vet)(implicit session: SlickSession) = vetId = vet.id

  def specialty(implicit session: SlickSession): VetSpecialty = TableQuery[VetSpecialtys].filter(_.id === specialtyId).first
  def specialty_=(specialty: VetSpecialty)(implicit session: SlickSession) = specialtyId = specialty.id

  def this(entity: VetSpecialties) = this(entity._id, entity._vetId, entity._specialtyId)

  def this() = this(0, 0, 0)

  def this(vet: Vet, specialty: VetSpecialty)(implicit session: SlickSession) = {
    this()
    this.vet_=(vet)(session)
    this.specialty_=(specialty)(session)
  }

  def persisted() = {
    id_persisted = id
    vetId_persisted = vetId
    specialtyId_persisted = specialtyId
  }
}

object VetSpecialties {
  val ID: String = "_id"
  val VETID: String = "_vetId"
  val SPECIALTYID: String = "_specialtyId"
}

object VETSPECIALTIES_DOESNT_EXIST extends DataConstraintException("VETSPECIALTIES_DOESNT_EXIST")

object VETSPECIALTIES_ID_IS_NOT_UNIQUE extends DataConstraintException("VETSPECIALTIES_ID_IS_NOT_UNIQUE")

class VetSpecialtiess(tag: Tag) extends Table[VetSpecialties](tag, "VetSpecialties") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def vetId = column[Int]("vetId")
  def specialtyId = column[Int]("specialtyId")

  val create = VetSpecialties.apply _
  def * = (id, vetId, specialtyId) <> (create.tupled, VetSpecialties.unapply)
  def ? = (id.?, vetId.?, specialtyId.?).shaped.<>({r=>import r._; _1.map(_=> create.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

  def vet= foreignKey("VETSPECIALTIES_VET_FK", vetId, TableQuery[Vets])(_.id)
  def specialty= foreignKey("VETSPECIALTIES_SPECIALTY_FK", specialtyId, TableQuery[VetSpecialtys])(_.id)
}

class VetSpecialtiesDao extends GenericSlickDao[VetSpecialties] {

  def save(entity: VetSpecialties)(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entity: $entity)")
    val tableQuery = TableQuery[VetSpecialtiess]
    val id = tableQuery returning tableQuery.map(_.id) += entity
    entity.id = id
    entity.persisted()
  }

  def save(entities: List[VetSpecialties])(implicit session: SlickSession): Unit = {
    logger.trace(s".save(entities: $entities)")
    val tableQuery = TableQuery[VetSpecialtiess]
    val ids = tableQuery returning tableQuery.map(_.id) ++= entities
    ids.zip(entities).foreach(idWithEntity => {
      val id = idWithEntity._1
      val entity = idWithEntity._2
      entity.id = id
      entity.persisted()
    })

  }

  def update(entity: VetSpecialties)(implicit session: SlickSession): Unit = {
    logger.trace(s".update(entity: $entity)")
    val tableQuery = TableQuery[VetSpecialtiess]
    tableQuery.filter(_.id === entity.id).update(entity)
    entity.persisted()
  }

  def findAll()(implicit session: SlickSession): List[VetSpecialties] = {
    logger.trace(s".findAll()")

    var query: Query[VetSpecialtiess, VetSpecialtiess#TableElementType, Seq] = TableQuery[VetSpecialtiess]

    query.list
  }

  def countAll()(implicit session: SlickSession): Int = {
    logger.trace(s".countAll()")

    var query: Query[VetSpecialtiess, VetSpecialtiess#TableElementType, Seq] = TableQuery[VetSpecialtiess]

    query.length.run
  }

  def getById(id: Int)(implicit session: SlickSession): VetSpecialties = {
    logger.trace(s".getById(id: $id)")

    var query: Query[VetSpecialtiess, VetSpecialtiess#TableElementType, Seq] = TableQuery[VetSpecialtiess]
    query = query.filter(_.id === id)

    query.firstOption.getOrElse(throw VETSPECIALTIES_DOESNT_EXIST)
  }

  def deleteById(id: Int)(implicit session: SlickSession): Boolean = {
    logger.trace(s".deleteById(id: $id)")

    var query: Query[VetSpecialtiess, VetSpecialtiess#TableElementType, Seq] = TableQuery[VetSpecialtiess]
    query = query.filter(_.id === id)

    query.delete != 0
  }

  def findById(id: Int)(implicit session: SlickSession): Option[VetSpecialties] = {
    logger.trace(s".findById(id: $id)")

    var query: Query[VetSpecialtiess, VetSpecialtiess#TableElementType, Seq] = TableQuery[VetSpecialtiess]
    query = query.filter(_.id === id)

    query.firstOption
  }

  def findByVetId(vetId: Int)(implicit session: SlickSession): List[VetSpecialties] = {
    logger.trace(s".findByVetId(vetId: $vetId)")

    var query: Query[VetSpecialtiess, VetSpecialtiess#TableElementType, Seq] = TableQuery[VetSpecialtiess]
    query = query.filter(_.vetId === vetId)

    query.list
  }

  def findBySpecialtyId(specialtyId: Int)(implicit session: SlickSession): List[VetSpecialties] = {
    logger.trace(s".findBySpecialtyId(specialtyId: $specialtyId)")

    var query: Query[VetSpecialtiess, VetSpecialtiess#TableElementType, Seq] = TableQuery[VetSpecialtiess]
    query = query.filter(_.specialtyId === specialtyId)

    query.list
  }

}
