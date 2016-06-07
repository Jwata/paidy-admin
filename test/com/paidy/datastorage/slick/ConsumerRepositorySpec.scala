package com.paidy.datastorage.slick

import com.paidy.UnitSpec
import com.paidy.domain.Consumer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

class ConsumerRepositorySpec extends UnitSpec {

  class TestClass(val dbConfig: DatabaseConfig[JdbcProfile]) extends SlickConsumerRepository

  lazy val testClass = new TestClass(dbConfig)

  "SlickConsumerRepository#list" should "return list of consumers" in {
    val list = Await.result(testClass.ConsumerRepository.list(), Duration.Inf)
    list.length should be > 0
    list(0) shouldBe a[Consumer]
  }

  it should "return list ordered by created at desc" in {
    val list = Await.result(testClass.ConsumerRepository.list(), Duration.Inf)
    import com.github.nscala_time.time.Imports.DateTimeOrdering
    list shouldBe list.sortBy(_.createdAt).reverse
  }

}