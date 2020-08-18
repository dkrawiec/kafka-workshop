package com.virtuslab.kafka.workshops.streams

import org.apache.kafka.streams.scala.StreamsBuilder

object ProductStatsTopology {
  val INPUT_TOPIC = "order-placed"
  val OUTPUT_TOPIC = "product-stats"

  def apply(builder: StreamsBuilder): Unit = {

  }
}
