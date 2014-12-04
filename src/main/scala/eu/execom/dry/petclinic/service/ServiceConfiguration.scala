package eu.execom.dry.petclinic.service

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import eu.execom.dry.petclinic.persistence.SlickPersistenceConfiguration
import eu.execom.dry.petclinic.util._

trait ServiceConfiguration extends SlickPersistenceConfiguration {

  def smtpUrl:String
  def smtpPort:String
  def smtpUserName:String
  def smtpPassword:String
  lazy val mailSender = new MailSender(smtpUrl, smtpPort.toInt, smtpUserName, smtpPassword)

  lazy val passwordEncoder = new PasswordEncoder

  def appEmail:String
  def appName:String
  def appUrl:String
  lazy val securedService: SecuredService = new SecuredService(userDao, passwordEncoder, mailSender, appEmail, appName, appUrl)

  def hazelcastGroupName:String
  def hazelcastGroupPassword:String
  val hazelCastConfig = new Config()
  lazy val hazelcast = Hazelcast.newHazelcastInstance(hazelCastConfig)
  lazy val eventBus = new EventBus(hazelcast)

  //services
  lazy val userService: UserService = new UserService(userDao, eventBus)
  lazy val ownerService: ownerService = new ownerService(ownerDao)
  lazy val petTypeService: PetTypeService = new PetTypeService(petTypeDao)
  lazy val petService: PetService = new PetService(petDao)
  lazy val vetSpecialtyService: VetSpecialtyService = new VetSpecialtyService(vetSpecialtyDao)
  lazy val vetService: VetService = new VetService(vetDao)
  lazy val vetSpecialtiesService: VetSpecialtiesService = new VetSpecialtiesService(vetSpecialtiesDao)
  lazy val visitService: VisitService = new VisitService(visitDao)
}
