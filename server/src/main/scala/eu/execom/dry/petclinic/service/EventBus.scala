package eu.execom.dry.petclinic.service

import com.hazelcast.core._
import eu.execom.dry.petclinic.persistence._

case class EventBus(hazelcast: HazelcastInstance) {

  private val publishOnReceive = new ThreadLocal[Boolean]
  private val delayedEvents = new ThreadLocal[List[ServiceEvent]]
  delayedEvents.set(Nil)

  def startSession(): Unit = publishOnReceive.set(true)
  def startTransaction(): Unit = publishOnReceive.set(false)

  private def delayEvent(event: ServiceEvent): Unit = delayedEvents.set(event :: delayedEvents.get)

  def publishPending(): Unit = {
    publishOnReceive.set(true)

    delayedEvents.get().foreach({
      case event: UserPasswordHashUpdateEvent => publish(event)
      case event: UserEmailUpdateEvent => publish(event)
      case event: UserCreateEvent => publish(event)
      case event: UserDeleteEvent => publish(event)
      case event: UserUpdateEvent => publish(event)
    })

    delayedEvents.set(Nil)
  }

  def rollback(): Unit = delayedEvents.set(Nil)

  def userPasswordHashUpdateEventTopic(): ITopic[UserPasswordHashUpdateEvent] = {
    hazelcast.getTopic[UserPasswordHashUpdateEvent](s"user/update/passwordHash")
  }

  def subscribeOnUserPasswordHashUpdateEvent(callback: (UserPasswordHashUpdateEvent) => Unit): () => Unit = {
    val topic = userPasswordHashUpdateEventTopic()

    val registrationId = topic.addMessageListener(new MessageListener[UserPasswordHashUpdateEvent]() {

      override def onMessage(message: Message[UserPasswordHashUpdateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(event: UserPasswordHashUpdateEvent): Unit = if (publishOnReceive.get) {
    val topic = userPasswordHashUpdateEventTopic()

    topic.publish(event)
  } else {
    delayEvent(event)
  }


  def userPasswordHashUpdateEventTopic(roleId: Int): ITopic[UserPasswordHashUpdateEvent] = {
    hazelcast.getTopic[UserPasswordHashUpdateEvent](s"user/update/passwordHash?roleId=$roleId")
  }

  def subscribeOnUserPasswordHashUpdateEvent(roleId: Int, callback: (UserPasswordHashUpdateEvent) => Unit): () => Unit = {
    val topic = userPasswordHashUpdateEventTopic(roleId)

    val registrationId = topic.addMessageListener(new MessageListener[UserPasswordHashUpdateEvent]() {

      override def onMessage(message: Message[UserPasswordHashUpdateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(roleId: Int, event: UserPasswordHashUpdateEvent): Unit = if (publishOnReceive.get) {
    val topic = userPasswordHashUpdateEventTopic(roleId)

    topic.publish(event)
  } else {
    delayEvent(event)
  }

  def userEmailUpdateEventTopic(): ITopic[UserEmailUpdateEvent] = {
    hazelcast.getTopic[UserEmailUpdateEvent](s"user/update/email")
  }

  def subscribeOnUserEmailUpdateEvent(callback: (UserEmailUpdateEvent) => Unit): () => Unit = {
    val topic = userEmailUpdateEventTopic()

    val registrationId = topic.addMessageListener(new MessageListener[UserEmailUpdateEvent]() {

      override def onMessage(message: Message[UserEmailUpdateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(event: UserEmailUpdateEvent): Unit = if (publishOnReceive.get) {
    val topic = userEmailUpdateEventTopic()

    topic.publish(event)
  } else {
    delayEvent(event)
  }

  def userCreateEventTopic(): ITopic[UserCreateEvent] = {
    hazelcast.getTopic[UserCreateEvent](s"user/create")
  }

  def subscribeOnUserCreateEvent(callback: (UserCreateEvent) => Unit): () => Unit = {
    val topic = userCreateEventTopic()

    val registrationId = topic.addMessageListener(new MessageListener[UserCreateEvent]() {

      override def onMessage(message: Message[UserCreateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(event: UserCreateEvent): Unit = if (publishOnReceive.get) {
    val topic = userCreateEventTopic()

    topic.publish(event)
  } else {
    delayEvent(event)
  }


  def userCreateEventTopic(id: Int): ITopic[UserCreateEvent] = {
    hazelcast.getTopic[UserCreateEvent](s"user/create?id=$id")
  }

  def subscribeOnUserCreateEvent(id: Int, callback: (UserCreateEvent) => Unit): () => Unit = {
    val topic = userCreateEventTopic(id)

    val registrationId = topic.addMessageListener(new MessageListener[UserCreateEvent]() {

      override def onMessage(message: Message[UserCreateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(id: Int, event: UserCreateEvent): Unit = if (publishOnReceive.get) {
    val topic = userCreateEventTopic(id)

    topic.publish(event)
  } else {
    delayEvent(event)
  }

  def userDeleteEventTopic(): ITopic[UserDeleteEvent] = {
    hazelcast.getTopic[UserDeleteEvent](s"user/delete")
  }

  def subscribeOnUserDeleteEvent(callback: (UserDeleteEvent) => Unit): () => Unit = {
    val topic = userDeleteEventTopic()

    val registrationId = topic.addMessageListener(new MessageListener[UserDeleteEvent]() {

      override def onMessage(message: Message[UserDeleteEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(event: UserDeleteEvent): Unit = if (publishOnReceive.get) {
    val topic = userDeleteEventTopic()

    topic.publish(event)
  } else {
    delayEvent(event)
  }

  def userUpdateEventTopic(): ITopic[UserUpdateEvent] = {
    hazelcast.getTopic[UserUpdateEvent](s"user/update")
  }

  def subscribeOnUserUpdateEvent(callback: (UserUpdateEvent) => Unit): () => Unit = {
    val topic = userUpdateEventTopic()

    val registrationId = topic.addMessageListener(new MessageListener[UserUpdateEvent]() {

      override def onMessage(message: Message[UserUpdateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(event: UserUpdateEvent): Unit = if (publishOnReceive.get) {
    val topic = userUpdateEventTopic()

    topic.publish(event)
  } else {
    delayEvent(event)
  }


  def userUpdateEventTopic(email: String): ITopic[UserUpdateEvent] = {
    hazelcast.getTopic[UserUpdateEvent](s"user/update?email=$email")
  }

  def subscribeOnUserUpdateEvent(email: String, callback: (UserUpdateEvent) => Unit): () => Unit = {
    val topic = userUpdateEventTopic(email)

    val registrationId = topic.addMessageListener(new MessageListener[UserUpdateEvent]() {

      override def onMessage(message: Message[UserUpdateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(email: String, event: UserUpdateEvent): Unit = if (publishOnReceive.get) {
    val topic = userUpdateEventTopic(email)

    topic.publish(event)
  } else {
    delayEvent(event)
  }
}

trait ServiceEvent

case class UserPasswordHashUpdateEvent(id: Int, passwordHash: (Option[String], Option[String])) extends ServiceEvent

case class UserEmailUpdateEvent(id: Int, email: (String, String)) extends ServiceEvent

case class UserCreateEvent(id: Int, roleId: Int, email: String, passwordHash: Option[String], facebookId: Option[String], googleId: Option[String]) extends ServiceEvent

case class UserDeleteEvent(id: Int, roleId: Int, email: String, passwordHash: Option[String], facebookId: Option[String], googleId: Option[String]) extends ServiceEvent

case class UserUpdateEvent(id: Int, roleId: Option[(Int, Int)], email: Option[(String, String)], passwordHash: Option[(Option[String], Option[String])], facebookId: Option[(Option[String], Option[String])], googleId: Option[(Option[String], Option[String])]) extends ServiceEvent {

  val changes: Map[String, ((Any,Any))] = Map(User.ROLEID -> roleId, User.EMAIL -> email, User.PASSWORDHASH -> passwordHash, User.FACEBOOKID -> facebookId, User.GOOGLEID -> googleId).filter(_._2.isDefined).
    map(change => change._1 -> change._2.get)
}
