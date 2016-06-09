package com.paidy.datastorage.slick

import com.paidy.{SpecUtility, UnitSpec}
import com.paidy.domain.Payment

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.util.Random

class PaymentRepositorySpec extends UnitSpec with SpecUtility {

  class TestSlickPaymentRepository(val dbConfig: DatabaseConfig[JdbcProfile]) extends SlickPaymentRepository

  lazy val repository = new TestSlickPaymentRepository(dbConfig).PaymentRepository

  override def beforeEach() = {
    deletePaymentsAll
  }

  "SlickPaymentRepository#byId" should "return a payment specified by id" in {
    val entityId = Await.result(createPayment(), Duration.Inf)

    val payment = Await.result(repository.byId(entityId), Duration.Inf).get
    payment shouldBe a[Payment]
  }

  "SlickPaymentRepository#byConsumerId" should "return list of payments specified by consumer id" in {
    val consumerId = Await.result(createConsumer, Duration.Inf)
    val num = 10
    Await.result(createPayments(num, Some(consumerId)), Duration.Inf)

    val payments = Await.result(repository.byConsumerId(consumerId), Duration.Inf)
    payments.length shouldEqual num
    payments.foreach { p =>
      p shouldBe a[Payment]
      p.consumerId shouldEqual consumerId
    }
  }


}