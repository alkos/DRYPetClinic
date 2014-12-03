package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util._

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.util.Try

class SecuredService(val userDao: UserDao, val passwordEncoder: PasswordEncoder, val mailSender: MailSender, val appEmail: String, val appName: String, val appUrl: String) {

  def signUp(username: String, passwordHash: String)(implicit session: SlickSession): Try[User] = Try {
    require(username != null, passwordHash != null)

    val user = new User()
    user.username = username
    user.passwordHash = passwordEncoder.encode(passwordHash, username)
    //TODO add other properties
    userDao.save(user)
    user
  }

  def signIn(username: String, passwordHash: String)(implicit session: SlickSession): Try[User] = Try {
    require(username != null, passwordHash != null)

    val user = userDao.findByUsername(username).getOrElse(throw CREDENTIALS_ARE_INVALID)
    val encodedPassword = passwordEncoder.encode(passwordHash, username)
    if (!encodedPassword.equals(user.passwordHash)) {
      throw CREDENTIALS_ARE_INVALID
    }
    user.authenticationCode = Some(encodedPassword)
    userDao.update(user)
    user
  }

  def signOut(authenticationCode: String)(implicit session: SlickSession): Try[User] = Try {
    require(authenticationCode != null)
    val user = userDao.findByAuthenticationCode(Some(authenticationCode)).getOrElse(throw CREDENTIALS_ARE_INVALID)
    user.authenticationCode = None
    userDao.update(user)
    user
  }

  def authenticate(authenticationCode: String)(implicit session: SlickSession): Try[User] = Try {
    require(authenticationCode != null)
    val user = userDao.findByAuthenticationCode(Some(authenticationCode)).getOrElse(throw CREDENTIALS_ARE_INVALID)
    user
  }

}
