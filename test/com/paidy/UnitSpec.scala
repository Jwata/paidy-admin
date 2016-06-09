package com.paidy

import org.scalatest._
import play.api.db.slick.DatabaseConfigProvider
import play.api.test.FakeApplication
import slick.driver.JdbcProfile

abstract class UnitSpec extends FlatSpec with Matchers with BeforeAndAfterEach {

  lazy val app = FakeApplication(
    additionalConfiguration = Map(
      "slick.dbs.default.db.url" -> "jdbc:h2:mem:paidy_admin_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
    )
  )

  lazy val dbConfig = DatabaseConfigProvider.get[JdbcProfile](app)

}
