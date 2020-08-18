package com.virtuslab.kafka.workshops.streams

import com.virtuslab.kafka.workshops.streams.ApplicationBootstrap.avroSerde
import org.apache.kafka.streams.scala.StreamsBuilder

object ProductStatsApplication extends ApplicationBootstrap {
  override def addTopologies(builder: StreamsBuilder): Unit = {
    ProductStatsTopology(builder)
  }

  override def applicationId: String = "product-stats-app"
}
