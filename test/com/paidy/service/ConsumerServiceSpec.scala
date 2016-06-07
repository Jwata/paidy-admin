package service

import com.paidy.domain.{Payment, Consumer}
import com.paidy.service.{ConsumerWithPaymentSummary, ConsumerService}
import org.scalatest._
import play.api.db.slick.DatabaseConfigProvider
import play.api.test.FakeApplication
import slick.driver.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent._

class ConsumerServiceSpec extends FlatSpec with Matchers {

  lazy val app = FakeApplication(
    additionalConfiguration = Map(
      "slick.dbs.default.db.url" -> "jdbc:h2:mem:paidy_admin_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
    )
  )
  lazy val dbConfig = DatabaseConfigProvider.get[JdbcProfile](app)
  lazy val consmerService = new ConsumerService(dbConfig)

  "ConsumerService#list" should "return list of consumers" in {
    val list = Await.result(consmerService.list(), Duration.Inf)
    list.length should be > 0
    list(0) shouldBe a[Consumer]
  }

  "ConsumerService#listWithPayments" should "return list of consumers with payments" in {
    val list = Await.result(consmerService.listWithPaymentSummary(), Duration.Inf)
    list.length should be > 0
    list(0) shouldBe a[ConsumerWithPaymentSummary]
  }

  it should "return filtered consumers by given status" in {
    val status = "enabled"

    val allList = Await.result(consmerService.listWithPaymentSummary(), Duration.Inf)
    val filteredList = Await.result(consmerService.listWithPaymentSummary(status = Some(status)), Duration.Inf)

    filteredList.length should be < allList.length
    filteredList.foreach { e =>
      e.consumer.status should be equals status
    }
  }

}
