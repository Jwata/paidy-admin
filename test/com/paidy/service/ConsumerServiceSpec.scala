package com.paidy.service

import com.paidy.{SpecUtility, UnitSpec}
import com.paidy.domain.Consumer
import org.scalatest.BeforeAndAfterEach

import scala.concurrent.duration.Duration
import scala.concurrent._

class ConsumerServiceSpec extends UnitSpec with SpecUtility with BeforeAndAfterEach {

  lazy val consmerService = new ConsumerService(dbConfig)

  override def beforeEach() = {
    deleteConsumerAll
    createConsumers(5)
  }

  "ConsumerService#list" should "return list of consumers" in {
    val list = Await.result(consmerService.list(), Duration.Inf)
    list.length should be > 0
    list(0) shouldBe a[Consumer]
  }

  "ConsumerService#listWithPayments" should "return list of consumers with payments" in {
    val list = Await.result(consmerService.listWithPaymentSummary(), Duration.Inf)
    list.length should be > 0
    list(0) shouldBe a[ConsumerWithPaymentSummary]
  }

  // TODO: add more tests to be sure that requests are delegated properly

}
