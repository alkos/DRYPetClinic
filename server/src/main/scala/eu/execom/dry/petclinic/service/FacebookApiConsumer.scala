package eu.execom.dry.petclinic.service

import com.restfb.DefaultFacebookClient
import com.restfb.types.User
import eu.execom.dry.petclinic.util.Logging
import org.joda.time.DateTime

import scala.util.Try

class FacebookApiConsumer extends Logging {

  def fetchFacebookUser(facebookAccessToken: String): Try[User] =  Try {
    val facebookClient = new DefaultFacebookClient(facebookAccessToken)
    val user = facebookClient.fetchObject("me", classOf[User])
    logger.trace(s"User: first name: ${user.getFirstName}, last name: ${user.getLastName}, facebookId: ${user.getId}, email: ${user.getEmail}, birthday: ${user.getBirthdayAsDate}, gender: ${user.getGender}, age: ${user.getBirthday}")

    user
  }

}
