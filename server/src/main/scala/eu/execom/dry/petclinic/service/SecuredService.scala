package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.persistence._
import eu.execom.dry.petclinic.util._
import org.joda.time.DateTime

import scala.slick.jdbc.JdbcBackend.{Session => SlickSession}
import scala.util.Try

class SecuredService(val userDao: UserDao, val clientDao: ClientDao, val passwordEncoder: PasswordEncoder, val mailSender: MailSender, val appEmail: String, val appName: String, val appUrl: String) {

  def signUp(username: String, passwordHash: String)(implicit session: SlickSession): Try[(User, Client)] = Try {
    require(username != null, passwordHash != null)

    val user = new User()
    user.username = username
    user.passwordHash = passwordEncoder.encode(passwordHash, username)
    //TODO add other properties
    userDao.save(user)

    val client = creteClient(user)

    (user, client)
  }

  def signIn(username: String, passwordHash: String)(implicit session: SlickSession): Try[(User, Client)] = Try {
    require(username != null, passwordHash != null)

    val user = userDao.findByUsername(username).getOrElse(throw CREDENTIALS_ARE_INVALID)
    val encodedPassword = passwordEncoder.encode(passwordHash, username)
    if (!encodedPassword.equals(user.passwordHash)) {
      throw CREDENTIALS_ARE_INVALID
    }

    val client = creteClient(user)

    (user, client)
  }

  def signOut(accessToken: String)(implicit session: SlickSession): Try[User] = Try {
    require(accessToken != null)

    val client = clientDao.findByAccessToken(accessToken).getOrElse(throw CREDENTIALS_ARE_INVALID)
    val user = client.user

    clientDao.deleteById(client.id)

    user
  }

  def authenticate(accessToken: String)(implicit session: SlickSession): Try[(User, Client)] = Try {
    require(accessToken != null)

    val client = clientDao.findByAccessToken(accessToken).getOrElse(throw CREDENTIALS_ARE_INVALID)
    if (client.accessTokenExpires.isAfterNow) throw ACCESS_TOKEN_IS_EXPIRED

    val user = client.user

    (user, client)
  }

  def refreshToken(refreshToken: String)(implicit session: SlickSession): Try[(User, Client)] = Try {
    require(refreshToken != null)

    val client = clientDao.findByRefreshToken(refreshToken).getOrElse(throw REFRESH_TOKEN_IS_INVALID)
    if (client.refreshTokenExpires.isAfterNow) throw REFRESH_TOKEN_IS_EXPIRED

    val user = client.user

    client.accessToken = passwordEncoder.encode(user.passwordHash, new DateTime())
    client.accessTokenExpires = new DateTime().plusDays(1)

    client.refreshToken = passwordEncoder.encode(user.passwordHash, new DateTime())
    client.refreshTokenExpires = new DateTime().plusMonths(1)

    (user, client)
  }

  def creteClient(user: User)(implicit session: SlickSession): Client = {
    val client = new Client(user, None, passwordEncoder.encode(user.passwordHash, new DateTime()), new DateTime().plusDays(1), passwordEncoder.encode(user.passwordHash, new DateTime()),new DateTime().plusMonths(1))
    client.accessToken = user.passwordHash
    clientDao.save(client)

    client
  }

}
