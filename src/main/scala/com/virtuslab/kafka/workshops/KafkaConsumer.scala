package com.virtuslab.kafka.workshops

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._

object KafkaConsumer {
  def main(args: Array[String]): Unit = {
    val consumer = new KafkaConsumer[String, String](
      Map[String, Object](
        "bootstrap.servers" -> "localhost:9092"
      ).asJava,
      new StringDeserializer(),
      new StringDeserializer()
    )
  }
}
