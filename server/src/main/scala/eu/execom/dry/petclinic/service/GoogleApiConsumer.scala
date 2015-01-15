package eu.execom.dry.petclinic.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import eu.execom.dry.petclinic.util.Logging

import scala.util.Try

class GoogleApiConsumer(val googleWebClientId: String, val googleWebClientSecret: String, val netHttpTransport: NetHttpTransport, val jacksonFactory: JacksonFactory) extends Logging {

  def fetchGoogleUser(googleAccessToken: String): Try[GoogleIdToken] =  Try {
    // Upgrade the authorization code into an access and refresh token.
    val tokenRequest = new GoogleAuthorizationCodeTokenRequest(netHttpTransport, jacksonFactory, googleWebClientId, googleWebClientSecret, googleAccessToken, "postmessage")
    val tokenResponse = tokenRequest.execute()

    tokenResponse.parseIdToken()
  }

}
