import akka.actor.{ActorRef, ActorSystem, Props}
import utils.KafkaConsumer

object MainApp extends App {
  implicit val system = ActorSystem("MessagingConsumerSystem")
  KafkaConsumer.startConsumers(system)
}
