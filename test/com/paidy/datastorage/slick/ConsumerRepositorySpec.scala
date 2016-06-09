package com.paidy.datastorage.slick

import com.paidy.{SpecUtility, UnitSpec}
import com.paidy.domain.Consumer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.util.Random

class ConsumerRepositorySpec extends UnitSpec with SpecUtility {

  class TestSlickConsumerRepository(val dbConfig: DatabaseConfig[JdbcProfile]) extends SlickConsumerRepository

  lazy val repository = new TestSlickConsumerRepository(dbConfig).ConsumerRepository

  override def beforeEach() = {
    deleteConsumersAll
  }

  "SlickConsumerRepository#list" should "return list of consumers" in {
    val num = 5
    Await.result(createConsumers(num), Duration.Inf)

    val list = Await.result(repository.list(), Duration.Inf)
    list.length shouldEqual num
    list(0) shouldBe a[Consumer]
  }

  it should "return list ordered by created at desc" in {
    val num = 5
    Await.result(createConsumers(num), Duration.Inf)

    val list = Await.result(repository.list(), Duration.Inf)
    import com.github.nscala_time.time.Imports.DateTimeOrdering
    list shouldBe list.sortBy(_.createdAt).reverse
  }

  it should "return list filtered by status" in {
    val num = 5
    Await.result(createConsumers(num), Duration.Inf)

    val allList = Await.result(repository.list(), Duration.Inf)
    val enabledList = Await.result(repository.list(status = Some(Consumer.Status.Enabled)), Duration.Inf)
    val disabledList = Await.result(repository.list(status = Some(Consumer.Status.Disabled)), Duration.Inf)
    allList.length shouldEqual num
    enabledList.length shouldEqual num
    disabledList.length shouldEqual 0

    // disable a consumer
    val targetConsumer = allList(Random.nextInt(num))
    Await.result(repository.disableById(targetConsumer.entityId), Duration.Inf)

    val allListAfter = Await.result(repository.list(), Duration.Inf)
    val enabledListAfter = Await.result(repository.list(status = Some(Consumer.Status.Enabled)), Duration.Inf)
    val disabledListAfter = Await.result(repository.list(status = Some(Consumer.Status.Disabled)), Duration.Inf)
    allListAfter.length shouldEqual num
    enabledListAfter.length shouldEqual num - 1
    disabledListAfter.length shouldEqual 1
  }

  "SlickConsumerRepository#disableById" should "change consumer's status to Disabled" in {
    val entityId = Await.result(createConsumer, Duration.Inf)

    val consumerBefore = Await.result(repository.byId(entityId), Duration.Inf).get
    consumerBefore.status shouldEqual Consumer.Status.Enabled

    val result = Await.result(repository.disableById(entityId), Duration.Inf)
    result shouldBe 1

    val consumerAfter = Await.result(repository.byId(entityId), Duration.Inf).get
    consumerAfter.status shouldEqual Consumer.Status.Disabled
  }

  it should "not change any resources" in {
    val entityId = randomString
    val result = Await.result(repository.disableById(entityId), Duration.Inf)
    result shouldBe 0
  }

  "SlickConsumerRepository#count" should "return number of consumers" in {
    val num = 5
    Await.result(createConsumers(num), Duration.Inf)

    val count = Await.result(repository.count, Duration.Inf)
    count shouldBe num
  }
}