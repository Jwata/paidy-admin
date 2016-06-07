package com.paidy.service

import java.sql.Timestamp

import com.paidy.datastorage.slick._
import com.paidy.domain.{Payment, Consumer}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ConsumerService(val dbConfig: DatabaseConfig[JdbcProfile])
  extends SlickConsumerRepository with SlickPaymentRepository {

  def list(status: Option[String] = None): Future[List[Consumer]] = {
    ConsumerRepository.list(status = status)
  }

  def listWithPaymentSummary(status: Option[String] = None): Future[List[ConsumerWithPaymentSummary]] = {
    list(status = status).map { l =>
      l.map { c =>
        getPaymentsSummaryByConsumer(c)
      }
    }.flatMap(s => Future.sequence(s))
  }

  private def getPaymentsSummaryByConsumer(consumer: Consumer): Future[ConsumerWithPaymentSummary] = {
    PaymentRepository.byConsumerId(consumer.entityId).map {
      case payments if payments.length > 0 =>
        val totalAmount = payments.map(_.amount).sum
        ConsumerWithPaymentSummary(
          consumer,
          Some(totalAmount),
          Some(totalAmount / payments.length),
          Some(payments.head.createdAt))
      case payments =>
        ConsumerWithPaymentSummary(consumer, None, None, None)
    }
  }

}

case class ConsumerWithPaymentSummary(
  consumer: Consumer,
  totalAmount: Option[Int],
  averageAmount: Option[Int],
  lastPaymentAt: Option[Timestamp]
)
