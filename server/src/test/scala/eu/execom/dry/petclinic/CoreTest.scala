package eu.execom.dry.petclinic

import java.sql.{Blob, Date, Timestamp}
import java.util.UUID

import eu.execom.dry.petclinic.AppTestConfiguration._
import eu.execom.dry.petclinic.persistence._
import eu.execom.fabut._
import junit.framework.AssertionFailedError
import org.joda.time.DateTime
import org.junit.Assert._
import org.junit.Assert._

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}

abstract class CoreTest extends AbstractFabutTest with IFabutRepositoryTest {


  var assertMails = new ListBuffer[AssertMail]
  implicit var slickSession: SlickSession = null
  override def fabutBeforeTest() = {

    //opening test transaction
    slickSession = slickDb.createSession()
    slickSession.conn.setAutoCommit(false)
    Fabut.beforeTest(this)

    //cleaning wiser
    assertMails.clear()
    wiser.getMessages.clear()
  }

  override def fabutAfterTest() = {

    try {
      Fabut.afterTest()
    } finally {

      // simulating transaction
      slickSession.rollback()
      slickSession.close()
      // asserting messages
      assertWiserMessages()
    }
  }

  override def getEntityTypes: java.util.List[Class[_]] = classOf[User] :: classOf[Role] :: classOf[Permission] :: classOf[owner] :: classOf[PetType] :: classOf[Pet] :: classOf[PetHistory] :: classOf[VetSpecialty] :: classOf[Vet] :: classOf[VetSpecialties] :: classOf[Visit] :: Nil
  override def getComplexTypes: java.util.List[Class[_]] = Nil
  override def getIgnoredTypes: java.util.List[Class[_]] = Nil


  override def findById(entityClass: Class[_], id: AnyRef): AnyRef =
    if (entityClass == classOf[User]) userDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[Role]) roleDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[Permission]) permissionDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[owner]) ownerDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[PetType]) petTypeDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[Pet]) petDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[PetHistory]) petHistoryDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[VetSpecialty]) vetSpecialtyDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[Vet]) vetDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[VetSpecialties]) vetSpecialtiesDao.findById(id.asInstanceOf[Int]).orNull
    else if (entityClass == classOf[Visit]) visitDao.findById(id.asInstanceOf[Int]).orNull
    else throw new IllegalStateException("Unsupported entity class " + entityClass)


  override def findAll(entityClass: Class[_]): java.util.List[_] =
    if (entityClass == classOf[User]) userDao.findAll
    else if (entityClass == classOf[Role]) roleDao.findAll
    else if (entityClass == classOf[Permission]) permissionDao.findAll
    else if (entityClass == classOf[owner]) ownerDao.findAll
    else if (entityClass == classOf[PetType]) petTypeDao.findAll
    else if (entityClass == classOf[Pet]) petDao.findAll
    else if (entityClass == classOf[PetHistory]) petHistoryDao.findAll
    else if (entityClass == classOf[VetSpecialty]) vetSpecialtyDao.findAll
    else if (entityClass == classOf[Vet]) vetDao.findAll
    else if (entityClass == classOf[VetSpecialties]) vetSpecialtiesDao.findAll
    else if (entityClass == classOf[Visit]) visitDao.findAll
    else throw new IllegalStateException("Unsupported entity class " + entityClass)


  override def customAssertEquals(expected: Any, actual: Any) = (expected, actual) match {
    case (e: Timestamp, a: Timestamp) => assertTrue(Math.abs(e.getTime - a.getTime) < 1000)
    case (e: DateTime, a: DateTime) => assertTrue(Math.abs(e.getMillis - a.getMillis) < 1000)
    case (e: Blob, a: Blob) => customAssertEquals(e.length(), a.length())

    //FIXME (nolah) FABUT errro bypass
    case (e: Blob, a: Array[Byte]) => assertArrayEquals(e.getBytes(1, e.length().toInt), a)
    case (e: Array[Byte], a: Blob) => assertArrayEquals(e, a.getBytes(1, a.length().toInt))
    case (e: String, a: Enum) => assertEquals(e, a.name)
    case (e: Enum, a: String) => assertEquals(e.name, a)
    case (e: DateTime, a: Date) => assertTrue(Math.abs(e.getMillis - a.getTime) < 1000)
    case (e: Date, a: DateTime) => assertTrue(Math.abs(e.getTime - a.getMillis) < 1000)
    case (Some(e), Some(a)) => customAssertEquals(e, a)
    case _ => assertEquals(expected, actual)
  }

  /**
   * Asserts mails sent by the test.
   */
  def assertWiserMessages() = {

    val wiserMails = wiser.getMessages.map(x => new AssertMail(x.getMimeMessage.getAllRecipients()(0).toString, x.getMimeMessage.getSubject))

    for (wiserMail <- wiserMails) {
      for (assertMail <- assertMails) {
        if (!wiserMail.asserted && !assertMail.asserted && wiserMail.to.equals(assertMail.to) && wiserMail.subject.equals(assertMail.subject)) {
          wiserMail.asserted = true
          assertMail.asserted = true
        }
      }
    }
    val sb = new StringBuffer
    for (wiserMail <- wiserMails) {
      if (!wiserMail.asserted) {
        sb.append(s"Mail recieved but not asserted in test: sent to: ${wiserMail.to} with subject ${wiserMail.subject} \n")
      }
    }

    for (assertMail <- assertMails) {
      if (!assertMail.asserted) {
        sb.append(s"Mail never recieved in test: to ${assertMail.to} with subject ${assertMail.subject} \n")
      }
    }

    if (sb.length() > 0) {
      sb.insert(0, "\n")
      throw new AssertionFailedError(sb.toString)
    }
  }

  def assertMail(to: String, subject: String) {
    assertMails = assertMails :+ new AssertMail(to, subject)
  }
}

case class AssertMail(to: String, subject: String, var asserted: Boolean = false) 
