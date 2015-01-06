package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util._
import org.joda.time.DateTime

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.util.Try

class SecuredService(val userDao: UserDao, val userSessionDao: UserSessionDao, val passwordEncoder: PasswordEncoder, val mailSender: MailSender, val appEmail: String, val appName: String, val appUrl: String) {

  def signUp(username: String, password: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(username != null, password != null)

    val user = new User()
    user.username = username
    user.passwordHash = passwordEncoder.encode(password, username)
    //TODO add other properties
    userDao.save(user)

    val userSession = createUserSession(user)

    (user, userSession)
  }

  def signIn(username: String, passwordHash: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(username != null, passwordHash != null)

    val user = userDao.findByUsername(username).getOrElse(throw CREDENTIALS_ARE_INVALID)
    val encodedPassword = passwordEncoder.encode(passwordHash, username)
    if (!encodedPassword.equals(user.passwordHash)) {
      throw CREDENTIALS_ARE_INVALID
    }

    val userSession = createUserSession(user)

    (user, userSession)
  }

  def signOut(accessToken: String)(implicit session: SlickSession): Try[User] = Try {
    require(accessToken != null)

    val userSession = userSessionDao.findByAccessToken(accessToken).getOrElse(throw CREDENTIALS_ARE_INVALID)
    val user = userSession.user

    userSessionDao.deleteById(userSession.id)

    user
  }

  def authenticate(accessToken: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(accessToken != null)

    val userSession = userSessionDao.findByAccessToken(accessToken).getOrElse(throw CREDENTIALS_ARE_INVALID)
    if (userSession.accessTokenExpires.isAfterNow) throw ACCESS_TOKEN_IS_EXPIRED

    val user = userSession.user

    (user, userSession)
  }

  def refreshToken(refreshToken: String)(implicit session: SlickSession): Try[(User, UserSession)] = Try {
    require(refreshToken != null)

    val userSession = userSessionDao.findByRefreshToken(refreshToken).getOrElse(throw REFRESH_TOKEN_IS_INVALID)
    if (userSession.refreshTokenExpires.isAfterNow) throw REFRESH_TOKEN_IS_EXPIRED

    val user = userSession.user

    userSession.accessToken = passwordEncoder.encode(user.passwordHash, new DateTime())
    userSession.accessTokenExpires = new DateTime().plusDays(1)

    userSession.refreshToken = passwordEncoder.encode(user.passwordHash, new DateTime())
    userSession.refreshTokenExpires = new DateTime().plusMonths(1)

    userSessionDao.update(userSession)

    (user, userSession)
  }

  def createUserSession(user: User)(implicit session: SlickSession): UserSession = {
    val userSession = new UserSession(user, passwordEncoder.encode(user.passwordHash, new DateTime()), new DateTime().plusDays(1), passwordEncoder.encode(user.passwordHash, new DateTime()),new DateTime().plusMonths(1))
    userSession.accessToken = user.passwordHash
    userSessionDao.save(userSession)

    userSession
  }

}
