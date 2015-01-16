package eu.execom.dry.petclinic

import java.util.UUID

import eu.execom.dry.petclinic.AppTestConfiguration._
import eu.execom.dry.petclinic.persistence._
import eu.execom.fabut._
import org.joda.time.DateTime
import org.junit.{After, Before}
import org.junit.Assert._

import scala.reflect.runtime.universe.{Type, typeOf}
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

abstract class CoreTest extends FabutRepository with FabutMail {


  implicit var slickSession: SlickSession = null
  @Before
  override def before() = {

    //opening test transaction
    slickSession = slickDb.createSession()
    slickSession.conn.setAutoCommit(false)
    beforeTest()

  }

  @After
  override def after() = {

    try {
      afterTest()
    } finally {

      // simulating transaction
      slickSession.rollback()
      slickSession.close()
    }
  }

  override def entityTypes(): List[Type] = typeOf[User] :: typeOf[UserSession] :: typeOf[Role] :: typeOf[Permission] :: typeOf[owner] :: typeOf[PetType] :: typeOf[Pet] :: typeOf[PetHistory] :: typeOf[VetSpecialty] :: typeOf[Vet] :: typeOf[VetSpecialties] :: typeOf[Visit] :: Nil
  override def complexTypes(): List[Type] = Nil
  override def ignoredTypes(): List[Type] = Nil


  override def findById(entityClass: Type, id: Any): Any =
    if (entityClass == typeOf[User]) userDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[UserSession]) userSessionDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[Role]) roleDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[Permission]) permissionDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[owner]) ownerDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[PetType]) petTypeDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[Pet]) petDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[PetHistory]) petHistoryDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[VetSpecialty]) vetSpecialtyDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[Vet]) vetDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[VetSpecialties]) vetSpecialtiesDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == typeOf[Visit]) visitDao.findById(id.asInstanceOf[Int]).orNull
    else throw new IllegalStateException("Unsupported entity class " + entityClass)


  override def findAll(entityClass: Type): List[_] =
    if (entityClass == typeOf[User]) userDao.findAll
    else if (entityClass == typeOf[UserSession]) userSessionDao.findAll
    else if (entityClass == typeOf[Role]) roleDao.findAll
    else if (entityClass == typeOf[Permission]) permissionDao.findAll
    else if (entityClass == typeOf[owner]) ownerDao.findAll
    else if (entityClass == typeOf[PetType]) petTypeDao.findAll
    else if (entityClass == typeOf[Pet]) petDao.findAll
    else if (entityClass == typeOf[PetHistory]) petHistoryDao.findAll
    else if (entityClass == typeOf[VetSpecialty]) vetSpecialtyDao.findAll
    else if (entityClass == typeOf[Vet]) vetDao.findAll
    else if (entityClass == typeOf[VetSpecialties]) vetSpecialtiesDao.findAll
    else if (entityClass == typeOf[Visit]) visitDao.findAll
    else throw new IllegalStateException("Unsupported entity class " + entityClass)


  override def customAssertEquals(expected: Any, actual: Any) = (expected, actual) match {
    case (e: DateTime, a: DateTime) => assertTrue(Math.abs(e.getMillis - a.getMillis) < 1000)
    case (e: Array[Byte], a: Array[Byte]) => assertArrayEquals(a,e)
    case (Some(e), Some(a)) => customAssertEquals(e, a)
    case _ => assertEquals(expected, actual)
  }

}
