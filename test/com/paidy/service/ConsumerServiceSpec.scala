package com.paidy.service

import com.paidy.{SpecUtility, UnitSpec}
import com.paidy.domain.Consumer
import org.scalatest.BeforeAndAfterEach

import scala.concurrent.duration.Duration
import scala.concurrent._

class ConsumerServiceSpec extends UnitSpec with SpecUtility with BeforeAndAfterEach {

  lazy val consmerService = new ConsumerService(dbConfig)

  override def beforeEach() = {
    deleteConsumersAll
    deletePaymentsAll
    createConsumers(5)
  }

  "ConsumerService#list" should "return list of consumers" in {
    val list = Await.result(consmerService.list(), Duration.Inf)
    list.length should be > 0
    list(0) shouldBe a[Consumer]
  }

  "ConsumerService#listWithPayments" should "return list of consumers with payments" in {
    val consumerId = Await.result(createConsumer, Duration.Inf)
    createPayments(num = 5, consumerId = Some(consumerId))

    val list = Await.result(consmerService.listWithPaymentSummary(), Duration.Inf)
    list.length should be > 0
    list(0) shouldBe a[ConsumerWithPaymentSummary]
  }

  "ConsumerService#disableById" should "change the target's status to Disabled" in {
    val entityId = Await.result(createConsumer, Duration.Inf)

    val consumerBefore = Await.result(consmerService.list(), Duration.Inf).filter(_.entityId == entityId).head
    consumerBefore.status shouldEqual Consumer.Status.Enabled

    Await.result(consmerService.disableById(entityId), Duration.Inf)

    val consumerAfter = Await.result(consmerService.list(), Duration.Inf).filter(_.entityId == entityId).head
    consumerAfter.status shouldEqual Consumer.Status.Disabled
  }
}
