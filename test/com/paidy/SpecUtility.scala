package com.paidy

import com.paidy.datastorage.slick.{SlickPaymentRepository, SlickConsumerRepository}
import com.paidy.domain.{Payment, Consumer}

import org.joda.time.DateTime
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.UUID.randomUUID

import scala.util.Random

trait SpecUtility {

  val dbConfig: DatabaseConfig[JdbcProfile]

  import dbConfig.driver.api._

  class UtilRepositoryClass(val dbConfig: DatabaseConfig[JdbcProfile])
    extends SlickConsumerRepository with SlickPaymentRepository

  lazy val utilRepository = new UtilRepositoryClass(dbConfig)
  lazy val utilConsumerRepository = utilRepository.ConsumerRepository
  lazy val utilPaymentRepository = utilRepository.PaymentRepository

  def createConsumer: Future[String] = {
    val entityId = randomString
    val consumer = randomConsumer(Some(entityId))
    val insertQuery = DBIO.seq(
      utilConsumerRepository.consumers += consumer
    )
    dbConfig.db.run(insertQuery).map(_ => entityId)
  }

  def createConsumers(num: Int): Future[Unit] = {
    val consumers = for {
      i <- 1 to num
    } yield randomConsumer()
    val insertQuery = DBIO.seq(
      utilConsumerRepository.consumers ++= consumers
    )
    dbConfig.db.run(insertQuery)
  }

  def randomConsumer(entityId: Option[String] = None): Consumer = {
    Consumer(
      entityId.getOrElse(randomString),
      Consumer.Status.Enabled,
      Some(randomString),
      randomString,
      randomString,
      new DateTime(Random.nextLong),
      new DateTime(Random.nextLong),
      true
    )
  }

  def deleteConsumersAll: Future[Int] = {
    val deleteQuery = utilConsumerRepository.consumers.delete
    dbConfig.db.run(deleteQuery)
  }

  def createPayment(consumerId: Option[String] = None): Future[String] = {
    val entityId = randomString
    val payment = randomPayment(entityId = Some(entityId), consumerId = consumerId)
    val insertQuery = DBIO.seq(
      utilPaymentRepository.payments += payment
    )
    dbConfig.db.run(insertQuery).map(_ => entityId)
  }

  def createPayments(num: Int, consumerId: Option[String] = None): Future[Unit] = {
    val payments = for {
      i <- 1 to num
    } yield randomPayment(consumerId = consumerId)
    val insertQuery = DBIO.seq(
      utilPaymentRepository.payments ++= payments
    )
    dbConfig.db.run(insertQuery)
  }

  def deletePaymentsAll: Future[Int] = {
    val deleteQuery = utilPaymentRepository.payments.delete
    dbConfig.db.run(deleteQuery)
  }

  def randomPayment(entityId: Option[String] = None, consumerId: Option[String]): Payment = {
    Payment(
      entityId.getOrElse(randomString),
      randomString,
      consumerId.getOrElse(randomString),
      new DateTime(Random.nextLong),
      new DateTime(Random.nextLong),
      BigDecimal(Random.nextInt(10000)),
      true
    )
  }

  def randomString: String = randomUUID().toString

}
