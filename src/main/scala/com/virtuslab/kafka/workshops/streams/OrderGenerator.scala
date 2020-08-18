package com.virtuslab.kafka.workshops.streams

import java.time.Instant
import java.util.UUID

import com.virtuslab.kafka.workshops.schema.{ItemReadyToBeShipped, Order, OrderedProduct}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer


import scala.jdk.CollectionConverters._

object OrderGenerator {
  def main(args: Array[String]): Unit = {
    val orderProducer = new KafkaProducer[String, Order](
      Map[String, Object](
        "bootstrap.servers" -> "localhost:9092"
      ).asJava,
      new StringSerializer(),
      ApplicationBootstrap.avroSerde[Order].serializer()
    )
    val productsReadyProducer = new KafkaProducer[String, ItemReadyToBeShipped](
      Map[String, Object](
        "bootstrap.servers" -> "localhost:9092"
      ).asJava,
      new StringSerializer(),
      ApplicationBootstrap.avroSerde[ItemReadyToBeShipped].serializer()
    )
    while (true) {
      val orderId = UUID.randomUUID().toString
      val orderDate = Instant.now()
      val productId1 = UUID.randomUUID().toString
      val productId2 = UUID.randomUUID().toString
      val order = Order.newBuilder()
        .setItems(
          List(
            OrderedProduct.newBuilder().setProductId(productId1).setQuantity(3).build(),
            OrderedProduct.newBuilder().setProductId(productId2).setQuantity(3).build()
          ).asJava)
        .setCreatedTimestamp(orderDate.getEpochSecond)
        .setUserId("uid")
        .setOrderId("oid")
        .setTotalPrice("100")
        .build()
      orderProducer.send(new ProducerRecord[String, Order](OrdersReadyTopology.ORDER_PLACED_TOPIC, orderId, order))
      Thread.sleep(1000)
      val earlierProduct: ItemReadyToBeShipped = ItemReadyToBeShipped.newBuilder().setItemId(productId1).setOrderId(orderId).setReadyTimestamp(Instant.now().toEpochMilli).build()
      productsReadyProducer.send(new ProducerRecord[String, ItemReadyToBeShipped](OrdersReadyTopology.PRODUCT_READY_TOPIC, orderId, earlierProduct))
      Thread.sleep(1500)
      val laterProduct: ItemReadyToBeShipped = ItemReadyToBeShipped.newBuilder().setItemId(productId2).setOrderId(orderId).setReadyTimestamp(Instant.now().toEpochMilli).build()
      productsReadyProducer.send(new ProducerRecord[String, ItemReadyToBeShipped](OrdersReadyTopology.PRODUCT_READY_TOPIC, orderId, laterProduct))
    }
  }
}
