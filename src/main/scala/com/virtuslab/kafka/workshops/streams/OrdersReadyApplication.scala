package com.virtuslab.kafka.workshops.streams

import com.virtuslab.kafka.workshops.streams.ApplicationBootstrap.avroSerde
import org.apache.kafka.streams.scala.StreamsBuilder

object OrdersReadyApplication extends ApplicationBootstrap {
  override def addTopologies(builder: StreamsBuilder): Unit = {
    OrdersReadyTopology(builder)
  }

  override def applicationId: String = "orders-ready-app"
}
