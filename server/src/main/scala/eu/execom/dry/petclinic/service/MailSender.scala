package eu.execom.dry.petclinic.service

import eu.execom.dry.petclinic.util.Logging
import org.apache.commons.mail.HtmlEmail

import scala.util.Try

class MailSender(val smtpUrl: String, val smtpPort: Int, val smtpUserName: String, val smtpPassword: String, val smtpSslOnConnect: Boolean) extends Logging {

  def sendEmail(toAddress: String, toName: String, fromAddress: String, fromName: String, subject: String, content: String): Unit =  {

    val email = new HtmlEmail()
    email.setHostName(smtpUrl)
    email.setSmtpPort(smtpPort)
    if (smtpUserName.nonEmpty && smtpPassword.nonEmpty)  email.setAuthentication(smtpUserName, smtpPassword)
    email.setSSLOnConnect(smtpSslOnConnect)
    email.addTo(toAddress, toName)
    email.setFrom(fromAddress, "")
    email.setSubject(subject)
    email.setCharset("UTF-8")
    email.setHtmlMsg(content)

    email.send()
    logger.trace("Email successfully sent")
  }

}
