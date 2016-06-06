package com.paidy.datastorage.slick

import java.sql.Timestamp

import com.paidy.datastorage.PaymentRepository
import com.paidy.domain.Payment

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait SlickPaymentRepository extends SlickBaseRepository {

  import dbConfig.driver.api._

  class PaymentsTable(tag: Tag) extends Table[Payment](tag, "payments") {

    def * = (entityId, merchantId, consumerId, createdAt, updatedAt, amount, test) <>(Payment.tupled, Payment.unapply)

    def entityId = column[String]("entity_id", O.PrimaryKey)

    def merchantId = column[String]("merchant_id")

    def consumerId = column[String]("consumer_id")

    def createdAt = column[Timestamp]("created_at")

    def updatedAt = column[Timestamp]("created_at")

    def amount = column[BigDecimal]("amount")

    def test = column[Boolean]("test")

  }

  object PaymentRepository extends PaymentRepository {
    lazy val payments = TableQuery[PaymentsTable]

    override def byId(entityId: String): Future[Option[Payment]] = {
      val byIdQuery = payments.filter(_.entityId === entityId).take(1).result
      db.run(byIdQuery).map(_.headOption)
    }

    override def byConsumerId(consumerId: String): Future[List[Payment]] = {
      val byConsumerIdQuery = payments.filter(_.consumerId === consumerId).result
      db.run(byConsumerIdQuery).map(_.toList)
    }
  }

}
