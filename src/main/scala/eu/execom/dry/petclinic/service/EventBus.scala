package eu.execom.dry.petclinic.service

import com.hazelcast.core._
import eu.execom.dry.petclinic.persistence._

case class EventBus(hazelcast: HazelcastInstance) {

  def userPasswordHashUpdateEventTopic(id: Int): ITopic[UserPasswordHashUpdateEvent] = {
    hazelcast.getTopic[UserPasswordHashUpdateEvent](s"user/$id/passwordHash/update")
  }

  def subscribeOnUserPasswordHashUpdateEvent(id: Int, callback: (UserPasswordHashUpdateEvent) => Unit): () => Unit = {
    val topic = userPasswordHashUpdateEventTopic(id)

    val registrationId = topic.addMessageListener(new MessageListener[UserPasswordHashUpdateEvent]() {

      override def onMessage(message: Message[UserPasswordHashUpdateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(event:UserPasswordHashUpdateEvent):Unit = {
    val topic = userPasswordHashUpdateEventTopic(event.id)

    topic.publish(event)
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

  def publish(event:UserCreateEvent):Unit = {
    val topic = userCreateEventTopic()

    topic.publish(event)
  }

  def userDeleteEventTopic(id: Int): ITopic[UserDeleteEvent] = {
    hazelcast.getTopic[UserDeleteEvent](s"user/$id/delete")
  }

  def subscribeOnUserDeleteEvent(id: Int, callback: (UserDeleteEvent) => Unit): () => Unit = {
    val topic = userDeleteEventTopic(id)

    val registrationId = topic.addMessageListener(new MessageListener[UserDeleteEvent]() {

      override def onMessage(message: Message[UserDeleteEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(event:UserDeleteEvent):Unit = {
    val topic = userDeleteEventTopic(event.id)

    topic.publish(event)
  }

  def userUpdateEventTopic(id: Int): ITopic[UserUpdateEvent] = {
    hazelcast.getTopic[UserUpdateEvent](s"user/$id/update")
  }

  def subscribeOnUserUpdateEvent(id: Int, callback: (UserUpdateEvent) => Unit): () => Unit = {
    val topic = userUpdateEventTopic(id)

    val registrationId = topic.addMessageListener(new MessageListener[UserUpdateEvent]() {

      override def onMessage(message: Message[UserUpdateEvent]): Unit = callback(message.getMessageObject)
    })

    () => topic.removeMessageListener(registrationId) // unsubscribe method
  }

  def publish(event:UserUpdateEvent):Unit = {
    val topic = userUpdateEventTopic(event.id)

    topic.publish(event)
  }
}

trait ServiceEvent

case class UserPasswordHashUpdateEvent(id: Int, passwordHash: (String, String)) extends ServiceEvent

case class UserCreateEvent(id: Int, authenticationCode: Option[String], role: UserRole, username: String, passwordHash: String) extends ServiceEvent

case class UserDeleteEvent(id: Int, authenticationCode: Option[String], role: UserRole, username: String, passwordHash: String) extends ServiceEvent

case class UserUpdateEvent(id: Int, authenticationCode: Option[(Option[String], Option[String])], role: Option[(UserRole, UserRole)], username: Option[(String, String)], passwordHash: Option[(String, String)]) extends ServiceEvent {

  val changes: Map[String, ((Any,Any))] = Map(User.AUTHENTICATIONCODE -> authenticationCode, User.ROLE -> role, User.USERNAME -> username, User.PASSWORDHASH -> passwordHash).filter(_._2.isDefined).
    map(change => change._1 -> change._2.get)
}
