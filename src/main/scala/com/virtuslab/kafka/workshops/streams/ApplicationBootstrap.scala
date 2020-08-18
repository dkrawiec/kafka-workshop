package com.virtuslab.kafka.workshops.streams

import java.util.Properties
import java.util.concurrent.CountDownLatch

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

abstract class ApplicationBootstrap {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def addTopologies(builder: StreamsBuilder): Unit
  def applicationId: String

  def main(args: Array[String]): Unit = {
    val props = getStreamsConfig
    val builder = new StreamsBuilder
    addTopologies(builder)
    val topology = builder.build
    logger.info(topology.describe().toString)
    val streams = new KafkaStreams(topology, props)
    val latch = new CountDownLatch(1)

    Runtime.getRuntime.addShutdownHook(new Thread("streams-shutdown-hook") {
      override def run(): Unit = {
        logger.info("Stopping application")
        streams.close()
        latch.countDown()
      }
    })

    try {
      streams.start()
      latch.await()
    } catch {
      case e: Throwable =>
        System.exit(1)
    }
  }

  def getStreamsConfig: Properties = {
    val properties = new Properties()
    properties.putAll(Map[String, Object](
      StreamsConfig.APPLICATION_ID_CONFIG -> applicationId,
      StreamsConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
      StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG -> "0",
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest",
      StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG -> Serdes.String.getClass.getName
    ).asJava)
    properties
  }


}

object ApplicationBootstrap{
  implicit def avroSerde[T <: SpecificRecord]: SpecificAvroSerde[T] = {
    val value = new SpecificAvroSerde[T]()
    value.configure(Map(
      AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS -> "true",
      AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> "http://localhost:8081"
    ).asJava, false)
    value
  }
}
