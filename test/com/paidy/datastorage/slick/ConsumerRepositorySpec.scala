package com.paidy.datastorage.slick

import com.paidy.UnitSpec
import com.paidy.domain.Consumer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

class ConsumerRepositorySpec extends UnitSpec {

  class TestClass(val dbConfig: DatabaseConfig[JdbcProfile]) extends SlickConsumerRepository

  lazy val repository = new TestClass(dbConfig).ConsumerRepository

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
}