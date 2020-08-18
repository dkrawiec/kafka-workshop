package com.virtuslab.kafka.workshops.streams

import io.confluent.kafka.schemaregistry.client.{MockSchemaRegistryClient, SchemaRegistryClient}
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

import scala.jdk.CollectionConverters._

class MockedSchemaRegistrySerde() extends SpecificAvroSerde[SpecificRecord] {
  private val internal: Serde[SpecificRecord] = new SpecificAvroSerde[SpecificRecord](MockedSchemaRegistrySerde.schemaRegistryClient)
  internal.configure(Map(
    AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG -> "http://fake:8081",
    AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS -> "true"
  ).asJava, false)

  override def serializer(): Serializer[SpecificRecord] = internal.serializer()

  override def deserializer(): Deserializer[SpecificRecord] = internal.deserializer()
}

object MockedSchemaRegistrySerde {
  val schemaRegistryClient: SchemaRegistryClient = new MockSchemaRegistryClient()
}
