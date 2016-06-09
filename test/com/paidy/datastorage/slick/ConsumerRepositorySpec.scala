package com.paidy.datastorage.slick

import com.paidy.UnitSpec
import com.paidy.domain.Consumer
import org.joda.time.DateTime

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

class ConsumerRepositorySpec extends UnitSpec {

  class TestClass(val dbConfig: DatabaseConfig[JdbcProfile]) extends SlickConsumerRepository

  lazy val repository = new TestClass(dbConfig).ConsumerRepository
  lazy val db = dbConfig.db

  import dbConfig.driver.api._

  def createConsumer: Future[String] = {
    val entityId = randomString
    val insertQuery = DBIO.seq(
      repository.consumers += Consumer(
        entityId, Consumer.Status.Enabled, Some(randomString), randomString, randomString, new DateTime, new DateTime, true)
    )
    db.run(insertQuery).map(_ => entityId)
  }

  private def randomString: String = {
    scala.util.Random.alphanumeric.take(10).mkString
  }

  "SlickConsumerRepository#list" should "return list of consumers" in {
    val list = Await.result(repository.list(), Duration.Inf)
    list.length should be > 0
    list(0) shouldBe a[Consumer]
  }

  it should "return list ordered by created at desc" in {
    val list = Await.result(repository.list(), Duration.Inf)
    import com.github.nscala_time.time.Imports.DateTimeOrdering
    list shouldBe list.sortBy(_.createdAt).reverse
  }

  it should "return list filtered by status" in {
    val status = Consumer.Status.Enabled

    val allList = Await.result(repository.list(), Duration.Inf)
    val filteredList = Await.result(repository.list(status = Some(status)), Duration.Inf)

    filteredList.length should be < allList.length
    filteredList.foreach { c =>
      c.status should be equals status
    }
  }

  "SlickConsumerRepository#disableById" should "change consumer's status to Disabled" in {
    val entityId = Await.result(createConsumer, Duration.Inf)

    val consumerBefore = Await.result(repository.byId(entityId), Duration.Inf).get
    consumerBefore.status should be equals Consumer.Status.Enabled

    val result = Await.result(repository.disableById(entityId), Duration.Inf)
    result shouldBe 1

    val consumerAfter = Await.result(repository.byId(entityId), Duration.Inf).get
    consumerAfter.status should be equals Consumer.Status.Disabled
  }

  it should "not change any resources" in {
    val entityId = randomString
    val result = Await.result(repository.disableById(entityId), Duration.Inf)
    result shouldBe 0
  }
}