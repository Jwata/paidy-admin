package service

import com.paidy.domain.Consumer
import com.paidy.service.ConsumerService
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
    val consumerList = Await.result(consmerService.list, Duration.Inf)
    consumerList.length should be > 0
    consumerList(0) shouldBe a[Consumer]
  }

}
