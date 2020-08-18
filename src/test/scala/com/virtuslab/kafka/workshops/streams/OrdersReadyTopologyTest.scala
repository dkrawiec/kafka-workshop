package com.virtuslab.kafka.workshops.streams

import com.virtuslab.kafka.workshops.schema.{ItemReadyToBeShipped, Order, OrderIsReady}
import org.apache.kafka.streams._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers._
import org.scalatest.{BeforeAndAfter, GivenWhenThen}


class OrdersReadyTopologyTest extends AnyFlatSpec
  with Matchers
  with BeforeAndAfter
  with GivenWhenThen {

  private implicit var testDriver: TopologyTestDriver = _
  private var orderPlacedTopic: TestInputTopic[String, Order] = _
  private var productReadyTopic: TestInputTopic[String, ItemReadyToBeShipped] = _
  private var outputTopic: TestOutputTopic[String, OrderIsReady] = _

  it should "match ready products with the order" in new TestStubs {
    Given("order and products ready")
    orderPlacedTopic.pipeInput(orderId1, orderWith2Products, orderDate1)
    productReadyTopic.pipeInput(orderId1, earlierProduct, product1ReadyDate)

    When("last product becomes available")
    productReadyTopic.pipeInput(orderId1, laterProduct, product2ReadyDate)

    Then("order is ready event should be emitted")
    val result = outputTopic.readKeyValue
    result.key should equal(orderId1)
    result.value should equal(OrderIsReady
      .newBuilder()
      .setOrder(orderWith2Products)
      .setReadyTimestamp(laterProduct.getReadyTimestamp)
      .build()
    )
    outputTopic.isEmpty should be
  }

  before {
    val builder = new StreamsBuilder
    OrdersReadyTopology(builder)
    testDriver = new TopologyTestDriver(builder.build, OrdersReadyApplication.getStreamsConfig)
    orderPlacedTopic = createInputTopic[String, Order](OrdersReadyTopology.ORDER_PLACED_TOPIC)
    productReadyTopic = createInputTopic[String, ItemReadyToBeShipped](OrdersReadyTopology.PRODUCT_READY_TOPIC)
    outputTopic = createOutputTopic[String, OrderIsReady](OrdersReadyTopology.OUTPUT_TOPIC)
  }

  after {
    try testDriver.close()
    catch {
      case e: RuntimeException =>
        System.out.println("Ignoring exception, test failing due this exception:" + e.getLocalizedMessage)
    }
  }
}
