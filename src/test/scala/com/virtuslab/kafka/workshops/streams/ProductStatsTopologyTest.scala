package com.virtuslab.kafka.workshops.streams

import java.time.{Duration, Instant}

import com.virtuslab.kafka.workshops.schema.Order
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream.{TimeWindows, Windowed, WindowedSerdes}
import org.apache.kafka.streams.scala.Serdes.{Integer, String}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers._
import org.scalatest.{BeforeAndAfter, GivenWhenThen}

import _root_.scala.jdk.CollectionConverters._


class ProductStatsTopologyTest extends AnyFlatSpec
  with Matchers
  with BeforeAndAfter
  with GivenWhenThen {
  private implicit var testDriver: TopologyTestDriver = _
  private var orderPlacedTopic: TestInputTopic[String, Order] = _
  private var outputTopic: TestOutputTopic[Windowed[String], Int] = _

  private val windows: TimeWindows = TimeWindows.of(Duration.ofDays(7))

  implicit def timeWindowedSerde[T](implicit tSerde: Serde[T]): WindowedSerdes.TimeWindowedSerde[T] =
    new WindowedSerdes.TimeWindowedSerde[T](tSerde, windows.size())

  it should "calculate stats" in new TestStubs {
    Given("order is placed")
    When("sent to kafka")
    orderPlacedTopic.pipeInput(orderId1, order1, orderDate1)
    orderPlacedTopic.pipeInput(orderId2, order2, orderDate2)
    orderPlacedTopic.pipeInput(orderId3, order3, orderDate3)

    Then("result should contain window with expected amount")
    val result = outputTopic.readKeyValuesToMap()
    println(result)
    result.get(keyForTimestampAndKey[String](orderDate1, productId1)) should equal(3)
    result.get(keyForTimestampAndKey[String](orderDate2, productId1)) should equal(1)
    result.get(keyForTimestampAndKey[String](orderDate3, productId2)) should equal(5)
  }

  private def keyForTimestampAndKey[T](orderDate1: Instant, productId1: T) =
    new Windowed[T](productId1, windows.windowsFor(orderDate1.toEpochMilli).asScala.head._2)

  before {
    val builder = new StreamsBuilder
    ProductStatsTopology(builder)
    testDriver = new TopologyTestDriver(builder.build, OrdersReadyApplication.getStreamsConfig)
    orderPlacedTopic = createInputTopic[String, Order](ProductStatsTopology.INPUT_TOPIC)
    outputTopic = createOutputTopic[Windowed[String], Int](ProductStatsTopology.OUTPUT_TOPIC)
  }

  after {
    try testDriver.close()
    catch {
      case e: RuntimeException =>
        System.out.println("Ignoring exception, test failing due this exception:" + e.getLocalizedMessage)
    }
  }
}
