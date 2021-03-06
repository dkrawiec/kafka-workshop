package com.virtuslab.kafka.workshops

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer

import scala.jdk.CollectionConverters._

object WorkshopProducer {
  def main(args: Array[String]): Unit = {
    val producer = new KafkaProducer[String, String](
      Map[String, Object](
        "bootstrap.servers" -> "localhost:9092"
      ).asJava,
      new StringSerializer(),
      new StringSerializer()
    )
  }
}
