package com.paidy

import com.paidy.datastorage.slick.SlickConsumerRepository
import com.paidy.domain.Consumer

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

  class UtilRepositoryClass(val dbConfig: DatabaseConfig[JdbcProfile]) extends SlickConsumerRepository

  lazy val utilRepository = new UtilRepositoryClass(dbConfig).ConsumerRepository

  def createConsumer: Future[String] = {
    val entityId = randomString
    val consumer = randomConsumer(Some(entityId))
    val insertQuery = DBIO.seq(
      utilRepository.consumers += consumer
    )
    dbConfig.db.run(insertQuery).map(_ => entityId)
  }

  def createConsumers(num: Int): Future[Unit] = {
    val consumers = for {
      i <- 1 to num
    } yield randomConsumer()
    val insertQuery = DBIO.seq(
      utilRepository.consumers ++= consumers
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

  def deleteConsumerAll: Future[Int] = {
    val deleteQuery = utilRepository.consumers.delete
    dbConfig.db.run(deleteQuery)
  }

  def randomString: String = randomUUID().toString

}
