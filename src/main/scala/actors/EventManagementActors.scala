package actors

import akka.actor.{Actor, ActorRef}
import constants.EventManagementMsgReceivers
import models.KafkaMessageFormat

import java.io.{BufferedWriter, File, FileWriter}

class EventManagementFileWriterActor() extends Actor {
  private val baseDir = "src/main/scala/messages/eventManagement"

  def ensureFileExists(fileName: String): File = {
    val file = new File(fileName)
    file.getParentFile.mkdirs() // Ensure directories exist
    if (!file.exists()) file.createNewFile()
    file
  }

  def receive: Receive = {
    case (fileName: String, messageType: String, message: String) =>
      val file = ensureFileExists(fileName) // Dynamically ensure file exists
      val bw = new BufferedWriter(new FileWriter(file, true))
      try {
        bw.write(s"$messageType :: $message")
        bw.newLine()
        bw.flush()
      } finally {
        bw.close() // Ensure resources are released
      }
  }
}

class CateringMessageListener(fileWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Catering Message Listener consumes the message")
      fileWriterActor ! ("src/main/scala/messages/eventManagement/catering.txt", msg.messageType, msg.message)
  }
}

class EntertainmentMessageListener(fileWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Entertainment Message Listener consumes the message")
      fileWriterActor ! ("src/main/scala/messages/eventManagement/entertainment.txt", msg.messageType, msg.message)
  }
}

class DecorationMessageListener(fileWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Decoration Message Listener consumes the message")
      fileWriterActor ! ("src/main/scala/messages/eventManagement/decoration.txt", msg.messageType, msg.message)
  }
}

class LogisticsMessageListener(fileWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Logistics Message Listener consumes the message")
      fileWriterActor ! ("src/main/scala/messages/eventManagement/logistics.txt", msg.messageType, msg.message)
  }
}

class ManagerMessageListener(fileWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Manager Message Listener consumes the message")
      fileWriterActor ! ("src/main/scala/messages/eventManagement/manager.txt", msg.messageType, msg.message)
  }
}

class EventManagementListener(cateringMessageListener: ActorRef,
                              entertainmentMessageListener: ActorRef,
                              decorationMessageListener: ActorRef,
                              logisticsMessageListener: ActorRef,
                              managerMessageListener: ActorRef
                             )extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat => msg.receiver match {
      case EventManagementMsgReceivers.CATERING =>
        cateringMessageListener ! msg
      case EventManagementMsgReceivers.ENTERTAINMENT =>
        entertainmentMessageListener ! msg
      case EventManagementMsgReceivers.DECORATIONS =>
        decorationMessageListener ! msg
      case EventManagementMsgReceivers.LOGISTICS =>
        logisticsMessageListener ! msg
      case EventManagementMsgReceivers.MANAGER =>
        managerMessageListener ! msg
    }
  }
}
