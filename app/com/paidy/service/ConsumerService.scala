package com.paidy.service

import com.paidy.datastorage.slick._
import com.paidy.domain.{Payment, Consumer}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class ConsumerService(val dbConfig: DatabaseConfig[JdbcProfile])
  extends SlickConsumerRepository with SlickPaymentRepository {

  def list: Future[List[Consumer]] = ConsumerRepository.list()

  def listWithPayments: Future[List[(Consumer, Future[List[Payment]])]] = {
    list.map { l =>
      l.map { c =>
        (c, PaymentRepository.byConsumerId(c.entityId))
      }
    }
  }
}