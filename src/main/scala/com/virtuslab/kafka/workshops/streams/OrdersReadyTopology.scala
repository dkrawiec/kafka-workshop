package com.virtuslab.kafka.workshops.streams

import org.apache.kafka.streams.scala.StreamsBuilder

object OrdersReadyTopology {
  val ORDER_PLACED_TOPIC = "order-placed"
  val PRODUCT_READY_TOPIC = "product-ready"
  val OUTPUT_TOPIC = "order-ready"

  def apply(builder: StreamsBuilder): Unit = {

  }
}
