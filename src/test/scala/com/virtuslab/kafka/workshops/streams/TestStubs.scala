package com.virtuslab.kafka.workshops.streams

import java.time.{Instant, LocalDateTime, ZoneId, ZoneOffset}

import com.virtuslab.kafka.workshops.schema.{ItemReadyToBeShipped, Order, OrderedProduct}

import scala.jdk.CollectionConverters._

trait TestStubs {
  val orderId1 = "oid1"
  val orderId2 = "oid2"
  val orderId3 = "oid3"
  private val start: LocalDateTime = LocalDateTime.now().minusDays(14)
  val orderDate1 = start.toInstant(ZoneOffset.UTC)
  val orderDate2 = start.plusDays(7).toInstant(ZoneOffset.UTC)
  val orderDate3 = start.plusDays(14).toInstant(ZoneOffset.UTC)
  val productId1 = "pid1"
  val productId2 = "pid2"
  val order1 = Order.newBuilder()
    .setItems(List(OrderedProduct.newBuilder().setProductId(productId1).setQuantity(3).build()).asJava)
    .setCreatedTimestamp(orderDate1.getEpochSecond)
    .setUserId("uid")
    .setOrderId(orderId1)
    .setTotalPrice("100")
    .build()
  val order2 = Order.newBuilder()
    .setItems(List(OrderedProduct.newBuilder().setProductId(productId1).setQuantity(1).build()).asJava)
    .setCreatedTimestamp(orderDate2.getEpochSecond)
    .setUserId("uid")
    .setOrderId(orderId2)
    .setTotalPrice("100")
    .build()
  val order3 = Order.newBuilder()
    .setItems(List(OrderedProduct.newBuilder().setProductId(productId2).setQuantity(5).build()).asJava)
    .setCreatedTimestamp(orderDate3.getEpochSecond)
    .setUserId("uid")
    .setOrderId(orderId3)
    .setTotalPrice("100")
    .build()

  val orderWith2Products = Order.newBuilder()
    .setItems(List(OrderedProduct.newBuilder().setProductId(productId1).setQuantity(3).build(), OrderedProduct.newBuilder().setProductId(productId2).setQuantity(2).build()).asJava)
    .setCreatedTimestamp(orderDate1.getEpochSecond)
    .setUserId("uid")
    .setOrderId(orderId1)
    .setTotalPrice("100")
    .build()

  val product1ReadyDate: Instant = orderDate1.plusSeconds(300)
  val earlierProduct: ItemReadyToBeShipped = ItemReadyToBeShipped.newBuilder().setItemId(productId1).setOrderId(orderId1).setReadyTimestamp(product1ReadyDate.toEpochMilli).build()
  val product2ReadyDate: Instant = orderDate1.plusSeconds(600)
  val laterProduct: ItemReadyToBeShipped = ItemReadyToBeShipped.newBuilder().setItemId(productId2).setOrderId(orderId1).setReadyTimestamp(product2ReadyDate.toEpochMilli).build()

}
