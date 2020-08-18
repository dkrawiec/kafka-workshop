package com.virtuslab.kafka.workshops

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.common.serialization.{Serde, StringDeserializer, StringSerializer}
import org.apache.kafka.streams.{TestInputTopic, TestOutputTopic, TopologyTestDriver}

package object streams {
  val mockedSchemaRegistrySerde = new MockedSchemaRegistrySerde
  val stringSerializer = new StringSerializer()
  val stringDeserializer = new StringDeserializer()

  implicit def avroSerde[T <: SpecificRecord]: SpecificAvroSerde[T] = mockedSchemaRegistrySerde.asInstanceOf[SpecificAvroSerde[T]]

  def createOutputTopic[K, V](topic: String)(implicit testDriver: TopologyTestDriver, keySerde: Serde[K], valueSerde: Serde[V]): TestOutputTopic[K, V] = {
    testDriver.createOutputTopic(topic, keySerde.deserializer(), valueSerde.deserializer())
  }

  def createInputTopic[K, V](topic: String)(implicit testDriver: TopologyTestDriver, keySerde: Serde[K], valueSerde: Serde[V]): TestInputTopic[K, V] = {
    testDriver.createInputTopic(topic, keySerde.serializer(), valueSerde.serializer())
  }
}

